import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

class PlayerView extends JPanel {
    private DnDController controller;
    private JTable playerTable;
    private DefaultTableModel tableModel;

    public PlayerView(DnDController controller) {
        this.controller = controller;
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
    }

    private void showAddPlayerDialog() {
        PlayerCreationDialog dialog = new PlayerCreationDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), controller);
        dialog.setVisible(true);

        if (dialog.wasPlayerCreated()) {
            refreshData();
        }
    }

    private void viewPlayerCharacters() {
        int selectedRow = playerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a player to view their characters.");
            return;
        }

        int playerId = (Integer) playerTable.getValueAt(selectedRow, 0);
        String playerName = playerTable.getValueAt(selectedRow, 1) + " " +
                (playerTable.getValueAt(selectedRow, 2) != null ? playerTable.getValueAt(selectedRow, 2) : "");

        JOptionPane.showMessageDialog(this,
                "Character list for " + playerName + " would be shown here.\n" +
                        "For now, check the Characters tab and filter by player name.");
    }
}