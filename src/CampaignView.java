/**
 * Campaign management view for displaying and interacting with campaign data.
 * Provides functionality to view campaign details and synopses.
 *
 * @author David Norman
 * @version Summer 2025
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class CampaignView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;
    private JTable myCampaignTable;
    private DefaultTableModel myTableModel;

    /**
     * Constructs a CampaignView with the specified controller and main view.
     *
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public CampaignView(DnDController theController, DnDMainView theMainView) {
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
        myCampaignTable = new JTable();
        myCampaignTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        myCampaignTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent theEvent) {
                if (theEvent.getClickCount() == 2) {
                    handleDoubleClick(theEvent);
                }
            }
        });
    }

    /**
     * Handles double-click events on the campaign table.
     *
     * @param theEvent the mouse event
     */
    private void handleDoubleClick(MouseEvent theEvent) {
        int row = myCampaignTable.rowAtPoint(theEvent.getPoint());
        int col = myCampaignTable.columnAtPoint(theEvent.getPoint());

        if (row >= 0 && col >= 0) {
            String columnName = myCampaignTable.getColumnName(col);

            if (columnName.equals("Synopsis")) {
                try {
                    String campaignName = (String) myCampaignTable.getValueAt(row, 0);

                    List<Campaign> campaigns = myController.getAllCampaigns();
                    String fullSynopsis = "";

                    for (Campaign campaign : campaigns) {
                        if (campaign.getGameId().equals(campaignName)) {
                            fullSynopsis = campaign.getSynopsis();
                            break;
                        }
                    }

                    if (!fullSynopsis.isEmpty()) {
                        showTextPopup("Campaign Synopsis: " + campaignName, fullSynopsis);
                    } else {
                        myMainView.showWarningMessage("No synopsis available for this campaign");
                    }

                } catch (Exception ex) {
                    myMainView.showErrorMessage("Error loading campaign synopsis: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Shows a text popup dialog with scrollable content.
     *
     * @param theTitle the dialog title
     * @param theText the text content to display
     */
    private void showTextPopup(String theTitle, String theText) {
        JTextArea textArea = new JTextArea(theText);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(60);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 248, 248));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JOptionPane.showMessageDialog(this, scrollPane, theTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sets up the panel layout and components.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(myCampaignTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Campaigns - Double-click Synopsis for full text"));

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

    /**
     * Refreshes the campaign data from the database.
     */
    public void refreshData() {
        try {
            List<Campaign> campaigns = myController.getAllCampaigns();

            Vector<String> columnNames = new Vector<String>();
            columnNames.add("Campaign Name");
            columnNames.add("Setting");
            columnNames.add("Synopsis");
            columnNames.add("Meeting Time");

            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            for (Campaign campaign : campaigns) {
                Vector<Object> row = new Vector<Object>();
                row.add(campaign.getGameId());
                row.add(campaign.getSetting());
                row.add(campaign.getSynopsis().length() > 100 ?
                        campaign.getSynopsis().substring(0, 100) + "..." :
                        campaign.getSynopsis());
                row.add(campaign.getMeetingTime());
                data.add(row);
            }

            myTableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int theRow, int theColumn) {
                    return false;
                }
            };
            myCampaignTable.setModel(myTableModel);

            myMainView.showSuccessMessage("Loaded " + campaigns.size() + " campaigns");

        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to load campaigns: " + e.getMessage());
        }
    }

    /**
     * Displays detailed information for the selected campaign.
     */
    private void viewCampaignDetails() {
        int selectedRow = myCampaignTable.getSelectedRow();
        if (selectedRow == -1) {
            myMainView.showWarningMessage("Please select a campaign to view details");
            return;
        }

        try {
            String campaignName = (String) myCampaignTable.getValueAt(selectedRow, 0);

            List<Campaign> campaigns = myController.getAllCampaigns();
            Campaign selectedCampaign = null;

            for (Campaign campaign : campaigns) {
                if (campaign.getGameId().equals(campaignName)) {
                    selectedCampaign = campaign;
                    break;
                }
            }

            if (selectedCampaign == null) {
                myMainView.showErrorMessage("Could not find campaign details");
                return;
            }

            StringBuilder details = new StringBuilder();
            details.append("CAMPAIGN: ").append(selectedCampaign.getGameId()).append("\n");
            details.append("=".repeat(50)).append("\n\n");
            details.append("Setting: ").append(selectedCampaign.getSetting()).append("\n");
            details.append("Meeting Time: ").append(selectedCampaign.getMeetingTime()).append("\n\n");
            details.append("Synopsis:\n");
            details.append("-".repeat(20)).append("\n");
            details.append(selectedCampaign.getSynopsis());

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

            myMainView.showInfoMessage("Viewing details for campaign '" + campaignName + "'");

        } catch (Exception e) {
            myMainView.showErrorMessage("Error loading campaign details: " + e.getMessage());
        }
    }
}