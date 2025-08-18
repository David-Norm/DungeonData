import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

// CharacterView.java - Panel for viewing and managing characters
class CharacterView extends JPanel {
    private DnDController controller;
    private JTable characterTable;
    private DefaultTableModel tableModel;

    public CharacterView(DnDController controller) {
        this.controller = controller;
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

    public void refreshData() {
        List<Map<String, Object>> characters = controller.getCharactersWithDetails();

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Character Name");
        columnNames.add("Level");
        columnNames.add("Subclass");
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
            row.add(character.get("subclass_id"));
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
    }

    private void editSelectedCharacter() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a character to edit.");
            return;
        }

        String charName = (String) characterTable.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this, "Edit functionality for " + charName + " would be implemented here.\nFor now, use the Character Creator to make new characters.");
    }

    private void deleteSelectedCharacter() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a character to delete.");
            return;
        }

        String charName = (String) characterTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete character: " + charName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCharacter(charName)) {
                JOptionPane.showMessageDialog(this, "Character deleted successfully!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete character.");
            }
        }
    }

    private void viewCharacterDetails() {
        int selectedRow = characterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a character to view.");
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

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Character Details", JOptionPane.INFORMATION_MESSAGE);
    }
}