import javax.swing.*;
import java.awt.*;

/**
 * Dialog for creating new players.
 * Provides form fields for entering player information.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
class PlayerCreationDialog extends JDialog {
    private DnDController myController;
    private DnDMainView myMainView;
    private boolean myPlayerCreated = false;

    private JTextField myFnameField;
    private JTextField myLnameField;
    private JComboBox<String> myContactCombo;
    private JTextField myContactInfoField;
    private JComboBox<String> myTimezoneCombo;

    /**
     * Constructs a PlayerCreationDialog.
     *
     * @param theParent the parent frame
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public PlayerCreationDialog(JFrame theParent, DnDController theController, DnDMainView theMainView) {
        super(theParent, "Add New Player", true);
        myController = theController;
        myMainView = theMainView;
        initializeComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(theParent);
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        myFnameField = new JTextField(15);
        myLnameField = new JTextField(15);
        myContactCombo = new JComboBox<>(new String[]{"Discord", "Email", "Phone"});
        myContactInfoField = new JTextField(20);
        myTimezoneCombo = new JComboBox<>(new String[]{"EST", "CST", "MST", "PST", "GMT", "CET"});
    }

    /**
     * Sets up the dialog layout.
     */
    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        add(myFnameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(myLnameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Preferred Contact:"), gbc);
        gbc.gridx = 1;
        add(myContactCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Contact Info:"), gbc);
        gbc.gridx = 1;
        add(myContactInfoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Time Zone:"), gbc);
        gbc.gridx = 1;
        add(myTimezoneCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create Player");
        JButton cancelBtn = new JButton("Cancel");

        createBtn.addActionListener(e -> createPlayer());
        cancelBtn.addActionListener(e -> {
            myMainView.showInfoMessage("Player creation cancelled");
            dispose();
        });

        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
    }

    /**
     * Creates a new player with the form data.
     */
    private void createPlayer() {
        try {
            String firstName = myFnameField.getText().trim();
            if (firstName.isEmpty()) {
                myMainView.showWarningMessage("First name is required for player creation");
                myFnameField.requestFocus();
                return;
            }

            String lastName = myLnameField.getText().trim();
            String contactInfo = myContactInfoField.getText().trim();

            if (contactInfo.isEmpty()) {
                myMainView.showWarningMessage("Contact information is required");
                myContactInfoField.requestFocus();
                return;
            }

            Player player = new Player();
            player.setFirstName(firstName);
            player.setLastName(lastName.isEmpty() ? null : lastName);
            player.setPreferredContact((String) myContactCombo.getSelectedItem());
            player.setContactInfo(contactInfo);
            player.setTimeZone((String) myTimezoneCombo.getSelectedItem());

            if (myController.createPlayer(player)) {
                myPlayerCreated = true;
                String fullName = player.getFullName();
                myMainView.showSuccessMessage("Player '" + fullName + "' created successfully!");
                dispose();
            } else {
                myMainView.showErrorMessage("Failed to create player - please check the information and try again");
            }

        } catch (Exception e) {
            myMainView.showErrorMessage("Error creating player: " + e.getMessage());
        }
    }

    /**
     * Checks if a player was successfully created.
     *
     * @return true if player was created, false otherwise
     */
    public boolean wasPlayerCreated() {
        return myPlayerCreated;
    }
}