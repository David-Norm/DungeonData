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
    private DnDMainView mainView;
    private JTable campaignTable;
    private DefaultTableModel tableModel;

    public CampaignView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
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
        try {
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

            mainView.showSuccessMessage("Loaded " + campaigns.size() + " campaigns");

        } catch (Exception e) {
            mainView.showErrorMessage("Failed to load campaigns: " + e.getMessage());
        }
    }

    private void viewCampaignDetails() {
        int selectedRow = campaignTable.getSelectedRow();
        if (selectedRow == -1) {
            mainView.showWarningMessage("Please select a campaign to view details");
            return;
        }

        try {
            String campaignName = (String) campaignTable.getValueAt(selectedRow, 0);
            String synopsis = (String) campaignTable.getValueAt(selectedRow, 2);

            // Get full synopsis from the data source
            List<Campaign> campaigns = controller.getAllCampaigns();
            String fullSynopsis = "";
            Campaign selectedCampaign = null;

            for (Campaign campaign : campaigns) {
                if (campaign.getGameId().equals(campaignName)) {
                    fullSynopsis = campaign.getSynopsis();
                    selectedCampaign = campaign;
                    break;
                }
            }

            if (selectedCampaign == null) {
                mainView.showErrorMessage("Could not find campaign details");
                return;
            }

            // Create detailed campaign info
            StringBuilder details = new StringBuilder();
            details.append("CAMPAIGN: ").append(selectedCampaign.getGameId()).append("\n");
            details.append("=".repeat(50)).append("\n\n");
            details.append("Setting: ").append(selectedCampaign.getSetting()).append("\n");
            details.append("Meeting Time: ").append(selectedCampaign.getMeetingTime()).append("\n");
            details.append("Max Players: ").append(selectedCampaign.getMaxPlayers()).append("\n\n");
            details.append("Synopsis:\n");
            details.append("-".repeat(20)).append("\n");
            details.append(fullSynopsis);

            JTextArea textArea = new JTextArea(details.toString());
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setEditable(false);
            textArea.setRows(15);
            textArea.setColumns(60);
            textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane,
                    "Campaign Details: " + campaignName, JOptionPane.INFORMATION_MESSAGE);

            mainView.showInfoMessage("Viewing details for campaign '" + campaignName + "'");

        } catch (Exception e) {
            mainView.showErrorMessage("Error loading campaign details: " + e.getMessage());
        }
    }
}