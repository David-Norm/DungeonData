import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

// CampaignView.java - Panel for viewing campaigns
public class CampaignView extends JPanel {
    private DnDController controller;
    private JTable campaignTable;
    private DefaultTableModel tableModel;

    public CampaignView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        campaignTable = new JTable();
        campaignTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(campaignTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Campaigns"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        JButton viewDetailsBtn = new JButton("View Campaign Details");

        refreshBtn.addActionListener(e -> refreshData());
        viewDetailsBtn.addActionListener(e -> viewCampaignDetails());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        List<Campaign> campaigns = controller.getAllCampaigns();

        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Campaign Name");
        columnNames.add("Setting");
        columnNames.add("Synopsis");
        columnNames.add("Meeting Time");
        columnNames.add("Max Players");

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        for (Campaign campaign : campaigns) {
            Vector<Object> row = new Vector<Object>();
            row.add(campaign.getGameId());
            row.add(campaign.getSetting());
            row.add(campaign.getSynopsis().length() > 100 ?
                    campaign.getSynopsis().substring(0, 100) + "..." :
                    campaign.getSynopsis());
            row.add(campaign.getMeetingTime());
            row.add(campaign.getMaxPlayers());
            data.add(row);
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        campaignTable.setModel(tableModel);
    }

    private void viewCampaignDetails() {
        int selectedRow = campaignTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a campaign to view.");
            return;
        }

        String campaignName = (String) campaignTable.getValueAt(selectedRow, 0);
        String synopsis = (String) campaignTable.getValueAt(selectedRow, 2);

        // Get full synopsis from the data source
        List<Campaign> campaigns = controller.getAllCampaigns();
        String fullSynopsis = "";
        for (Campaign campaign : campaigns) {
            if (campaign.getGameId().equals(campaignName)) {
                fullSynopsis = campaign.getSynopsis();
                break;
            }
        }

        JTextArea textArea = new JTextArea(fullSynopsis);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(50);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Campaign: " + campaignName, JOptionPane.INFORMATION_MESSAGE);
    }
}