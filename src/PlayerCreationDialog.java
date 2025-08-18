import javax.swing.*;
import java.awt.*;

class PlayerCreationDialog extends JDialog {
    private DnDController controller;
    private boolean playerCreated = false;

    private JTextField fnameField;
    private JTextField lnameField;
    private JComboBox<String> contactCombo;
    private JTextField contactInfoField;
    private JComboBox<String> timezoneCombo;

    public PlayerCreationDialog(JFrame parent, DnDController controller) {
        super(parent, "Add New Player", true);
        this.controller = controller;
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
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
    }

    private void createPlayer() {
        String firstName = fnameField.getText().trim();
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!");
            return;
        }

        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lnameField.getText().trim().isEmpty() ? null : lnameField.getText().trim());
        player.setPreferredContact((String) contactCombo.getSelectedItem());
        player.setContactInfo(contactInfoField.getText().trim());
        player.setTimeZone((String) timezoneCombo.getSelectedItem());

        if (controller.createPlayer(player)) {
            playerCreated = true;
            JOptionPane.showMessageDialog(this, "Player created successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create player.");
        }
    }

    public boolean wasPlayerCreated() {
        return playerCreated;
    }
}