import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.List;

class ReportView extends JPanel {
    private DnDController controller;
    private DnDMainView mainView;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel reportTitleLabel;

    public ReportView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
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

        // Report buttons panel - adjusted for 10 reports in 2 columns
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Available Reports"));

        // Create report buttons - only the ones from provided SQL queries
        JButton[] reportButtons = {
                new JButton("Characters by Class & Campaign"),
                new JButton("Classes with Most Subclasses"),
                new JButton("Above Average Level by Species"),
                new JButton("All Players and Characters"),
                new JButton("Popular Settings & Military"),
                new JButton("Character Species & Size"),
                new JButton("Player Character Counts"),
                new JButton("Campaign Participation"),
                new JButton("Class Distribution"),
                new JButton("Character Ability Modifiers")
        };

        // Add action listeners - only for the 10 SQL queries provided
        reportButtons[0].addActionListener(e -> runCharactersByClassReport());           // Query 1
        reportButtons[1].addActionListener(e -> runClassesWithMostSubclassesReport());   // Query 2
        reportButtons[2].addActionListener(e -> runAboveAverageLevelReport());           // Query 3
        reportButtons[3].addActionListener(e -> runAllPlayersAndCharactersReport());     // Query 4
        reportButtons[4].addActionListener(e -> runPopularSettingsAndMilitaryReport()); // Query 5
        reportButtons[5].addActionListener(e -> runCharacterSpeciesAndSizeReport());    // Query 6
        reportButtons[6].addActionListener(e -> runPlayerCharacterCountsReport());      // Query 7
        reportButtons[7].addActionListener(e -> runCampaignParticipationReport());      // Query 8
        reportButtons[8].addActionListener(e -> runClassDistributionReport());          // Query 9
        reportButtons[9].addActionListener(e -> runAbilityModifiersReport());           // Query 10

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
        mainView.showInfoMessage("Reports cleared - select a report to run");
    }

    // Only the 10 reports from the provided SQL queries

    private void runCharactersByClassReport() {
        try {
            mainView.showInfoMessage("Generating Characters by Class & Campaign report...");
            List<Map<String, Object>> results = controller.getCharactersByClassAndCampaign();
            displayResults(results, "Characters by Class & Campaign");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Characters by Class report: " + e.getMessage());
        }
    }

    private void runClassesWithMostSubclassesReport() {
        try {
            mainView.showInfoMessage("Generating Classes with Most Subclasses report...");
            List<Map<String, Object>> results = controller.getClassesWithMostSubclasses();
            displayResults(results, "Classes with Most Subclasses");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Classes with Most Subclasses report: " + e.getMessage());
        }
    }

    private void runAboveAverageLevelReport() {
        try {
            mainView.showInfoMessage("Generating Above Average Level by Species report...");
            List<Map<String, Object>> results = controller.getAboveAverageLevelBySpecies();
            displayResults(results, "Characters Above Average Level by Species");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Above Average Level report: " + e.getMessage());
        }
    }

    private void runAllPlayersAndCharactersReport() {
        try {
            mainView.showInfoMessage("Generating All Players and Characters report...");
            List<Map<String, Object>> results = controller.getAllPlayersAndCharacters();
            displayResults(results, "All Players and Characters");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate All Players and Characters report: " + e.getMessage());
        }
    }

    private void runPopularSettingsAndMilitaryReport() {
        try {
            mainView.showInfoMessage("Generating Popular Settings & Military Background report...");
            List<Map<String, Object>> results = controller.getPopularSettingsAndMilitary();
            displayResults(results, "Popular Settings & Military Background");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Popular Settings & Military report: " + e.getMessage());
        }
    }

    private void runCharacterSpeciesAndSizeReport() {
        try {
            mainView.showInfoMessage("Generating Character Species & Size report...");
            List<Map<String, Object>> results = controller.getCharacterSpeciesAndSize();
            displayResults(results, "Character Species & Size");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Character Species & Size report: " + e.getMessage());
        }
    }

    private void runPlayerCharacterCountsReport() {
        try {
            mainView.showInfoMessage("Generating Player Character Counts report...");
            List<Map<String, Object>> results = controller.getPlayerCharacterCounts();
            displayResults(results, "Player Character Counts");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Player Character Counts report: " + e.getMessage());
        }
    }

    private void runCampaignParticipationReport() {
        try {
            mainView.showInfoMessage("Generating Campaign Participation report...");
            List<Map<String, Object>> results = controller.getCampaignParticipation();
            displayResults(results, "Campaign Participation Statistics");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Campaign Participation report: " + e.getMessage());
        }
    }

    private void runClassDistributionReport() {
        try {
            mainView.showInfoMessage("Generating Class Distribution report...");
            List<Map<String, Object>> results = controller.getClassDistribution();
            displayResults(results, "Class Distribution Analysis");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Class Distribution report: " + e.getMessage());
        }
    }

    private void runAbilityModifiersReport() {
        try {
            mainView.showInfoMessage("Generating Character Ability Modifiers report...");
            List<Map<String, Object>> results = controller.getCharacterAbilityModifiers();
            displayResults(results, "Character Ability Modifiers");
        } catch (Exception e) {
            mainView.showErrorMessage("Failed to generate Ability Modifiers report: " + e.getMessage());
        }
    }

    private void displayResults(List<Map<String, Object>> results, String title) {
        try {
            reportTitleLabel.setText(title);

            if (results.isEmpty()) {
                tableModel = new DefaultTableModel();
                resultsTable.setModel(tableModel);
                mainView.showWarningMessage("No results found for " + title);
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

            mainView.showSuccessMessage(title + " generated successfully - " + results.size() + " results found");

        } catch (Exception e) {
            mainView.showErrorMessage("Error displaying report results: " + e.getMessage());
        }
    }
}