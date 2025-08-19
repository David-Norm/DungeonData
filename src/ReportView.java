/**
 * Report generation view for displaying various database reports.
 * Provides buttons to generate different types of analytical reports.
 *
 * @author David Norman
 * @version Summer 2025
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class ReportView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;
    private JTable myResultsTable;
    private DefaultTableModel myTableModel;
    private JLabel myReportTitleLabel;

    /**
     * Constructs a ReportView with the specified controller and main view.
     *
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public ReportView(DnDController theController, DnDMainView theMainView) {
        myController = theController;
        myMainView = theMainView;
        initializeComponents();
        setupLayout();
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        myResultsTable = new JTable();
        myResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        myReportTitleLabel = new JLabel("Select a report to run", JLabel.CENTER);
        myReportTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    }

    /**
     * Sets up the panel layout and components.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Available Reports"));

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

        reportButtons[0].addActionListener(e -> runCharactersByClassReport());
        reportButtons[1].addActionListener(e -> runClassesWithMostSubclassesReport());
        reportButtons[2].addActionListener(e -> runAboveAverageLevelReport());
        reportButtons[3].addActionListener(e -> runAllPlayersAndCharactersReport());
        reportButtons[4].addActionListener(e -> runPopularSettingsAndMilitaryReport());
        reportButtons[5].addActionListener(e -> runCharacterSpeciesAndSizeReport());
        reportButtons[6].addActionListener(e -> runPlayerCharacterCountsReport());
        reportButtons[7].addActionListener(e -> runCampaignParticipationReport());
        reportButtons[8].addActionListener(e -> runClassDistributionReport());
        reportButtons[9].addActionListener(e -> runAbilityModifiersReport());

        for (JButton button : reportButtons) {
            buttonPanel.add(button);
        }

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(myReportTitleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(myResultsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Results"));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
    }

    /**
     * Refreshes the report view by clearing current results.
     */
    public void refreshData() {
        if (myTableModel != null) {
            myTableModel.setRowCount(0);
        }
        myReportTitleLabel.setText("Select a report to run");
        myMainView.showInfoMessage("Reports cleared - select a report to run");
    }

    /**
     * Generates and displays the characters by class and campaign report.
     */
    private void runCharactersByClassReport() {
        try {
            myMainView.showInfoMessage("Generating Characters by Class & Campaign report...");
            List<Map<String, Object>> results = myController.getCharactersByClassAndCampaign();
            displayResults(results, "Characters by Class & Campaign");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Characters by Class report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the classes with most subclasses report.
     */
    private void runClassesWithMostSubclassesReport() {
        try {
            myMainView.showInfoMessage("Generating Classes with Most Subclasses report...");
            List<Map<String, Object>> results = myController.getClassesWithMostSubclasses();
            displayResults(results, "Classes with Most Subclasses");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Classes with Most Subclasses report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the above average level by species report.
     */
    private void runAboveAverageLevelReport() {
        try {
            myMainView.showInfoMessage("Generating Above Average Level by Species report...");
            List<Map<String, Object>> results = myController.getAboveAverageLevelBySpecies();
            displayResults(results, "Characters Above Average Level by Species");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Above Average Level report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the all players and characters report.
     */
    private void runAllPlayersAndCharactersReport() {
        try {
            myMainView.showInfoMessage("Generating All Players and Characters report...");
            List<Map<String, Object>> results = myController.getAllPlayersAndCharacters();
            displayResults(results, "All Players and Characters");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate All Players and Characters report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the popular settings and military report.
     */
    private void runPopularSettingsAndMilitaryReport() {
        try {
            myMainView.showInfoMessage("Generating Popular Settings & Military Background report...");
            List<Map<String, Object>> results = myController.getPopularSettingsAndMilitary();
            displayResults(results, "Popular Settings & Military Background");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Popular Settings & Military report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the character species and size report.
     */
    private void runCharacterSpeciesAndSizeReport() {
        try {
            myMainView.showInfoMessage("Generating Character Species & Size report...");
            List<Map<String, Object>> results = myController.getCharacterSpeciesAndSize();
            displayResults(results, "Character Species & Size");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Character Species & Size report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the player character counts report.
     */
    private void runPlayerCharacterCountsReport() {
        try {
            myMainView.showInfoMessage("Generating Player Character Counts report...");
            List<Map<String, Object>> results = myController.getPlayerCharacterCounts();
            displayResults(results, "Player Character Counts");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Player Character Counts report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the campaign participation report.
     */
    private void runCampaignParticipationReport() {
        try {
            myMainView.showInfoMessage("Generating Campaign Participation report...");
            List<Map<String, Object>> results = myController.getCampaignParticipation();
            displayResults(results, "Campaign Participation Statistics");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Campaign Participation report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the class distribution report.
     */
    private void runClassDistributionReport() {
        try {
            myMainView.showInfoMessage("Generating Class Distribution report...");
            List<Map<String, Object>> results = myController.getClassDistribution();
            displayResults(results, "Class Distribution Analysis");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Class Distribution report: " + e.getMessage());
        }
    }

    /**
     * Generates and displays the character ability modifiers report.
     */
    private void runAbilityModifiersReport() {
        try {
            myMainView.showInfoMessage("Generating Character Ability Modifiers report...");
            List<Map<String, Object>> results = myController.getCharacterAbilityModifiers();
            displayResults(results, "Character Ability Modifiers");
        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to generate Ability Modifiers report: " + e.getMessage());
        }
    }

    /**
     * Displays the report results in the table.
     *
     * @param theResults the data to display
     * @param theTitle the report title
     */
    private void displayResults(List<Map<String, Object>> theResults, String theTitle) {
        try {
            myReportTitleLabel.setText(theTitle);

            if (theResults.isEmpty()) {
                myTableModel = new DefaultTableModel();
                myResultsTable.setModel(myTableModel);
                myMainView.showWarningMessage("No results found for " + theTitle);
                return;
            }

            Map<String, Object> firstResult = theResults.get(0);
            Vector<String> columnNames = new Vector<>(firstResult.keySet());

            Vector<Vector<Object>> data = new Vector<>();
            for (Map<String, Object> result : theResults) {
                Vector<Object> row = new Vector<>();
                for (String columnName : columnNames) {
                    row.add(result.get(columnName));
                }
                data.add(row);
            }

            myTableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            myResultsTable.setModel(myTableModel);
            myResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            myMainView.showSuccessMessage(theTitle + " generated successfully - " + theResults.size() + " results found");

        } catch (Exception e) {
            myMainView.showErrorMessage("Error displaying report results: " + e.getMessage());
        }
    }
}
