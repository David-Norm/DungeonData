import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Player management view for displaying and managing player data.
 * Provides functionality to view players, add new players, delete players, and view their characters.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class PlayerView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;
    private JTable myPlayerTable;
    private DefaultTableModel myTableModel;

    /**
     * Constructs a PlayerView with the specified controller and main view.
     *
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public PlayerView(DnDController theController, DnDMainView theMainView) {
        myController = theController;
        myMainView = theMainView;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        myPlayerTable = new JTable();
        myPlayerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Sets up the panel layout and components.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(myPlayerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Players"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add Player");
        JButton deleteBtn = new JButton("Delete Player");
        JButton viewCharactersBtn = new JButton("View Player's Characters");

        refreshBtn.addActionListener(e -> refreshData());
        addBtn.addActionListener(e -> showAddPlayerDialog());
        deleteBtn.addActionListener(e -> deleteSelectedPlayer());
        viewCharactersBtn.addActionListener(e -> viewPlayerCharacters());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewCharactersBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the player data from the database.
     */
    public void refreshData() {
        try {
            List<Player> players = myController.getAllPlayers();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Player ID");
            columnNames.add("First Name");
            columnNames.add("Last Name");
            columnNames.add("Preferred Contact");
            columnNames.add("Contact Info");
            columnNames.add("Time Zone");

            Vector<Vector<Object>> data = new Vector<>();
            for (Player player : players) {
                Vector<Object> row = new Vector<>();
                row.add(player.getPlayerId());
                row.add(player.getFirstName());
                row.add(player.getLastName());
                row.add(player.getPreferredContact());
                row.add(player.getContactInfo());
                row.add(player.getTimeZone());
                data.add(row);
            }

            myTableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            myPlayerTable.setModel(myTableModel);

            myMainView.showSuccessMessage("Loaded " + players.size() + " players");

        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to load players: " + e.getMessage());
        }
    }

    /**
     * Shows the dialog for adding a new player.
     */
    private void showAddPlayerDialog() {
        try {
            PlayerCreationDialog dialog = new PlayerCreationDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this), myController, myMainView);
            dialog.setVisible(true);

            if (dialog.wasPlayerCreated()) {
                refreshData();
                myMainView.showSuccessMessage("New player added successfully");
            } else {
                myMainView.showInfoMessage("Player creation cancelled");
            }
        } catch (Exception e) {
            myMainView.showErrorMessage("Error opening player creation dialog: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected player after confirmation and validation.
     */
    private void deleteSelectedPlayer() {
        int selectedRow = myPlayerTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a player to delete");
            return;
        }

        try {
            int playerId = (Integer) myPlayerTable.getValueAt(selectedRow, 0);
            String firstName = (String) myPlayerTable.getValueAt(selectedRow, 1);
            String lastName = (String) myPlayerTable.getValueAt(selectedRow, 2);
            String playerName = firstName + (lastName != null ? " " + lastName : "");

            // Check if player has characters
            if (myController.playerHasCharacters(playerId)) {
                int characterCount = myController.getPlayerCharacterCount(playerId);

                // Show warning about characters
                int choice = JOptionPane.showConfirmDialog(this,
                        "Player '" + playerName + "' has " + characterCount + " character(s).\n" +
                                "You cannot delete a player who has characters.\n\n" +
                                "Would you like to view the player's characters?",
                        "Cannot Delete Player",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    viewPlayerCharacters();
                }
                return;
            }

            // Confirm deletion for players with no characters
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete player: " + playerName + "?\n" +
                            "This action cannot be undone.",
                    "Confirm Delete Player",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (myController.deletePlayer(playerId)) {
                    myMainView.showSuccessMessage("Player '" + playerName + "' deleted successfully");
                    refreshData();
                } else {
                    myMainView.showErrorMessage("Failed to delete player '" + playerName + "'");
                }
            } else {
                myMainView.showInfoMessage("Player deletion cancelled");
            }

        } catch (Exception e) {
            myMainView.showErrorMessage("Error deleting player: " + e.getMessage());
        }
    }

    /**
     * Displays characters belonging to the selected player.
     */
    private void viewPlayerCharacters() {
        int selectedRow = myPlayerTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a player to view their characters");
            return;
        }

        try {
            int playerId = (Integer) myPlayerTable.getValueAt(selectedRow, 0);
            String playerName = myPlayerTable.getValueAt(selectedRow, 1) + " " +
                    (myPlayerTable.getValueAt(selectedRow, 2) != null ? myPlayerTable.getValueAt(selectedRow, 2) : "");

            List<Character> playerCharacters = myController.getCharactersByPlayer(playerId);

            if (playerCharacters.isEmpty()) {
                myMainView.showInfoMessage(playerName + " has no characters yet");
                return;
            }

            StringBuilder characterList = new StringBuilder();
            characterList.append("Characters for ").append(playerName).append(":\n\n");

            for (Character character : playerCharacters) {
                characterList.append("• ").append(character.getCharId())
                        .append(" (Level ").append(character.getLevel())
                        .append(" ").append(character.getFullClass())
                        .append(" ").append(character.getFullSpecies())
                        .append(")\n");
                characterList.append("  Campaign: ").append(character.getGameId()).append("\n");
                characterList.append("  Background: ").append(character.getBackgroundId()).append("\n\n");
            }

            characterList.append("Note: To delete this player, you must first delete or reassign all their characters.");

            JTextArea textArea = new JTextArea(characterList.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Characters for " + playerName + " (" + playerCharacters.size() + " characters)",
                    JOptionPane.INFORMATION_MESSAGE);

            myMainView.showInfoMessage("Viewing " + playerCharacters.size() + " characters for " + playerName);

        } catch (Exception e) {
            myMainView.showErrorMessage("Error loading player characters: " + e.getMessage());
        }
    }
}