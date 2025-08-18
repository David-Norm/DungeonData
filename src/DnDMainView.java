import javax.swing.*;
import java.awt.*;

// DnDMainView.java - Main window coordinating all view panels
public class DnDMainView extends JFrame {
    private DnDController controller;
    private JTabbedPane tabbedPane;

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
    }

    private void initializeComponents() {
        setTitle("D&D Character Database Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize view panels
        characterView = new CharacterView(controller);
        playerView = new PlayerView(controller);
        campaignView = new CampaignView(controller);
        classSpeciesView = new ClassSpeciesView(controller);
        characterCreatorView = new CharacterCreatorView(controller);
        reportView = new ReportView(controller);
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

        // Add status bar
        JLabel statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
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

    private void refreshCurrentTab() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        switch (selectedIndex) {
            case 0 -> characterView.refreshData();
            case 1 -> playerView.refreshData();
            case 2 -> campaignView.refreshData();
            case 3 -> classSpeciesView.refreshData();
            case 4 -> characterCreatorView.refreshData();
            case 5 -> reportView.refreshData();
            default -> JOptionPane.showMessageDialog(this, "Current tab refreshed!");
        }
    }

    private void refreshAllTabs() {
        characterView.refreshData();
        playerView.refreshData();
        campaignView.refreshData();
        classSpeciesView.refreshData();
        characterCreatorView.refreshData();
        reportView.refreshData();
        JOptionPane.showMessageDialog(this, "All data refreshed!");
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
    }

    public void switchToPlayersTab() {
        tabbedPane.setSelectedIndex(1);
        playerView.refreshData();
    }

    public DnDController getController() {
        return controller;
    }
}