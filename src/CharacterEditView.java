import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class CharacterEditView extends JPanel {
    private DnDController controller;
    private DnDMainView mainView;
    private Character currentCharacter;

    // Form components
    private JLabel nameLabel;
    private JSpinner levelSpinner;
    private JComboBox<String> classCombo;
    private JComboBox<String> subclassCombo;
    private JComboBox<String> speciesCombo;
    private JComboBox<String> subspeciesCombo;
    private JComboBox<String> backgroundCombo;
    private JComboBox<Player> playerCombo;
    private JComboBox<Campaign> campaignCombo;
    private JSpinner[] abilitySpinners;

    // Control buttons
    private JButton saveBtn;
    private JButton cancelBtn;
    private JButton rollStatsBtn;

    public CharacterEditView(DnDController controller, DnDMainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeComponents();
        setupLayout();
        setVisible(false); // Initially hidden
    }

    private void initializeComponents() {
        nameLabel = new JLabel("No character selected");
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

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

        // Control buttons
        saveBtn = new JButton("Save Changes");
        cancelBtn = new JButton("Cancel Edit");
        rollStatsBtn = new JButton("Roll Random Stats");

        saveBtn.addActionListener(e -> saveCharacter());
        cancelBtn.addActionListener(e -> cancelEdit());
        rollStatsBtn.addActionListener(e -> rollRandomStats());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel with character name
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Edit Character"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        headerPanel.add(nameLabel, BorderLayout.CENTER);

        // Main form panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Level
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(levelSpinner, gbc);

        // Class
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Class:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(classCombo, gbc);

        // Subclass
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Subclass:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subclassCombo, gbc);

        // Species
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Species:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(speciesCombo, gbc);

        // Subspecies
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Subspecies:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(subspeciesCombo, gbc);

        // Background
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(backgroundCombo, gbc);

        // Player
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(playerCombo, gbc);

        // Campaign
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Campaign:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(campaignCombo, gbc);

        // Ability Scores Panel
        JPanel abilityPanel = createAbilityScorePanel();
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(abilityPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(rollStatsBtn);

        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Wrap main panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add to layout
        add(headerPanel, BorderLayout.NORTH);
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

    public void editCharacter(Character character) {
        this.currentCharacter = character;
        nameLabel.setText("Editing: " + character.getCharId());

        // Load data first
        refreshData();

        // Then populate fields
        populateFields();

        // Show the panel
        setVisible(true);
        mainView.showInfoMessage("Editing character: " + character.getCharId());
    }

    private void populateFields() {
        if (currentCharacter == null) return;

        // Set basic info
        levelSpinner.setValue(currentCharacter.getLevel());

        // Set class and trigger subclass update
        if (currentCharacter.getClassId() != null) {
            classCombo.setSelectedItem(currentCharacter.getClassId());
            updateSubclasses();
            if (currentCharacter.getSubclassId() != null) {
                subclassCombo.setSelectedItem(currentCharacter.getSubclassId());
            }
        }

        // Set species and trigger subspecies update
        if (currentCharacter.getSpeciesId() != null) {
            speciesCombo.setSelectedItem(currentCharacter.getSpeciesId());
            updateSubspecies();
            if (currentCharacter.getSubspeciesId() != null) {
                subspeciesCombo.setSelectedItem(currentCharacter.getSubspeciesId());
            }
        }

        // Set background
        if (currentCharacter.getBackgroundId() != null) {
            backgroundCombo.setSelectedItem(currentCharacter.getBackgroundId());
        }

        // Set player
        for (int i = 0; i < playerCombo.getItemCount(); i++) {
            Player player = playerCombo.getItemAt(i);
            if (player.getPlayerId() == currentCharacter.getPlayerId()) {
                playerCombo.setSelectedItem(player);
                break;
            }
        }

        // Set campaign
        for (int i = 0; i < campaignCombo.getItemCount(); i++) {
            Campaign campaign = campaignCombo.getItemAt(i);
            if (campaign.getGameId().equals(currentCharacter.getGameId())) {
                campaignCombo.setSelectedItem(campaign);
                break;
            }
        }

        // Set ability scores
        abilitySpinners[0].setValue(currentCharacter.getStrength());
        abilitySpinners[1].setValue(currentCharacter.getDexterity());
        abilitySpinners[2].setValue(currentCharacter.getConstitution());
        abilitySpinners[3].setValue(currentCharacter.getIntelligence());
        abilitySpinners[4].setValue(currentCharacter.getWisdom());
        abilitySpinners[5].setValue(currentCharacter.getCharisma());
    }

    public void refreshData() {
        try {
            // Load classes
            classCombo.removeAllItems();
            List<String> classes = controller.getClasses();
            for (String dndClass : classes) {
                classCombo.addItem(dndClass);
            }

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

        } catch (Exception e) {
            mainView.showErrorMessage("Failed to load character edit data: " + e.getMessage());
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

    private void saveCharacter() {
        if (currentCharacter == null) {
            mainView.showErrorMessage("No character to save");
            return;
        }

        try {
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

            // Create updated character with same ID but new values
            Character updatedCharacter = new Character(
                    currentCharacter.getCharId(), // Keep same character ID
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

            if (controller.updateCharacter(updatedCharacter)) {
                mainView.showSuccessMessage("Character '" + currentCharacter.getCharId() + "' updated successfully!");

                // Hide the edit panel and refresh the character view
                setVisible(false);
                mainView.switchToCharactersTab();
            } else {
                mainView.showErrorMessage("Failed to update character '" + currentCharacter.getCharId() + "'");
            }

        } catch (Exception e) {
            mainView.showErrorMessage("Error updating character: " + e.getMessage());
        }
    }

    private void cancelEdit() {
        setVisible(false);
        mainView.showInfoMessage("Character edit cancelled");
        mainView.switchToCharactersTab();
    }

    public boolean isEditingCharacter() {
        return isVisible() && currentCharacter != null;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }
}