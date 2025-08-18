import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// DnDMainView.java - Main window coordinating all view panels
public class DnDMainView extends JFrame {
    private DnDController controller;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;
    private Timer statusTimer;

    // View panels
    private CharacterView characterView;
    private PlayerView playerView;
    private CampaignView campaignView;
    private ClassSpeciesView classSpeciesView;
    private CharacterCreatorView characterCreatorView;
    private ReportView reportView;

    public DnDMainView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupStatusTimer();
    }

    private void initializeComponents() {
        setTitle("D&D Character Database Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize view panels
        characterView = new CharacterView(controller, this);
        playerView = new PlayerView(controller, this);
        campaignView = new CampaignView(controller, this);
        classSpeciesView = new ClassSpeciesView(controller);
        characterCreatorView = new CharacterCreatorView(controller, this);
        reportView = new ReportView(controller, this);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Add tabs
        tabbedPane.addTab("Characters", characterView);
        tabbedPane.addTab("Players", playerView);
        tabbedPane.addTab("Campaigns", campaignView);
        tabbedPane.addTab("Classes & Species", classSpeciesView);
        tabbedPane.addTab("Character Creator", characterCreatorView);
        tabbedPane.addTab("Reports", reportView);

        add(tabbedPane, BorderLayout.CENTER);

        // Add status bar with enhanced styling
        statusBar = new JLabel("Ready - Welcome to D&D Character Database Manager");
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusBar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(240, 240, 240));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem refreshCurrentItem = new JMenuItem("Refresh Current Tab");
        refreshCurrentItem.addActionListener(e -> refreshCurrentTab());
        fileMenu.add(refreshCurrentItem);

        JMenuItem refreshAllItem = new JMenuItem("Refresh All Data");
        refreshAllItem.addActionListener(e -> refreshAllTabs());
        fileMenu.add(refreshAllItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> controller.shutdown());
        fileMenu.add(exitItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupStatusTimer() {
        // Timer to clear status messages after 5 seconds
        statusTimer = new Timer(5000, e -> {
            if (!statusBar.getText().equals("Ready")) {
                setStatusMessage("Ready", MessageType.INFO);
            }
        });
        statusTimer.setRepeats(false);
    }

    private void refreshCurrentTab() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String tabName = tabbedPane.getTitleAt(selectedIndex);

        switch (selectedIndex) {
            case 0 -> {
                characterView.refreshData();
                setStatusMessage("Characters data refreshed", MessageType.SUCCESS);
            }
            case 1 -> {
                playerView.refreshData();
                setStatusMessage("Players data refreshed", MessageType.SUCCESS);
            }
            case 2 -> {
                campaignView.refreshData();
                setStatusMessage("Campaigns data refreshed", MessageType.SUCCESS);
            }
            case 3 -> {
                classSpeciesView.refreshData();
                setStatusMessage("Classes & Species data refreshed", MessageType.SUCCESS);
            }
            case 4 -> {
                characterCreatorView.refreshData();
                setStatusMessage("Character Creator data refreshed", MessageType.SUCCESS);
            }
            case 5 -> {
                reportView.refreshData();
                setStatusMessage("Reports cleared", MessageType.SUCCESS);
            }
            default -> setStatusMessage("Tab refreshed", MessageType.SUCCESS);
        }
    }

    private void refreshAllTabs() {
        characterView.refreshData();
        playerView.refreshData();
        campaignView.refreshData();
        classSpeciesView.refreshData();
        characterCreatorView.refreshData();
        reportView.refreshData();
        setStatusMessage("All data refreshed successfully", MessageType.SUCCESS);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                """
                D&D Character Database Manager v1.0
                
                A comprehensive tool for managing D&D campaigns,
                characters, and players.
                
                Features:
                • Character creation and management
                • Player database with contact information
                • Campaign tracking and organization
                • Advanced reporting and analytics
                • D&D 5e class and species reference
                
                Built with Java Swing and MySQL
                """,
                "About D&D Database Manager",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Public methods for other views to use
    public void switchToCharactersTab() {
        tabbedPane.setSelectedIndex(0);
        characterView.refreshData();
        setStatusMessage("Switched to Characters tab", MessageType.INFO);
    }

    public void switchToPlayersTab() {
        tabbedPane.setSelectedIndex(1);
        playerView.refreshData();
        setStatusMessage("Switched to Players tab", MessageType.INFO);
    }

    public DnDController getController() {
        return controller;
    }

    // Enhanced status bar messaging system
    public enum MessageType {
        INFO(new Color(240, 240, 240), Color.BLACK),
        SUCCESS(new Color(220, 255, 220), new Color(0, 120, 0)),
        WARNING(new Color(255, 248, 220), new Color(180, 120, 0)),
        ERROR(new Color(255, 220, 220), new Color(180, 0, 0));

        private final Color backgroundColor;
        private final Color textColor;

        MessageType(Color backgroundColor, Color textColor) {
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
        }

        public Color getBackgroundColor() { return backgroundColor; }
        public Color getTextColor() { return textColor; }
    }

    public void setStatusMessage(String message, MessageType type) {
        SwingUtilities.invokeLater(() -> {
            statusBar.setText(message);
            statusBar.setBackground(type.getBackgroundColor());
            statusBar.setForeground(type.getTextColor());

            // Restart the timer to clear the message after 5 seconds
            statusTimer.restart();
        });
    }

    // Convenience methods for different message types
    public void showInfoMessage(String message) {
        setStatusMessage(message, MessageType.INFO);
    }

    public void showSuccessMessage(String message) {
        setStatusMessage(message, MessageType.SUCCESS);
    }

    public void showWarningMessage(String message) {
        setStatusMessage(message, MessageType.WARNING);
    }

    public void showErrorMessage(String message) {
        setStatusMessage(message, MessageType.ERROR);
    }
}