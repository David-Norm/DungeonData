import javax.swing.*;
import java.awt.*;

class PlayerCreationDialog extends JDialog {
    private DnDController controller;
    private DnDMainView mainView;
    private boolean playerCreated = false;

    private JTextField fnameField;
    private JTextField lnameField;
    private JComboBox<String> contactCombo;
    private JTextField contactInfoField;
    private JComboBox<String> timezoneCombo;

    public PlayerCreationDialog(JFrame parent, DnDController controller, DnDMainView mainView) {
        super(parent, "Add New Player", true);
        this.controller = controller;
        this.mainView = mainView;
        initializeComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        fnameField = new JTextField(15);
        lnameField = new JTextField(15);
        contactCombo = new JComboBox<>(new String[]{"Discord", "Email", "Phone"});
        contactInfoField = new JTextField(20);
        timezoneCombo = new JComboBox<>(new String[]{"EST", "CST", "MST", "PST", "GMT", "CET"});
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        add(fnameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(lnameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Preferred Contact:"), gbc);
        gbc.gridx = 1;
        add(contactCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Contact Info:"), gbc);
        gbc.gridx = 1;
        add(contactInfoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Time Zone:"), gbc);
        gbc.gridx = 1;
        add(timezoneCombo, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create Player");
        JButton cancelBtn = new JButton("Cancel");

        createBtn.addActionListener(e -> createPlayer());
        cancelBtn.addActionListener(e -> {
            mainView.showInfoMessage("Player creation cancelled");
            dispose();
        });

        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
    }

    private void createPlayer() {
        try {
            String firstName = fnameField.getText().trim();
            if (firstName.isEmpty()) {
                mainView.showWarningMessage("First name is required for player creation");
                fnameField.requestFocus();
                return;
            }

            String lastName = lnameField.getText().trim();
            String contactInfo = contactInfoField.getText().trim();

            if (contactInfo.isEmpty()) {
                mainView.showWarningMessage("Contact information is required");
                contactInfoField.requestFocus();
                return;
            }

            Player player = new Player();
            player.setFirstName(firstName);
            player.setLastName(lastName.isEmpty() ? null : lastName);
            player.setPreferredContact((String) contactCombo.getSelectedItem());
            player.setContactInfo(contactInfo);
            player.setTimeZone((String) timezoneCombo.getSelectedItem());

            if (controller.createPlayer(player)) {
                playerCreated = true;
                String fullName = player.getFullName();
                mainView.showSuccessMessage("Player '" + fullName + "' created successfully!");
                dispose();
            } else {
                mainView.showErrorMessage("Failed to create player - please check the information and try again");
            }

        } catch (Exception e) {
            mainView.showErrorMessage("Error creating player: " + e.getMessage());
        }
    }

    public boolean wasPlayerCreated() {
        return playerCreated;
    }
}