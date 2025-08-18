import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// CharacterCreatorView.java - Panel for creating new characters
public class CharacterCreatorView extends JPanel {
    private DnDController controller;
    private DnDMainView mainView;

    // Form components
    private JTextField nameField;
    private JSpinner levelSpinner;
    private JComboBox<String> classCombo;
    private JComboBox<String> subclassCombo;
    private JComboBox<String> speciesCombo;
    private JComboBox<String> subspeciesCombo;
    private JComboBox<String> backgroundCombo;
    private JComboBox<Player> playerCombo;
    private JComboBox<Campaign> campaignCombo;
    private JSpinner[] abilitySpinners;

    public CharacterCreatorView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        classCombo = new JComboBox<>();
        subclassCombo = new JComboBox<>();
        speciesCombo = new JComboBox<>();
        subspeciesCombo = new JComboBox<>();
        backgroundCombo = new JComboBox<>();
        playerCombo = new JComboBox<>();
        campaignCombo = new JComboBox<>();

        // Add listeners for class/species changes to update subclass/subspecies options
        classCombo.addActionListener(e -> updateSubclasses());
        speciesCombo.addActionListener(e -> updateSubspecies());

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

        // Class
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Class:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(classCombo, gbc);

        // Subclass
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Subclass:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subclassCombo, gbc);

        // Species
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Species:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(speciesCombo, gbc);

        // Subspecies
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Subspecies:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subspeciesCombo, gbc);

        // Background
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(backgroundCombo, gbc);

        // Player
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(playerCombo, gbc);

        // Campaign
        gbc.gridx = 0; gbc.gridy = 8;
        mainPanel.add(new JLabel("Campaign:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(campaignCombo, gbc);

        // Ability Scores Panel
        JPanel abilityPanel = createAbilityScorePanel();
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(abilityPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create Character");
        JButton clearBtn = new JButton("Clear Form");
        JButton rollStatsBtn = new JButton("Roll Random Stats");

        createBtn.addActionListener(e -> createCharacter());
        clearBtn.addActionListener(e -> clearForm());
        rollStatsBtn.addActionListener(e -> rollRandomStats());

        buttonPanel.add(createBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(rollStatsBtn);

        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

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
        try {
            // Load classes
            classCombo.removeAllItems();
            List<String> classes = controller.getClasses();
            for (String dndClass : classes) {
                classCombo.addItem(dndClass);
            }
            mainView.showInfoMessage("Loaded " + classes.size() + " classes");

            // Load species
            speciesCombo.removeAllItems();
            List<String> species = controller.getSpecies();
            for (String speciesName : species) {
                speciesCombo.addItem(speciesName);
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

            // Update subclasses and subspecies for initially selected items
            updateSubclasses();
            updateSubspecies();

            mainView.showSuccessMessage("Character creation form loaded with " + classes.size() + " classes, " +
                    species.size() + " species, " + players.size() + " players, and " +
                    campaigns.size() + " campaigns");

        } catch (Exception e) {
            mainView.showErrorMessage("Failed to load character creation data: " + e.getMessage());
        }
    }

    private void updateSubclasses() {
        subclassCombo.removeAllItems();
        String selectedClass = (String) classCombo.getSelectedItem();
        if (selectedClass != null) {
            List<String> subclasses = controller.getSubclassesByClass(selectedClass);
            for (String subclass : subclasses) {
                subclassCombo.addItem(subclass);
            }
            mainView.showInfoMessage("Loaded " + subclasses.size() + " subclasses for " + selectedClass);
        }
    }

    private void updateSubspecies() {
        subspeciesCombo.removeAllItems();
        String selectedSpecies = (String) speciesCombo.getSelectedItem();
        if (selectedSpecies != null) {
            List<String> subspecies = controller.getSubspeciesBySpecies(selectedSpecies);
            for (String subspecie : subspecies) {
                subspeciesCombo.addItem(subspecie);
            }
            mainView.showInfoMessage("Loaded " + subspecies.size() + " subspecies for " + selectedSpecies);
        }
    }

    private void rollRandomStats() {
        for (JSpinner spinner : abilitySpinners) {
            // Simulate 4d6 drop lowest (common D&D stat generation method)
            int[] rolls = new int[4];
            for (int i = 0; i < 4; i++) {
                rolls[i] = (int) (Math.random() * 6) + 1;
            }
            java.util.Arrays.sort(rolls);
            int total = rolls[1] + rolls[2] + rolls[3]; // Sum highest 3
            spinner.setValue(total);
        }
        mainView.showSuccessMessage("Random ability scores generated using 4d6 drop lowest method");
    }

    private void createCharacter() {
        try {
            String charName = nameField.getText().trim();
            if (charName.isEmpty()) {
                mainView.showWarningMessage("Character name is required");
                return;
            }

            Player selectedPlayer = (Player) playerCombo.getSelectedItem();
            Campaign selectedCampaign = (Campaign) campaignCombo.getSelectedItem();

            if (selectedPlayer == null) {
                mainView.showWarningMessage("Please select a player");
                return;
            }

            if (selectedCampaign == null) {
                mainView.showWarningMessage("Please select a campaign");
                return;
            }

            Character character = new Character(
                    charName,
                    (Integer) levelSpinner.getValue(),
                    (String) classCombo.getSelectedItem(),
                    (String) subclassCombo.getSelectedItem(),
                    (String) speciesCombo.getSelectedItem(),
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
                mainView.showSuccessMessage("Character '" + charName + "' created successfully!");
                clearForm();

                // Optionally switch to Characters tab to see the new character
                mainView.switchToCharactersTab();
            } else {
                mainView.showErrorMessage("Failed to create character '" + charName + "' - name may already exist");
            }

        } catch (Exception e) {
            mainView.showErrorMessage("Error creating character: " + e.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        levelSpinner.setValue(1);
        for (JSpinner spinner : abilitySpinners) {
            spinner.setValue(10);
        }
        mainView.showInfoMessage("Form cleared - ready for new character");
    }
}