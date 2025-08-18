import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

class ClassSpeciesView extends JPanel {
    private DnDController controller;
    private JTable classTable;
    private JTable speciesTable;

    public ClassSpeciesView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        classTable = new JTable();
        speciesTable = new JTable();
        classTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        speciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new GridLayout(2, 1, 5, 5));

        // Classes panel
        JPanel classPanel = new JPanel(new BorderLayout());
        JScrollPane classScrollPane = new JScrollPane(classTable);
        classScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Classes"));
        classPanel.add(classScrollPane, BorderLayout.CENTER);

        // Species panel
        JPanel speciesPanel = new JPanel(new BorderLayout());
        JScrollPane speciesScrollPane = new JScrollPane(speciesTable);
        speciesScrollPane.setBorder(BorderFactory.createTitledBorder("D&D Species"));
        speciesPanel.add(speciesScrollPane, BorderLayout.CENTER);

        add(classPanel);
        add(speciesPanel);
    }

    public void refreshData() {
        // Load classes
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
            row.add(dndClass.getCastingStat());
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

        // Load species
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

        // Auto-resize columns for both tables
        classTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        speciesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
}