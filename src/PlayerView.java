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
public class PlayerView extends JPanel {
    private DnDController controller;
    private DnDMainView mainView;
    private JTable playerTable;
    private DefaultTableModel tableModel;

    public PlayerView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        playerTable = new JTable();
        playerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Players"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add Player");
        JButton viewCharactersBtn = new JButton("View Player's Characters");

        refreshBtn.addActionListener(e -> refreshData());
        addBtn.addActionListener(e -> showAddPlayerDialog());
        viewCharactersBtn.addActionListener(e -> viewPlayerCharacters());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(viewCharactersBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        try {
            List<Player> players = controller.getAllPlayers();

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

            tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            playerTable.setModel(tableModel);

            mainView.showSuccessMessage("Loaded " + players.size() + " players");

        } catch (Exception e) {
            mainView.showErrorMessage("Failed to load players: " + e.getMessage());
        }
    }

    private void showAddPlayerDialog() {
        try {
            PlayerCreationDialog dialog = new PlayerCreationDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this), controller, mainView);
            dialog.setVisible(true);

            if (dialog.wasPlayerCreated()) {
                refreshData();
                mainView.showSuccessMessage("New player added successfully");
            } else {
                mainView.showInfoMessage("Player creation cancelled");
            }
        } catch (Exception e) {
            mainView.showErrorMessage("Error opening player creation dialog: " + e.getMessage());
        }
    }

    private void viewPlayerCharacters() {
        int selectedRow = playerTable.getSelectedRow();
        if (selectedRow == -1) {
            mainView.showWarningMessage("Please select a player to view their characters");
            return;
        }

        try {
            int playerId = (Integer) playerTable.getValueAt(selectedRow, 0);
            String playerName = playerTable.getValueAt(selectedRow, 1) + " " +
                    (playerTable.getValueAt(selectedRow, 2) != null ? playerTable.getValueAt(selectedRow, 2) : "");

            // Get characters for this player
            List<Character> playerCharacters = controller.getCharactersByPlayer(playerId);

            if (playerCharacters.isEmpty()) {
                mainView.showInfoMessage(playerName + " has no characters yet");
                return;
            }

            // Create a simple display of characters
            StringBuilder characterList = new StringBuilder();
            characterList.append("Characters for ").append(playerName).append(":\n\n");

            for (Character character : playerCharacters) {
                characterList.append("• ").append(character.getCharId())
                        .append(" (Level ").append(character.getLevel())
                        .append(" ").append(character.getFullClass())
                        .append(" ").append(character.getFullSpecies())
                        .append(")\n");
            }

            JTextArea textArea = new JTextArea(characterList.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Characters for " + playerName, JOptionPane.INFORMATION_MESSAGE);

            mainView.showInfoMessage("Viewing " + playerCharacters.size() + " characters for " + playerName);

        } catch (Exception e) {
            mainView.showErrorMessage("Error loading player characters: " + e.getMessage());
        }
    }
}