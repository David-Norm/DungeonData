import javax.swing.*;
import java.awt.*;
import java.util.List;

// CharacterCreatorView.java - Panel for creating new characters
public class CharacterCreatorView extends JPanel {
    private DnDController controller;

    // Form components
    private JTextField nameField;
    private JSpinner levelSpinner;
    private JComboBox<String> subclassCombo;
    private JComboBox<String> subspeciesCombo;
    private JComboBox<String> backgroundCombo;
    private JComboBox<Player> playerCombo;
    private JComboBox<Campaign> campaignCombo;
    private JSpinner[] abilitySpinners;

    public CharacterCreatorView(DnDController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        subclassCombo = new JComboBox<>();
        subspeciesCombo = new JComboBox<>();
        backgroundCombo = new JComboBox<>();
        playerCombo = new JComboBox<>();
        campaignCombo = new JComboBox<>();

        // Initialize ability score spinners
        abilitySpinners = new JSpinner[6];
        for (int i = 0; i < 6; i++) {
            abilitySpinners[i] = new JSpinner(new SpinnerNumberModel(10, 0, 30, 1));
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Character Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Character Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        // Level
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(levelSpinner, gbc);

        // Subclass
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Subclass:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subclassCombo, gbc);

        // Subspecies
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Subspecies:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subspeciesCombo, gbc);

        // Background
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(backgroundCombo, gbc);

        // Player
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(playerCombo, gbc);

        // Campaign
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Campaign:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(campaignCombo, gbc);

        // Ability Scores Panel
        JPanel abilityPanel = createAbilityScorePanel();
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(abilityPanel, gbc);

        // Create Button
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createBtn = new JButton("Create Character");
        createBtn.addActionListener(e -> createCharacter());
        mainPanel.add(createBtn, gbc);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Create New Character"));

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createAbilityScorePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Ability Scores"));

        String[] abilities = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

        // Add labels
        for (String ability : abilities) {
            panel.add(new JLabel(ability, JLabel.CENTER));
        }

        // Add spinners
        for (JSpinner spinner : abilitySpinners) {
            panel.add(spinner);
        }

        return panel;
    }

    public void refreshData() {
        // Load subclasses
        subclassCombo.removeAllItems();
        List<String> subclasses = controller.getSubclasses();
        for (String subclass : subclasses) {
            subclassCombo.addItem(subclass);
        }

        // Load subspecies
        subspeciesCombo.removeAllItems();
        List<String> subspecies = controller.getSubspecies();
        for (String subspecie : subspecies) {
            subspeciesCombo.addItem(subspecie);
        }

        // Load backgrounds
        backgroundCombo.removeAllItems();
        List<String> backgrounds = controller.getBackgrounds();
        for (String background : backgrounds) {
            backgroundCombo.addItem(background);
        }

        // Load players
        playerCombo.removeAllItems();
        List<Player> players = controller.getAllPlayers();
        for (Player player : players) {
            playerCombo.addItem(player);
        }

        // Load campaigns
        campaignCombo.removeAllItems();
        List<Campaign> campaigns = controller.getAllCampaigns();
        for (Campaign campaign : campaigns) {
            campaignCombo.addItem(campaign);
        }
    }

    private void createCharacter() {
        try {
            String charName = nameField.getText().trim();
            if (charName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Character name is required!");
                return;
            }

            Player selectedPlayer = (Player) playerCombo.getSelectedItem();
            Campaign selectedCampaign = (Campaign) campaignCombo.getSelectedItem();

            if (selectedPlayer == null || selectedCampaign == null) {
                JOptionPane.showMessageDialog(this, "Please select both a player and campaign!");
                return;
            }

            Character character = new Character(
                    charName,
                    (Integer) levelSpinner.getValue(),
                    (String) subclassCombo.getSelectedItem(),
                    (String) subspeciesCombo.getSelectedItem(),
                    (String) backgroundCombo.getSelectedItem(),
                    selectedPlayer.getPlayerId(),
                    selectedCampaign.getGameId(),
                    (Integer) abilitySpinners[0].getValue(), // STR
                    (Integer) abilitySpinners[1].getValue(), // DEX
                    (Integer) abilitySpinners[2].getValue(), // CON
                    (Integer) abilitySpinners[3].getValue(), // INT
                    (Integer) abilitySpinners[4].getValue(), // WIS
                    (Integer) abilitySpinners[5].getValue()  // CHA
            );

            if (controller.createCharacter(character)) {
                JOptionPane.showMessageDialog(this, "Character created successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create character. Check if name already exists.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating character: " + e.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        levelSpinner.setValue(1);
        for (JSpinner spinner : abilitySpinners) {
            spinner.setValue(10);
        }
    }
}