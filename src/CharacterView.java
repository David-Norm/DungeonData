import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class CharacterView extends JPanel {
    private DnDController controller;
    private DnDMainView mainView;
    private JTable characterTable;
    private DefaultTableModel tableModel;

    public CharacterView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        characterTable = new JTable();
        characterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Table with scroll pane
        JScrollPane scrollPane = new JScrollPane(characterTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Characters"));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        JButton editBtn = new JButton("Edit Character");
        JButton deleteBtn = new JButton("Delete Character");
        JButton viewDetailsBtn = new JButton("View Details");

        refreshBtn.addActionListener(e -> refreshData());
        editBtn.addActionListener(e -> editSelectedCharacter()); // UPDATED: Now functional
        deleteBtn.addActionListener(e -> deleteSelectedCharacter());
        viewDetailsBtn.addActionListener(e -> viewCharacterDetails());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        try {
            List<Map<String, Object>> characters = controller.getCharactersWithDetails();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Character Name");
            columnNames.add("Level");
            columnNames.add("Class");
            columnNames.add("Subclass");
            columnNames.add("Species");
            columnNames.add("Subspecies");
            columnNames.add("Background");
            columnNames.add("Player");
            columnNames.add("Campaign");
            columnNames.add("STR");
            columnNames.add("DEX");
            columnNames.add("CON");
            columnNames.add("INT");
            columnNames.add("WIS");
            columnNames.add("CHA");

            Vector<Vector<Object>> data = new Vector<>();
            for (Map<String, Object> character : characters) {
                Vector<Object> row = new Vector<>();
                row.add(character.get("char_id"));
                row.add(character.get("lvl"));
                row.add(character.get("class_id"));
                row.add(character.get("subclass_id"));
                row.add(character.get("species_id"));
                row.add(character.get("subspecies_id"));
                row.add(character.get("bg_id"));

                String playerName = "";
                if (character.get("fname") != null) {
                    playerName = character.get("fname").toString();
                    if (character.get("lname") != null) {
                        playerName += " " + character.get("lname").toString();
                    }
                }
                row.add(playerName);
                row.add(character.get("game_id"));
                row.add(character.get("s_str"));
                row.add(character.get("s_dex"));
                row.add(character.get("s_con"));
                row.add(character.get("s_int"));
                row.add(character.get("s_wis"));
                row.add(character.get("s_cha"));

                data.add(row);
            }

            tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table read-only
                }
            };
            characterTable.setModel(tableModel);

            // Auto-resize columns
            characterTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            mainView.showSuccessMessage("Loaded " + characters.size() + " characters");

        } catch (Exception e) {
            mainView.showErrorMessage("Failed to load characters: " + e.getMessage());
        }
    }

    // UPDATED: Now fully implements character editing functionality
    private void editSelectedCharacter() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            mainView.showWarningMessage("Please select a character to edit");
            return;
        }

        try {
            // Extract character data from the selected row
            String charName = (String) characterTable.getValueAt(selectedRow, 0);
            Integer level = (Integer) characterTable.getValueAt(selectedRow, 1);
            String className = (String) characterTable.getValueAt(selectedRow, 2);
            String subclassName = (String) characterTable.getValueAt(selectedRow, 3);
            String speciesName = (String) characterTable.getValueAt(selectedRow, 4);
            String subspeciesName = (String) characterTable.getValueAt(selectedRow, 5);
            String backgroundName = (String) characterTable.getValueAt(selectedRow, 6);
            String playerName = (String) characterTable.getValueAt(selectedRow, 7);
            String campaignName = (String) characterTable.getValueAt(selectedRow, 8);
            Integer str = (Integer) characterTable.getValueAt(selectedRow, 9);
            Integer dex = (Integer) characterTable.getValueAt(selectedRow, 10);
            Integer con = (Integer) characterTable.getValueAt(selectedRow, 11);
            Integer intel = (Integer) characterTable.getValueAt(selectedRow, 12);
            Integer wis = (Integer) characterTable.getValueAt(selectedRow, 13);
            Integer cha = (Integer) characterTable.getValueAt(selectedRow, 14);

            // Find the player ID from the player name
            int playerId = 0;
            List<Player> players = controller.getAllPlayers();
            for (Player player : players) {
                if (player.getFullName().equals(playerName)) {
                    playerId = player.getPlayerId();
                    break;
                }
            }

            // Create a Character object with the current data
            Character characterToEdit = new Character(
                    charName, level, className, subclassName, speciesName, subspeciesName,
                    backgroundName, playerId, campaignName, str, dex, con, intel, wis, cha
            );

            // Switch to the character editor tab and load this character
            mainView.switchToCharacterEditor(characterToEdit);

        } catch (Exception e) {
            mainView.showErrorMessage("Error loading character for editing: " + e.getMessage());
        }
    }

    private void deleteSelectedCharacter() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            mainView.showWarningMessage("Please select a character to delete");
            return;
        }

        String charName = (String) characterTable.getValueAt(selectedRow, 0);

        // Still use a confirmation dialog for destructive actions
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete character: " + charName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.deleteCharacter(charName)) {
                    mainView.showSuccessMessage("Character '" + charName + "' deleted successfully");
                    refreshData();
                } else {
                    mainView.showErrorMessage("Failed to delete character '" + charName + "'");
                }
            } catch (Exception e) {
                mainView.showErrorMessage("Error deleting character: " + e.getMessage());
            }
        } else {
            mainView.showInfoMessage("Delete operation cancelled");
        }
    }

    private void viewCharacterDetails() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            mainView.showWarningMessage("Please select a character to view details");
            return;
        }

        // Create detailed view dialog
        StringBuilder details = new StringBuilder();
        details.append("CHARACTER DETAILS\n");
        details.append("================\n\n");

        for (int i = 0; i < characterTable.getColumnCount(); i++) {
            String columnName = characterTable.getColumnName(i);
            Object value = characterTable.getValueAt(selectedRow, i);
            details.append(columnName).append(": ").append(value).append("\n");
        }

        // Add computed information
        String charName = (String) characterTable.getValueAt(selectedRow, 0);
        String className = (String) characterTable.getValueAt(selectedRow, 2);
        String subclassName = (String) characterTable.getValueAt(selectedRow, 3);
        String speciesName = (String) characterTable.getValueAt(selectedRow, 4);
        String subspeciesName = (String) characterTable.getValueAt(selectedRow, 5);

        details.append("\n--- FORMATTED INFO ---\n");
        if (className != null && subclassName != null) {
            details.append("Full Class: ").append(className).append(" (").append(subclassName).append(")\n");
        }
        if (speciesName != null && subspeciesName != null) {
            details.append("Full Species: ").append(speciesName).append(" (").append(subspeciesName).append(")\n");
        }

        // Add ability modifiers
        details.append("\n--- ABILITY MODIFIERS ---\n");
        try {
            Integer str = (Integer) characterTable.getValueAt(selectedRow, 9);
            Integer dex = (Integer) characterTable.getValueAt(selectedRow, 10);
            Integer con = (Integer) characterTable.getValueAt(selectedRow, 11);
            Integer intel = (Integer) characterTable.getValueAt(selectedRow, 12);
            Integer wis = (Integer) characterTable.getValueAt(selectedRow, 13);
            Integer cha = (Integer) characterTable.getValueAt(selectedRow, 14);

            details.append("STR: ").append(str).append(" (").append(getModifierString(str)).append(")\n");
            details.append("DEX: ").append(dex).append(" (").append(getModifierString(dex)).append(")\n");
            details.append("CON: ").append(con).append(" (").append(getModifierString(con)).append(")\n");
            details.append("INT: ").append(intel).append(" (").append(getModifierString(intel)).append(")\n");
            details.append("WIS: ").append(wis).append(" (").append(getModifierString(wis)).append(")\n");
            details.append("CHA: ").append(cha).append(" (").append(getModifierString(cha)).append(")\n");
        } catch (Exception e) {
            details.append("Error calculating ability modifiers\n");
        }

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Character Details: " + charName, JOptionPane.INFORMATION_MESSAGE);

        mainView.showInfoMessage("Viewing details for character '" + charName + "'");
    }

    // Helper method to format ability score modifiers
    private String getModifierString(Integer score) {
        if (score == null) return "+0";
        int modifier = (score - 10) / 2;
        return modifier >= 0 ? "+" + modifier : String.valueOf(modifier);
    }
}