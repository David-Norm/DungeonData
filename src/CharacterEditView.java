import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Character editing view for modifying existing D&D characters.
 * Provides form fields pre-populated with character data.
 *
 * @author David Norman
 * @version Summer 2025
 */
public class CharacterEditView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;
    private Character myCurrentCharacter;

    private JLabel myNameLabel;
    private JSpinner myLevelSpinner;
    private JComboBox<String> myClassCombo;
    private JComboBox<String> mySubclassCombo;
    private JComboBox<String> mySpeciesCombo;
    private JComboBox<String> mySubspeciesCombo;
    private JComboBox<String> myBackgroundCombo;
    private JComboBox<Player> myPlayerCombo;
    private JComboBox<Campaign> myCampaignCombo;
    private JSpinner[] myAbilitySpinners;

    private JButton mySaveBtn;
    private JButton myCancelBtn;
    private JButton myRollStatsBtn;

    /**
     * Constructs a CharacterEditView with the specified controller and main view.
     *
     * @param theController the application controller
     * @param theMainView the main application view
     */
    public CharacterEditView(DnDController theController, DnDMainView theMainView) {
        myController = theController;
        myMainView = theMainView;
        initializeComponents();
        setupLayout();
        setVisible(false);
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        myNameLabel = new JLabel("No character selected");
        myNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        myNameLabel.setHorizontalAlignment(JLabel.CENTER);

        myLevelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        myClassCombo = new JComboBox<>();
        mySubclassCombo = new JComboBox<>();
        mySpeciesCombo = new JComboBox<>();
        mySubspeciesCombo = new JComboBox<>();
        myBackgroundCombo = new JComboBox<>();
        myPlayerCombo = new JComboBox<>();
        myCampaignCombo = new JComboBox<>();

        myClassCombo.addActionListener(e -> updateSubclasses());
        mySpeciesCombo.addActionListener(e -> updateSubspecies());

        myAbilitySpinners = new JSpinner[6];
        for (int i = 0; i < 6; i++) {
            myAbilitySpinners[i] = new JSpinner(new SpinnerNumberModel(10, 0, 30, 1));
        }

        mySaveBtn = new JButton("Save Changes");
        myCancelBtn = new JButton("Cancel Edit");
        myRollStatsBtn = new JButton("Roll Random Stats");

        mySaveBtn.addActionListener(e -> saveCharacter());
        myCancelBtn.addActionListener(e -> cancelEdit());
        myRollStatsBtn.addActionListener(e -> rollRandomStats());
    }

    /**
     * Sets up the panel layout and components.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Edit Character"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        headerPanel.add(myNameLabel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myLevelSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Class:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myClassCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Subclass:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySubclassCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Species:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySpeciesCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Subspecies:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySubspeciesCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myBackgroundCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myPlayerCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Campaign:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myCampaignCombo, gbc);

        JPanel abilityPanel = createAbilityScorePanel();
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(abilityPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(mySaveBtn);
        buttonPanel.add(myCancelBtn);
        buttonPanel.add(myRollStatsBtn);

        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the ability score input panel.
     *
     * @return the configured ability score panel
     */
    private JPanel createAbilityScorePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Ability Scores"));

        String[] abilities = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

        // Add labels
        for (String ability : abilities) {
            panel.add(new JLabel(ability, JLabel.CENTER));
        }

        // Add spinners
        for (JSpinner spinner : myAbilitySpinners) {
            panel.add(spinner);
        }

        return panel;
    }

    public void editCharacter(Character character) {
        myCurrentCharacter = character;
        myNameLabel.setText("Editing: " + character.getCharId());

        // Load data first
        refreshData();

        // Then populate fields
        populateFields();

        // Show the panel
        setVisible(true);
        myMainView.showInfoMessage("Editing character: " + character.getCharId());
    }

    private void populateFields() {
        if (myCurrentCharacter == null) return;

        // Set basic info
        myLevelSpinner.setValue(myCurrentCharacter.getLevel());

        // Set class and trigger subclass update
        if (myCurrentCharacter.getClassId() != null) {
            myClassCombo.setSelectedItem(myCurrentCharacter.getClassId());
            updateSubclasses();
            if (myCurrentCharacter.getSubclassId() != null) {
                mySubclassCombo.setSelectedItem(myCurrentCharacter.getSubclassId());
            }
        }

        // Set species and trigger subspecies update
        if (myCurrentCharacter.getSpeciesId() != null) {
            mySpeciesCombo.setSelectedItem(myCurrentCharacter.getSpeciesId());
            updateSubspecies();
            if (myCurrentCharacter.getSubspeciesId() != null) {
                mySubspeciesCombo.setSelectedItem(myCurrentCharacter.getSubspeciesId());
            }
        }

        // Set background
        if (myCurrentCharacter.getBackgroundId() != null) {
            myBackgroundCombo.setSelectedItem(myCurrentCharacter.getBackgroundId());
        }

        // Set player
        for (int i = 0; i < myPlayerCombo.getItemCount(); i++) {
            Player player = myPlayerCombo.getItemAt(i);
            if (player.getPlayerId() == myCurrentCharacter.getPlayerId()) {
                myPlayerCombo.setSelectedItem(player);
                break;
            }
        }

        // Set campaign
        for (int i = 0; i < myCampaignCombo.getItemCount(); i++) {
            Campaign campaign = myCampaignCombo.getItemAt(i);
            if (campaign.getGameId().equals(myCurrentCharacter.getGameId())) {
                myCampaignCombo.setSelectedItem(campaign);
                break;
            }
        }

        // Set ability scores
        myAbilitySpinners[0].setValue(myCurrentCharacter.getStrength());
        myAbilitySpinners[1].setValue(myCurrentCharacter.getDexterity());
        myAbilitySpinners[2].setValue(myCurrentCharacter.getConstitution());
        myAbilitySpinners[3].setValue(myCurrentCharacter.getIntelligence());
        myAbilitySpinners[4].setValue(myCurrentCharacter.getWisdom());
        myAbilitySpinners[5].setValue(myCurrentCharacter.getCharisma());
    }

    public void refreshData() {
        try {
            // Load classes
            myClassCombo.removeAllItems();
            List<String> classes = myController.getClasses();
            for (String dndClass : classes) {
                myClassCombo.addItem(dndClass);
            }

            // Load species
            mySpeciesCombo.removeAllItems();
            List<String> species = myController.getSpecies();
            for (String speciesName : species) {
                mySpeciesCombo.addItem(speciesName);
            }

            // Load backgrounds
            myBackgroundCombo.removeAllItems();
            List<String> backgrounds = myController.getBackgrounds();
            for (String background : backgrounds) {
                myBackgroundCombo.addItem(background);
            }

            // Load players
            myPlayerCombo.removeAllItems();
            List<Player> players = myController.getAllPlayers();
            for (Player player : players) {
                myPlayerCombo.addItem(player);
            }

            // Load campaigns
            myCampaignCombo.removeAllItems();
            List<Campaign> campaigns = myController.getAllCampaigns();
            for (Campaign campaign : campaigns) {
                myCampaignCombo.addItem(campaign);
            }

        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to load character edit data: " + e.getMessage());
        }
    }

    private void updateSubclasses() {
        mySubclassCombo.removeAllItems();
        String selectedClass = (String) myClassCombo.getSelectedItem();
        if (selectedClass != null) {
            List<String> subclasses = myController.getSubclassesByClass(selectedClass);
            for (String subclass : subclasses) {
                mySubclassCombo.addItem(subclass);
            }
        }
    }

    private void updateSubspecies() {
        mySubspeciesCombo.removeAllItems();
        String selectedSpecies = (String) mySpeciesCombo.getSelectedItem();
        if (selectedSpecies != null) {
            List<String> subspecies = myController.getSubspeciesBySpecies(selectedSpecies);
            for (String subspecie : subspecies) {
                mySubspeciesCombo.addItem(subspecie);
            }
        }
    }

    private void rollRandomStats() {
        for (JSpinner spinner : myAbilitySpinners) {
            // Simulate 4d6 drop lowest (common D&D stat generation method)
            int[] rolls = new int[4];
            for (int i = 0; i < 4; i++) {
                rolls[i] = (int) (Math.random() * 6) + 1;
            }
            java.util.Arrays.sort(rolls);
            int total = rolls[1] + rolls[2] + rolls[3]; // Sum highest 3
            spinner.setValue(total);
        }
        myMainView.showSuccessMessage("Random ability scores generated using 4d6 drop lowest method");
    }

    private void saveCharacter() {
        if (myCurrentCharacter == null) {
            myMainView.showErrorMessage("No character to save");
            return;
        }

        try {
            Player selectedPlayer = (Player) myPlayerCombo.getSelectedItem();
            Campaign selectedCampaign = (Campaign) myCampaignCombo.getSelectedItem();

            if (selectedPlayer == null) {
                myMainView.showWarningMessage("Please select a player");
                return;
            }

            if (selectedCampaign == null) {
                myMainView.showWarningMessage("Please select a campaign");
                return;
            }

            // Create updated character with same ID but new values
            Character updatedCharacter = new Character(
                    myCurrentCharacter.getCharId(), // Keep same character ID
                    (Integer) myLevelSpinner.getValue(),
                    (String) myClassCombo.getSelectedItem(),
                    (String) mySubclassCombo.getSelectedItem(),
                    (String) mySpeciesCombo.getSelectedItem(),
                    (String) mySubspeciesCombo.getSelectedItem(),
                    (String) myBackgroundCombo.getSelectedItem(),
                    selectedPlayer.getPlayerId(),
                    selectedCampaign.getGameId(),
                    (Integer) myAbilitySpinners[0].getValue(), // STR
                    (Integer) myAbilitySpinners[1].getValue(), // DEX
                    (Integer) myAbilitySpinners[2].getValue(), // CON
                    (Integer) myAbilitySpinners[3].getValue(), // INT
                    (Integer) myAbilitySpinners[4].getValue(), // WIS
                    (Integer) myAbilitySpinners[5].getValue()  // CHA
            );

            if (myController.updateCharacter(updatedCharacter)) {
                myMainView.showSuccessMessage("Character '" + myCurrentCharacter.getCharId() + "' updated successfully!");

                // Hide the edit panel and refresh the character view
                setVisible(false);
                myMainView.switchToCharactersTab();
            } else {
                myMainView.showErrorMessage("Failed to update character '" + myCurrentCharacter.getCharId() + "'");
            }

        } catch (Exception e) {
            myMainView.showErrorMessage("Error updating character: " + e.getMessage());
        }
    }

    private void cancelEdit() {
        setVisible(false);
        myMainView.showInfoMessage("Character edit cancelled");
        myMainView.switchToCharactersTab();
    }

    public boolean isEditingCharacter() {
        return isVisible() && myCurrentCharacter != null;
    }

    public Character getCurrentCharacter() {
        return myCurrentCharacter;
    }
}