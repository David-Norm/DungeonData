import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the D&D Character Database Manager.
 * Provides a tabbed interface for managing characters, players, campaigns, and reports.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class DnDMainView extends JFrame {
    private final DnDController myController;
    private JTabbedPane myTabbedPane;
    private JLabel myStatusBar;
    private Timer myStatusTimer;

    private CharacterView myCharacterView;
    private PlayerView myPlayerView;
    private CampaignView myCampaignView;
    private ClassSpeciesView myClassSpeciesView;
    private CharacterCreatorView myCharacterCreatorView;
    private CharacterEditView myCharacterEditView;
    private ReportView myReportView;

    /**
     * Constructs the main view with the specified controller.
     *
     * @param theController the main application controller
     */
    public DnDMainView(DnDController theController) {
        myController = theController;
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupStatusTimer();
    }

    /**
     * Initializes all GUI components.
     */
    private void initializeComponents() {
        setTitle("D&D Character Database Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        myTabbedPane = new JTabbedPane();

        myCharacterView = new CharacterView(myController, this);
        myPlayerView = new PlayerView(myController, this);
        myCampaignView = new CampaignView(myController, this);
        myClassSpeciesView = new ClassSpeciesView(myController);
        myCharacterCreatorView = new CharacterCreatorView(myController, this);
        myCharacterEditView = new CharacterEditView(myController, this);
        myReportView = new ReportView(myController, this);
    }

    /**
     * Sets up the main layout and adds all tabs.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        myTabbedPane.addTab("Characters", myCharacterView);
        myTabbedPane.addTab("Players", myPlayerView);
        myTabbedPane.addTab("Campaigns", myCampaignView);
        myTabbedPane.addTab("Classes & Species", myClassSpeciesView);
        myTabbedPane.addTab("Character Creator", myCharacterCreatorView);
        myTabbedPane.addTab("Character Editor", myCharacterEditView);
        myTabbedPane.addTab("Reports", myReportView);

        add(myTabbedPane, BorderLayout.CENTER);

        myStatusBar = new JLabel("Ready - Welcome to D&D Character Database Manager");
        myStatusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        myStatusBar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        myStatusBar.setOpaque(true);
        myStatusBar.setBackground(new Color(240, 240, 240));
        add(myStatusBar, BorderLayout.SOUTH);
    }

    /**
     * Sets up the application menu bar.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem refreshCurrentItem = new JMenuItem("Refresh Current Tab");
        refreshCurrentItem.addActionListener(e -> refreshCurrentTab());
        fileMenu.add(refreshCurrentItem);

        JMenuItem refreshAllItem = new JMenuItem("Refresh All Data");
        refreshAllItem.addActionListener(e -> refreshAllTabs());
        fileMenu.add(refreshAllItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> myController.shutdown());
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Sets up the status message timer.
     */
    private void setupStatusTimer() {
        myStatusTimer = new Timer(5000, e -> {
            if (!myStatusBar.getText().equals("Ready")) {
                setStatusMessage("Ready", MessageType.INFO);
            }
        });
        myStatusTimer.setRepeats(false);
    }

    /**
     * Refreshes data for the currently selected tab.
     */
    private void refreshCurrentTab() {
        int selectedIndex = myTabbedPane.getSelectedIndex();

        switch (selectedIndex) {
            case 0 -> {
                myCharacterView.refreshData();
                setStatusMessage("Characters data refreshed", MessageType.SUCCESS);
            }
            case 1 -> {
                myPlayerView.refreshData();
                setStatusMessage("Players data refreshed", MessageType.SUCCESS);
            }
            case 2 -> {
                myCampaignView.refreshData();
                setStatusMessage("Campaigns data refreshed", MessageType.SUCCESS);
            }
            case 3 -> {
                myClassSpeciesView.refreshData();
                setStatusMessage("Classes & Species data refreshed", MessageType.SUCCESS);
            }
            case 4 -> {
                myCharacterCreatorView.refreshData();
                setStatusMessage("Character Creator data refreshed", MessageType.SUCCESS);
            }
            case 5 -> {
                myCharacterEditView.refreshData();
                setStatusMessage("Character Editor data refreshed", MessageType.SUCCESS);
            }
            case 6 -> {
                myReportView.refreshData();
                setStatusMessage("Reports cleared", MessageType.SUCCESS);
            }
            default -> setStatusMessage("Tab refreshed", MessageType.SUCCESS);
        }
    }

    /**
     * Refreshes data for all tabs.
     */
    private void refreshAllTabs() {
        myCharacterView.refreshData();
        myPlayerView.refreshData();
        myCampaignView.refreshData();
        myClassSpeciesView.refreshData();
        myCharacterCreatorView.refreshData();
        myCharacterEditView.refreshData();
        myReportView.refreshData();
        setStatusMessage("All data refreshed successfully", MessageType.SUCCESS);
    }

    /**
     * Shows the application about dialog.
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                """
                D&D Character Database Manager
                By: David Norman
                     &
                     Georgia Karwhite
                """,
                "About D&D Database Manager",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Switches to the Characters tab and refreshes data.
     */
    public void switchToCharactersTab() {
        myTabbedPane.setSelectedIndex(0);
        myCharacterView.refreshData();
        setStatusMessage("Switched to Characters tab", MessageType.INFO);
    }

    /**
     * Switches to the character editor and loads a specific character.
     *
     * @param theCharacter the character to edit
     */
    public void switchToCharacterEditor(Character theCharacter) {
        myTabbedPane.setSelectedIndex(5);
        myCharacterEditView.editCharacter(theCharacter);
        setStatusMessage("Editing character: " + theCharacter.getCharId(), MessageType.INFO);
    }

    /**
     * Enumeration for different message types with associated colors.
     */
    public enum MessageType {
        INFO(new Color(240, 240, 240), Color.BLACK),
        SUCCESS(new Color(220, 255, 220), new Color(0, 120, 0)),
        WARNING(new Color(255, 248, 220), new Color(180, 120, 0)),
        ERROR(new Color(255, 220, 220), new Color(180, 0, 0));

        private final Color myBackgroundColor;
        private final Color myTextColor;

        MessageType(Color theBackgroundColor, Color theTextColor) {
            myBackgroundColor = theBackgroundColor;
            myTextColor = theTextColor;
        }

        public Color getBackgroundColor() { return myBackgroundColor; }
        public Color getTextColor() { return myTextColor; }
    }

    /**
     * Sets a status message with the specified type and color.
     *
     * @param theMessage the message to display
     * @param theType the message type for color coding
     */
    public void setStatusMessage(String theMessage, MessageType theType) {
        SwingUtilities.invokeLater(() -> {
            myStatusBar.setText(theMessage);
            myStatusBar.setBackground(theType.getBackgroundColor());
            myStatusBar.setForeground(theType.getTextColor());
            myStatusTimer.restart();
        });
    }

    /**
     * Shows an informational message.
     *
     * @param theMessage the message to display
     */
    public void showInfoMessage(String theMessage) {
        setStatusMessage(theMessage, MessageType.INFO);
    }

    /**
     * Shows a success message.
     *
     * @param theMessage the message to display
     */
    public void showSuccessMessage(String theMessage) {
        setStatusMessage(theMessage, MessageType.SUCCESS);
    }

    /**
     * Shows a warning message.
     *
     * @param theMessage the message to display
     */
    public void showWarningMessage(String theMessage) {
        setStatusMessage(theMessage, MessageType.WARNING);
    }

    /**
     * Shows an error message.
     *
     * @param theMessage the message to display
     */
    public void showErrorMessage(String theMessage) {
        setStatusMessage(theMessage, MessageType.ERROR);
    }
}