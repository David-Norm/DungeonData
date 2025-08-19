/**
 * Class and Species reference view for displaying D&D classes and species information.
 * Provides detailed information about classes, subclasses, species, and subspecies
 * in a four-panel layout with interactive filtering and description viewing.
 *
 * @author David Norman
 * @version Summer 2025
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class ClassSpeciesView extends JPanel {
    private DnDController myController;
    private JTable myClassTable;
    private JTable mySubclassTable;
    private JTable mySpeciesTable;
    private JTable mySubspeciesTable;

    /**
     * Constructs a ClassSpeciesView with the specified controller.
     * Initializes all components, sets up the layout, and loads initial data.
     *
     * @param theController the application controller for data operations
     */
    public ClassSpeciesView(DnDController theController) {
        myController = theController;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    /**
     * Initializes all GUI components including tables and event listeners.
     * Sets up single selection mode for all tables and adds mouse and selection listeners.
     */
    private void initializeComponents() {
        myClassTable = new JTable();
        mySubclassTable = new JTable();
        mySpeciesTable = new JTable();
        mySubspeciesTable = new JTable();

        // Set selection modes for all tables
        myClassTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mySubclassTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mySpeciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mySubspeciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add double-click listeners for description columns
        myClassTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent theEvent) {
                if (theEvent.getClickCount() == 2) {
                    handleClassTableDoubleClick(theEvent);
                }
            }
        });

        mySpeciesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent theEvent) {
                if (theEvent.getClickCount() == 2) {
                    handleSpeciesTableDoubleClick(theEvent);
                }
            }
        });

        // Add selection listeners to show related data
        myClassTable.getSelectionModel().addListSelectionListener(theEvent -> {
            if (!theEvent.getValueIsAdjusting()) {
                showSubclassesForSelectedClass();
            }
        });

        mySpeciesTable.getSelectionModel().addListSelectionListener(theEvent -> {
            if (!theEvent.getValueIsAdjusting()) {
                showSubspeciesForSelectedSpecies();
            }
        });
    }

    /**
     * Sets up the panel layout with four tables in a 2x2 grid.
     * Creates panels for classes, subclasses, species, and subspecies with associated buttons.
     */
    private void setupLayout() {
        setLayout(new GridLayout(2, 2, 10, 10));

        // Classes panel (top-left)
        JPanel classPanel = new JPanel(new BorderLayout());
        JScrollPane classScrollPane = new JScrollPane(myClassTable);
        classScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Classes - Double-click Description for full text"));
        classScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel classButtonPanel = new JPanel(new FlowLayout());
        JButton refreshClassesBtn = new JButton("Refresh Classes");
        refreshClassesBtn.addActionListener(theEvent -> loadClasses());
        classButtonPanel.add(refreshClassesBtn);

        classPanel.add(classScrollPane, BorderLayout.CENTER);
        classPanel.add(classButtonPanel, BorderLayout.SOUTH);

        // Subclasses panel (top-right)
        JPanel subclassPanel = new JPanel(new BorderLayout());
        JScrollPane subclassScrollPane = new JScrollPane(mySubclassTable);
        subclassScrollPane.setBorder(BorderFactory.createTitledBorder("Subclasses"));
        subclassScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel subclassButtonPanel = new JPanel(new FlowLayout());
        JButton showAllSubclassesBtn = new JButton("Show All Subclasses");
        JButton clearSubclassFilterBtn = new JButton("Clear Filter");
        showAllSubclassesBtn.addActionListener(theEvent -> loadAllSubclasses());
        clearSubclassFilterBtn.addActionListener(theEvent -> clearSubclassTable());
        subclassButtonPanel.add(showAllSubclassesBtn);
        subclassButtonPanel.add(clearSubclassFilterBtn);

        subclassPanel.add(subclassScrollPane, BorderLayout.CENTER);
        subclassPanel.add(subclassButtonPanel, BorderLayout.SOUTH);

        // Species panel (bottom-left)
        JPanel speciesPanel = new JPanel(new BorderLayout());
        JScrollPane speciesScrollPane = new JScrollPane(mySpeciesTable);
        speciesScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Species - Double-click Description for full text"));
        speciesScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel speciesButtonPanel = new JPanel(new FlowLayout());
        JButton refreshSpeciesBtn = new JButton("Refresh Species");
        refreshSpeciesBtn.addActionListener(theEvent -> loadSpecies());
        speciesButtonPanel.add(refreshSpeciesBtn);

        speciesPanel.add(speciesScrollPane, BorderLayout.CENTER);
        speciesPanel.add(speciesButtonPanel, BorderLayout.SOUTH);

        // Subspecies panel (bottom-right)
        JPanel subspeciesPanel = new JPanel(new BorderLayout());
        JScrollPane subspeciesScrollPane = new JScrollPane(mySubspeciesTable);
        subspeciesScrollPane.setBorder(BorderFactory.createTitledBorder("Subspecies"));
        subspeciesScrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel subspeciesButtonPanel = new JPanel(new FlowLayout());
        JButton showAllSubspeciesBtn = new JButton("Show All Subspecies");
        JButton clearSubspeciesFilterBtn = new JButton("Clear Filter");
        showAllSubspeciesBtn.addActionListener(theEvent -> loadAllSubspecies());
        clearSubspeciesFilterBtn.addActionListener(theEvent -> clearSubspeciesTable());
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

    /**
     * Refreshes all data in the view by reloading classes and species.
     * Clears the subclass and subspecies tables to reset filters.
     */
    public void refreshData() {
        loadClasses();
        loadSpecies();
        clearSubclassTable();
        clearSubspeciesTable();
    }

    /**
     * Loads class data from the database and populates the class table.
     * Includes class ID, casting stat, primary/secondary stats, and description.
     */
    private void loadClasses() {
        try {
            List<DnDClass> classes = myController.getAllClasses();
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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            myClassTable.setModel(classModel);
            myClassTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading classes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads all subclasses from the database and populates the subclass table.
     * Determines parent class for each subclass by checking class relationships.
     */
    private void loadAllSubclasses() {
        try {
            List<String> subclasses = myController.getSubclasses();
            Vector<String> subclassColumnNames = new Vector<>();
            subclassColumnNames.add("Subclass");
            subclassColumnNames.add("Parent Class");

            Vector<Vector<Object>> subclassData = new Vector<>();

            // Get all classes to find parent class for each subclass
            List<DnDClass> allClasses = myController.getAllClasses();

            for (String subclass : subclasses) {
                // Find which class this subclass belongs to
                String parentClass = "Unknown";
                for (DnDClass dndClass : allClasses) {
                    List<String> classSubclasses = myController.getSubclassesByClass(dndClass.getClassId());
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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            mySubclassTable.setModel(subclassModel);
            mySubclassTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subclasses: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows subclasses for the currently selected class.
     * Filters the subclass table to show only subclasses of the selected class.
     */
    private void showSubclassesForSelectedClass() {
        int selectedRow = myClassTable.getSelectedRow();
        if (selectedRow == -1) {
            clearSubclassTable();
            return;
        }

        try {
            String selectedClassName = (String) myClassTable.getValueAt(selectedRow, 0);
            List<String> subclasses = myController.getSubclassesByClass(selectedClassName);

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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            mySubclassTable.setModel(subclassModel);
            mySubclassTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Update border title to show filter
            ((JScrollPane)mySubspeciesTable.getParent().getParent()).setBorder(
                    BorderFactory.createTitledBorder("Subclasses for " + selectedClassName)
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subclasses for selected class: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads species data from the database and populates the species table.
     * Includes species ID, size category, and description summary.
     */
    private void loadSpecies() {
        try {
            List<Species> species = myController.getAllSpecies();
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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            mySpeciesTable.setModel(speciesModel);
            mySpeciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading species: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads all subspecies from the database and populates the subspecies table.
     * Determines parent species for each subspecies by checking species relationships.
     */
    private void loadAllSubspecies() {
        try {
            List<String> subspecies = myController.getSubspecies();
            Vector<String> subspeciesColumnNames = new Vector<>();
            subspeciesColumnNames.add("Subspecies");
            subspeciesColumnNames.add("Parent Species");

            Vector<Vector<Object>> subspeciesData = new Vector<>();

            // Get all species to find parent species for each subspecies
            List<Species> allSpecies = myController.getAllSpecies();

            for (String subspecies_name : subspecies) {
                // Find which species this subspecies belongs to
                String parentSpecies = "Unknown";
                for (Species species : allSpecies) {
                    List<String> speciesSubspecies = myController.getSubspeciesBySpecies(species.getSpeciesId());
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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            mySubspeciesTable.setModel(subspeciesModel);
            mySubspeciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subspecies: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows subspecies for the currently selected species.
     * Filters the subspecies table to show only subspecies of the selected species.
     */
    private void showSubspeciesForSelectedSpecies() {
        int selectedRow = mySpeciesTable.getSelectedRow();
        if (selectedRow == -1) {
            clearSubspeciesTable();
            return;
        }

        try {
            String selectedSpeciesName = (String) mySpeciesTable.getValueAt(selectedRow, 0);
            List<String> subspecies = myController.getSubspeciesBySpecies(selectedSpeciesName);

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
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            mySubspeciesTable.setModel(subspeciesModel);
            mySubspeciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Update border title to show filter
            ((JScrollPane)mySubspeciesTable.getParent().getParent()).setBorder(
                    BorderFactory.createTitledBorder("Subspecies for " + selectedSpeciesName)
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subspecies for selected species: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the subclass table and resets its column headers.
     * Resets the border title to the default "Subclasses".
     */
    private void clearSubclassTable() {
        DefaultTableModel emptyModel = new DefaultTableModel();
        emptyModel.addColumn("Subclass");
        emptyModel.addColumn("Parent Class");
        mySubclassTable.setModel(emptyModel);

        // Reset border title
        ((JScrollPane)mySubclassTable.getParent().getParent()).setBorder(
                BorderFactory.createTitledBorder("Subclasses")
        );
    }

    /**
     * Clears the subspecies table and resets its column headers.
     * Resets the border title to the default "Subspecies".
     */
    private void clearSubspeciesTable() {
        DefaultTableModel emptyModel = new DefaultTableModel();
        emptyModel.addColumn("Subspecies");
        emptyModel.addColumn("Parent Species");
        mySubspeciesTable.setModel(emptyModel);

        // Reset border title
        ((JScrollPane)mySubspeciesTable.getParent().getParent()).setBorder(
                BorderFactory.createTitledBorder("Subspecies")
        );
    }

    /**
     * Handles double-click events on the class table.
     * Shows full class description in a popup dialog when description column is double-clicked.
     *
     * @param theEvent the mouse event containing click information
     */
    private void handleClassTableDoubleClick(java.awt.event.MouseEvent theEvent) {
        int row = myClassTable.rowAtPoint(theEvent.getPoint());
        int col = myClassTable.columnAtPoint(theEvent.getPoint());

        if (row >= 0 && col >= 0) {
            String columnName = myClassTable.getColumnName(col);

            // Check if it's the Description column
            if (columnName.equals("Description")) {
                try {
                    String className = (String) myClassTable.getValueAt(row, 0);

                    // Get the full description from the controller
                    List<DnDClass> classes = myController.getAllClasses();
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

    /**
     * Handles double-click events on the species table.
     * Shows full species description in a popup dialog when description column is double-clicked.
     *
     * @param theEvent the mouse event containing click information
     */
    private void handleSpeciesTableDoubleClick(java.awt.event.MouseEvent theEvent) {
        int row = mySpeciesTable.rowAtPoint(theEvent.getPoint());
        int col = mySpeciesTable.columnAtPoint(theEvent.getPoint());

        if (row >= 0 && col >= 0) {
            String columnName = mySpeciesTable.getColumnName(col);

            // Check if it's the Description column
            if (columnName.equals("Description")) {
                try {
                    String speciesName = (String) mySpeciesTable.getValueAt(row, 0);

                    // Get the full description from the controller
                    List<Species> speciesList = myController.getAllSpecies();
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

    /**
     * Shows a text popup dialog with scrollable content.
     * Creates a formatted dialog with word wrapping and scroll bars for long text.
     *
     * @param theTitle the dialog title
     * @param theText the text content to display
     */
    private void showTextPopup(String theTitle, String theText) {
        JTextArea textArea = new JTextArea(theText);
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

        JOptionPane.showMessageDialog(this, scrollPane, theTitle, JOptionPane.INFORMATION_MESSAGE);
    }
}