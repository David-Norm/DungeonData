import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class ClassSpeciesView extends JPanel {
    private DnDController controller;
    private JTable classTable;
    private JTable subclassTable;
    private JTable speciesTable;
    private JTable subspeciesTable;

    public ClassSpeciesView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        classTable = new JTable();
        subclassTable = new JTable();
        speciesTable = new JTable();
        subspeciesTable = new JTable();

        // Set selection modes
        classTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subclassTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        speciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subspeciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add double-click listeners for description columns
        classTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleClassTableDoubleClick(e);
                }
            }
        });

        speciesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleSpeciesTableDoubleClick(e);
                }
            }
        });

        // Add selection listeners to show related data
        classTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSubclassesForSelectedClass();
            }
        });

        speciesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSubspeciesForSelectedSpecies();
            }
        });
    }

    private void setupLayout() {
        setLayout(new GridLayout(2, 2, 10, 10));

        // Classes panel
        JPanel classPanel = new JPanel(new BorderLayout());
        JScrollPane classScrollPane = new JScrollPane(classTable);
        classScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Classes - Double-click Description for full text"));
        classScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel classButtonPanel = new JPanel(new FlowLayout());
        JButton refreshClassesBtn = new JButton("Refresh Classes");
        refreshClassesBtn.addActionListener(e -> loadClasses());
        classButtonPanel.add(refreshClassesBtn);

        classPanel.add(classScrollPane, BorderLayout.CENTER);
        classPanel.add(classButtonPanel, BorderLayout.SOUTH);

        // Subclasses panel
        JPanel subclassPanel = new JPanel(new BorderLayout());
        JScrollPane subclassScrollPane = new JScrollPane(subclassTable);
        subclassScrollPane.setBorder(BorderFactory.createTitledBorder("Subclasses"));
        subclassScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel subclassButtonPanel = new JPanel(new FlowLayout());
        JButton showAllSubclassesBtn = new JButton("Show All Subclasses");
        JButton clearSubclassFilterBtn = new JButton("Clear Filter");
        showAllSubclassesBtn.addActionListener(e -> loadAllSubclasses());
        clearSubclassFilterBtn.addActionListener(e -> clearSubclassTable());
        subclassButtonPanel.add(showAllSubclassesBtn);
        subclassButtonPanel.add(clearSubclassFilterBtn);

        subclassPanel.add(subclassScrollPane, BorderLayout.CENTER);
        subclassPanel.add(subclassButtonPanel, BorderLayout.SOUTH);

        // Species panel
        JPanel speciesPanel = new JPanel(new BorderLayout());
        JScrollPane speciesScrollPane = new JScrollPane(speciesTable);
        speciesScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Species - Double-click Description for full text"));
        speciesScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel speciesButtonPanel = new JPanel(new FlowLayout());
        JButton refreshSpeciesBtn = new JButton("Refresh Species");
        refreshSpeciesBtn.addActionListener(e -> loadSpecies());
        speciesButtonPanel.add(refreshSpeciesBtn);

        speciesPanel.add(speciesScrollPane, BorderLayout.CENTER);
        speciesPanel.add(speciesButtonPanel, BorderLayout.SOUTH);

        // Subspecies panel
        JPanel subspeciesPanel = new JPanel(new BorderLayout());
        JScrollPane subspeciesScrollPane = new JScrollPane(subspeciesTable);
        subspeciesScrollPane.setBorder(BorderFactory.createTitledBorder("Subspecies"));
        subspeciesScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel subspeciesButtonPanel = new JPanel(new FlowLayout());
        JButton showAllSubspeciesBtn = new JButton("Show All Subspecies");
        JButton clearSubspeciesFilterBtn = new JButton("Clear Filter");
        showAllSubspeciesBtn.addActionListener(e -> loadAllSubspecies());
        clearSubspeciesFilterBtn.addActionListener(e -> clearSubspeciesTable());
        subspeciesButtonPanel.add(showAllSubspeciesBtn);
        subspeciesButtonPanel.add(clearSubspeciesFilterBtn);

        subspeciesPanel.add(subspeciesScrollPane, BorderLayout.CENTER);
        subspeciesPanel.add(subspeciesButtonPanel, BorderLayout.SOUTH);

        // Add all panels to the main layout
        add(classPanel);        // Top-left
        add(subclassPanel);     // Top-right
        add(speciesPanel);      // Bottom-left
        add(subspeciesPanel);   // Bottom-right
    }

    public void refreshData() {
        loadClasses();
        loadSpecies();
        clearSubclassTable();
        clearSubspeciesTable();
    }

    private void loadClasses() {
        try {
            List<DnDClass> classes = controller.getAllClasses();
            Vector<String> classColumnNames = new Vector<>();
            classColumnNames.add("Class");
            classColumnNames.add("Casting Stat");
            classColumnNames.add("Primary Stat");
            classColumnNames.add("Secondary Stat");
            classColumnNames.add("Description");

            Vector<Vector<Object>> classData = new Vector<>();
            for (DnDClass dndClass : classes) {
                Vector<Object> row = new Vector<>();
                row.add(dndClass.getClassId());
                row.add(dndClass.getCastingStat() != null ? dndClass.getCastingStat() : "None");
                row.add(dndClass.getPrimaryStat());
                row.add(dndClass.getSecondaryStat());
                row.add(dndClass.getClassSummary().length() > 100 ?
                        dndClass.getClassSummary().substring(0, 100) + "..." :
                        dndClass.getClassSummary());
                classData.add(row);
            }

            DefaultTableModel classModel = new DefaultTableModel(classData, classColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            classTable.setModel(classModel);
            classTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading classes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllSubclasses() {
        try {
            List<String> subclasses = controller.getSubclasses();
            Vector<String> subclassColumnNames = new Vector<>();
            subclassColumnNames.add("Subclass");
            subclassColumnNames.add("Parent Class");

            Vector<Vector<Object>> subclassData = new Vector<>();

            // Get all classes to find parent class for each subclass
            List<DnDClass> allClasses = controller.getAllClasses();

            for (String subclass : subclasses) {
                // Find which class this subclass belongs to
                String parentClass = "Unknown";
                for (DnDClass dndClass : allClasses) {
                    List<String> classSubclasses = controller.getSubclassesByClass(dndClass.getClassId());
                    if (classSubclasses.contains(subclass)) {
                        parentClass = dndClass.getClassId();
                        break;
                    }
                }

                Vector<Object> row = new Vector<>();
                row.add(subclass);
                row.add(parentClass);
                subclassData.add(row);
            }

            DefaultTableModel subclassModel = new DefaultTableModel(subclassData, subclassColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            subclassTable.setModel(subclassModel);
            subclassTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subclasses: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSubclassesForSelectedClass() {
        int selectedRow = classTable.getSelectedRow();
        if (selectedRow == -1) {
            clearSubclassTable();
            return;
        }

        try {
            String selectedClassName = (String) classTable.getValueAt(selectedRow, 0);
            List<String> subclasses = controller.getSubclassesByClass(selectedClassName);

            Vector<String> subclassColumnNames = new Vector<>();
            subclassColumnNames.add("Subclass");
            subclassColumnNames.add("Parent Class");

            Vector<Vector<Object>> subclassData = new Vector<>();
            for (String subclass : subclasses) {
                Vector<Object> row = new Vector<>();
                row.add(subclass);
                row.add(selectedClassName);
                subclassData.add(row);
            }

            DefaultTableModel subclassModel = new DefaultTableModel(subclassData, subclassColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            subclassTable.setModel(subclassModel);
            subclassTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Update border title to show filter
            ((JScrollPane)subspeciesTable.getParent().getParent()).setBorder(
                    BorderFactory.createTitledBorder("Subclasses for " + selectedClassName)
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subclasses for selected class: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSpecies() {
        try {
            List<Species> species = controller.getAllSpecies();
            Vector<String> speciesColumnNames = new Vector<>();
            speciesColumnNames.add("Species");
            speciesColumnNames.add("Size");
            speciesColumnNames.add("Description");

            Vector<Vector<Object>> speciesData = new Vector<>();
            for (Species sp : species) {
                Vector<Object> row = new Vector<>();
                row.add(sp.getSpeciesId());
                row.add(sp.getSpeciesSize());
                row.add(sp.getSpeciesSummary().length() > 150 ?
                        sp.getSpeciesSummary().substring(0, 150) + "..." :
                        sp.getSpeciesSummary());
                speciesData.add(row);
            }

            DefaultTableModel speciesModel = new DefaultTableModel(speciesData, speciesColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            speciesTable.setModel(speciesModel);
            speciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading species: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllSubspecies() {
        try {
            List<String> subspecies = controller.getSubspecies();
            Vector<String> subspeciesColumnNames = new Vector<>();
            subspeciesColumnNames.add("Subspecies");
            subspeciesColumnNames.add("Parent Species");

            Vector<Vector<Object>> subspeciesData = new Vector<>();

            // Get all species to find parent species for each subspecies
            List<Species> allSpecies = controller.getAllSpecies();

            for (String subspecies_name : subspecies) {
                // Find which species this subspecies belongs to
                String parentSpecies = "Unknown";
                for (Species species : allSpecies) {
                    List<String> speciesSubspecies = controller.getSubspeciesBySpecies(species.getSpeciesId());
                    if (speciesSubspecies.contains(subspecies_name)) {
                        parentSpecies = species.getSpeciesId();
                        break;
                    }
                }

                Vector<Object> row = new Vector<>();
                row.add(subspecies_name);
                row.add(parentSpecies);
                subspeciesData.add(row);
            }

            DefaultTableModel subspeciesModel = new DefaultTableModel(subspeciesData, subspeciesColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            subspeciesTable.setModel(subspeciesModel);
            subspeciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subspecies: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSubspeciesForSelectedSpecies() {
        int selectedRow = speciesTable.getSelectedRow();
        if (selectedRow == -1) {
            clearSubspeciesTable();
            return;
        }

        try {
            String selectedSpeciesName = (String) speciesTable.getValueAt(selectedRow, 0);
            List<String> subspecies = controller.getSubspeciesBySpecies(selectedSpeciesName);

            Vector<String> subspeciesColumnNames = new Vector<>();
            subspeciesColumnNames.add("Subspecies");
            subspeciesColumnNames.add("Parent Species");

            Vector<Vector<Object>> subspeciesData = new Vector<>();
            for (String subspecies_name : subspecies) {
                Vector<Object> row = new Vector<>();
                row.add(subspecies_name);
                row.add(selectedSpeciesName);
                subspeciesData.add(row);
            }

            DefaultTableModel subspeciesModel = new DefaultTableModel(subspeciesData, subspeciesColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            subspeciesTable.setModel(subspeciesModel);
            subspeciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Update border title to show filter
            ((JScrollPane)subspeciesTable.getParent().getParent()).setBorder(
                    BorderFactory.createTitledBorder("Subspecies for " + selectedSpeciesName)
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subspecies for selected species: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearSubclassTable() {
        DefaultTableModel emptyModel = new DefaultTableModel();
        emptyModel.addColumn("Subclass");
        emptyModel.addColumn("Parent Class");
        subclassTable.setModel(emptyModel);

        // Reset border title
        ((JScrollPane)subclassTable.getParent().getParent()).setBorder(
                BorderFactory.createTitledBorder("Subclasses")
        );
    }

    private void clearSubspeciesTable() {
        DefaultTableModel emptyModel = new DefaultTableModel();
        emptyModel.addColumn("Subspecies");
        emptyModel.addColumn("Parent Species");
        subspeciesTable.setModel(emptyModel);

        // Reset border title
        ((JScrollPane)subspeciesTable.getParent().getParent()).setBorder(
                BorderFactory.createTitledBorder("Subspecies")
        );
    }

    private void handleClassTableDoubleClick(java.awt.event.MouseEvent e) {
        int row = classTable.rowAtPoint(e.getPoint());
        int col = classTable.columnAtPoint(e.getPoint());

        if (row >= 0 && col >= 0) {
            String columnName = classTable.getColumnName(col);

            // Check if it's the Description column (index 4)
            if (columnName.equals("Description")) {
                try {
                    String className = (String) classTable.getValueAt(row, 0);

                    // Get the full description from the controller
                    List<DnDClass> classes = controller.getAllClasses();
                    String fullDescription = "";

                    for (DnDClass dndClass : classes) {
                        if (dndClass.getClassId().equals(className)) {
                            fullDescription = dndClass.getClassSummary();
                            break;
                        }
                    }

                    if (!fullDescription.isEmpty()) {
                        showTextPopup("Class Description: " + className, fullDescription);
                    } else {
                        JOptionPane.showMessageDialog(this, "No description available for this class",
                                "No Description", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading class description: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleSpeciesTableDoubleClick(java.awt.event.MouseEvent e) {
        int row = speciesTable.rowAtPoint(e.getPoint());
        int col = speciesTable.columnAtPoint(e.getPoint());

        if (row >= 0 && col >= 0) {
            String columnName = speciesTable.getColumnName(col);

            // Check if it's the Description column (index 2)
            if (columnName.equals("Description")) {
                try {
                    String speciesName = (String) speciesTable.getValueAt(row, 0);

                    // Get the full description from the controller
                    List<Species> speciesList = controller.getAllSpecies();
                    String fullDescription = "";

                    for (Species species : speciesList) {
                        if (species.getSpeciesId().equals(speciesName)) {
                            fullDescription = species.getSpeciesSummary();
                            break;
                        }
                    }

                    if (!fullDescription.isEmpty()) {
                        showTextPopup("Species Description: " + speciesName, fullDescription);
                    } else {
                        JOptionPane.showMessageDialog(this, "No description available for this species",
                                "No Description", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading species description: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showTextPopup(String title, String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(60);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 248, 248));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}