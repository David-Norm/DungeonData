import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.List;

class ReportView extends JPanel {
    private DnDController controller;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel reportTitleLabel;

    public ReportView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        resultsTable = new JTable();
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportTitleLabel = new JLabel("Select a report to run", JLabel.CENTER);
        reportTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Report buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Available Reports"));

        // Create report buttons
        JButton[] reportButtons = {
                new JButton("Characters by Class & Campaign"),
                new JButton("Above Average Level by Species"),
                new JButton("Player Character Counts"),
                new JButton("Campaign Participation"),
                new JButton("Character Ability Modifiers"),
                new JButton("Class Distribution"),
                new JButton("Species Size Analysis"),
                new JButton("Background Popularity"),
                new JButton("Level Distribution"),
                new JButton("Campaign Settings Report")
        };

        // Add action listeners
        reportButtons[0].addActionListener(e -> runCharactersByClassReport());
        reportButtons[1].addActionListener(e -> runAboveAverageLevelReport());
        reportButtons[2].addActionListener(e -> runPlayerCharacterCountsReport());
        reportButtons[3].addActionListener(e -> runCampaignParticipationReport());
        reportButtons[4].addActionListener(e -> runAbilityModifiersReport());
        reportButtons[5].addActionListener(e -> runClassDistributionReport());
        reportButtons[6].addActionListener(e -> runSpeciesSizeReport());
        reportButtons[7].addActionListener(e -> runBackgroundPopularityReport());
        reportButtons[8].addActionListener(e -> runLevelDistributionReport());
        reportButtons[9].addActionListener(e -> runCampaignSettingsReport());

        for (JButton button : reportButtons) {
            buttonPanel.add(button);
        }

        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(reportTitleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Results"));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Layout
        add(buttonPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Clear current results
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }
        reportTitleLabel.setText("Select a report to run");
    }

    private void runCharactersByClassReport() {
        List<Map<String, Object>> results = controller.getCharactersByClassAndCampaign();
        displayResults(results, "Characters by Class & Campaign");
    }

    private void runAboveAverageLevelReport() {
        List<Map<String, Object>> results = controller.getAboveAverageLevelBySpecies();
        displayResults(results, "Characters Above Average Level by Species");
    }

    private void runPlayerCharacterCountsReport() {
        List<Map<String, Object>> results = controller.getPlayerCharacterCounts();
        displayResults(results, "Player Character Counts");
    }

    private void runCampaignParticipationReport() {
        List<Map<String, Object>> results = controller.getCampaignParticipation();
        displayResults(results, "Campaign Participation Statistics");
    }

    private void runAbilityModifiersReport() {
        List<Map<String, Object>> results = controller.getCharacterAbilityModifiers();
        displayResults(results, "Character Ability Modifiers");
    }

    // Placeholder methods for additional reports
    private void runClassDistributionReport() {
        JOptionPane.showMessageDialog(this, "Class Distribution report would be implemented here.");
    }

    private void runSpeciesSizeReport() {
        JOptionPane.showMessageDialog(this, "Species Size Analysis report would be implemented here.");
    }

    private void runBackgroundPopularityReport() {
        JOptionPane.showMessageDialog(this, "Background Popularity report would be implemented here.");
    }

    private void runLevelDistributionReport() {
        JOptionPane.showMessageDialog(this, "Level Distribution report would be implemented here.");
    }

    private void runCampaignSettingsReport() {
        JOptionPane.showMessageDialog(this, "Campaign Settings report would be implemented here.");
    }

    private void displayResults(List<Map<String, Object>> results, String title) {
        reportTitleLabel.setText(title);

        if (results.isEmpty()) {
            tableModel = new DefaultTableModel();
            resultsTable.setModel(tableModel);
            JOptionPane.showMessageDialog(this, "No results found for this report.");
            return;
        }

        // Get column names from first result
        Map<String, Object> firstResult = results.get(0);
        Vector<String> columnNames = new Vector<>(firstResult.keySet());

        // Prepare data
        Vector<Vector<Object>> data = new Vector<>();
        for (Map<String, Object> result : results) {
            Vector<Object> row = new Vector<>();
            for (String columnName : columnNames) {
                row.add(result.get(columnName));
            }
            data.add(row);
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable.setModel(tableModel);

        // Auto-resize columns
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
}
