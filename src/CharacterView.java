import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Character management view for displaying and managing character data.
 * Provides functionality to view, edit, and delete characters.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class CharacterView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;
    private JTable myCharacterTable;
    private DefaultTableModel myTableModel;

    /**
     * Constructs a CharacterView with the specified controller and main view.
     *
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public CharacterView(DnDController theController, DnDMainView theMainView) {
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
        myCharacterTable = new JTable();
        myCharacterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Sets up the panel layout and components.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(myCharacterTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Characters"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        JButton editBtn = new JButton("Edit Character");
        JButton deleteBtn = new JButton("Delete Character");
        JButton viewDetailsBtn = new JButton("View Details");

        refreshBtn.addActionListener(e -> refreshData());
        editBtn.addActionListener(e -> editSelectedCharacter());
        deleteBtn.addActionListener(e -> deleteSelectedCharacter());
        viewDetailsBtn.addActionListener(e -> viewCharacterDetails());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the character data from the database.
     */
    public void refreshData() {
        try {
            List<Map<String, Object>> characters = myController.getCharactersWithDetails();

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

            myTableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            myCharacterTable.setModel(myTableModel);
            myCharacterTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            myMainView.showSuccessMessage("Loaded " + characters.size() + " characters");

        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to load characters: " + e.getMessage());
        }
    }

    /**
     * Opens the character editor for the selected character.
     */
    private void editSelectedCharacter() {
        int selectedRow = myCharacterTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a character to edit");
            return;
        }

        try {
            String charName = (String) myCharacterTable.getValueAt(selectedRow, 0);
            Integer level = (Integer) myCharacterTable.getValueAt(selectedRow, 1);
            String className = (String) myCharacterTable.getValueAt(selectedRow, 2);
            String subclassName = (String) myCharacterTable.getValueAt(selectedRow, 3);
            String speciesName = (String) myCharacterTable.getValueAt(selectedRow, 4);
            String subspeciesName = (String) myCharacterTable.getValueAt(selectedRow, 5);
            String backgroundName = (String) myCharacterTable.getValueAt(selectedRow, 6);
            String playerName = (String) myCharacterTable.getValueAt(selectedRow, 7);
            String campaignName = (String) myCharacterTable.getValueAt(selectedRow, 8);
            Integer str = (Integer) myCharacterTable.getValueAt(selectedRow, 9);
            Integer dex = (Integer) myCharacterTable.getValueAt(selectedRow, 10);
            Integer con = (Integer) myCharacterTable.getValueAt(selectedRow, 11);
            Integer intel = (Integer) myCharacterTable.getValueAt(selectedRow, 12);
            Integer wis = (Integer) myCharacterTable.getValueAt(selectedRow, 13);
            Integer cha = (Integer) myCharacterTable.getValueAt(selectedRow, 14);

            int playerId = 0;
            List<Player> players = myController.getAllPlayers();
            for (Player player : players) {
                if (player.getFullName().equals(playerName)) {
                    playerId = player.getPlayerId();
                    break;
                }
            }

            Character characterToEdit = new Character(
                    charName, level, className, subclassName, speciesName, subspeciesName,
                    backgroundName, playerId, campaignName, str, dex, con, intel, wis, cha
            );

            myMainView.switchToCharacterEditor(characterToEdit);

        } catch (Exception e) {
            myMainView.showErrorMessage("Error loading character for editing: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected character after confirmation.
     */
    private void deleteSelectedCharacter() {
        int selectedRow = myCharacterTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a character to delete");
            return;
        }

        String charName = (String) myCharacterTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete character: " + charName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (myController.deleteCharacter(charName)) {
                    myMainView.showSuccessMessage("Character '" + charName + "' deleted successfully");
                    refreshData();
                } else {
                    myMainView.showErrorMessage("Failed to delete character '" + charName + "'");
                }
            } catch (Exception e) {
                myMainView.showErrorMessage("Error deleting character: " + e.getMessage());
            }
        } else {
            myMainView.showInfoMessage("Delete operation cancelled");
        }
    }

    /**
     * Displays detailed information for the selected character.
     */
    private void viewCharacterDetails() {
        int selectedRow = myCharacterTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a character to view details");
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("CHARACTER DETAILS\n");
        details.append("================\n\n");

        for (int i = 0; i < myCharacterTable.getColumnCount(); i++) {
            String columnName = myCharacterTable.getColumnName(i);
            Object value = myCharacterTable.getValueAt(selectedRow, i);
            details.append(columnName).append(": ").append(value).append("\n");
        }

        String charName = (String) myCharacterTable.getValueAt(selectedRow, 0);
        String className = (String) myCharacterTable.getValueAt(selectedRow, 2);
        String subclassName = (String) myCharacterTable.getValueAt(selectedRow, 3);
        String speciesName = (String) myCharacterTable.getValueAt(selectedRow, 4);
        String subspeciesName = (String) myCharacterTable.getValueAt(selectedRow, 5);

        details.append("\n--- FORMATTED INFO ---\n");
        if (className != null && subclassName != null) {
            details.append("Full Class: ").append(className).append(" (").append(subclassName).append(")\n");
        }
        if (speciesName != null && subspeciesName != null) {
            details.append("Full Species: ").append(speciesName).append(" (").append(subspeciesName).append(")\n");
        }

        details.append("\n--- ABILITY MODIFIERS ---\n");
        try {
            Integer str = (Integer) myCharacterTable.getValueAt(selectedRow, 9);
            Integer dex = (Integer) myCharacterTable.getValueAt(selectedRow, 10);
            Integer con = (Integer) myCharacterTable.getValueAt(selectedRow, 11);
            Integer intel = (Integer) myCharacterTable.getValueAt(selectedRow, 12);
            Integer wis = (Integer) myCharacterTable.getValueAt(selectedRow, 13);
            Integer cha = (Integer) myCharacterTable.getValueAt(selectedRow, 14);

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

        myMainView.showInfoMessage("Viewing details for character '" + charName + "'");
    }

    /**
     * Formats an ability score modifier.
     *
     * @param theScore the ability score
     * @return formatted modifier string
     */
    private String getModifierString(Integer theScore) {
        if (theScore == null) return "+0";
        int modifier = (theScore - 10) / 2;
        return modifier >= 0 ? "+" + modifier : String.valueOf(modifier);
    }
}
