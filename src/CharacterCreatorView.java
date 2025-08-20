import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Character creation view for creating new D&D characters.
 * Provides form fields for all character attributes including ability scores,
 * class selection, species selection, and campaign assignment.
 *
 * @author David Norman
 * @version Summer 2025
 */
public class CharacterCreatorView extends JPanel {
    private DnDController myController;
    private DnDMainView myMainView;

    private JTextField myNameField;
    private JSpinner myLevelSpinner;
    private JComboBox<String> myClassCombo;
    private JComboBox<String> mySubclassCombo;
    private JComboBox<String> mySpeciesCombo;
    private JComboBox<String> mySubspeciesCombo;
    private JComboBox<String> myBackgroundCombo;
    private JComboBox<Player> myPlayerCombo;
    private JComboBox<Campaign> myCampaignCombo;
    private JSpinner[] myAbilitySpinners;

    /**
     * Constructs a CharacterCreatorView with the specified myController and main view.
     * Initializes all components, sets up the layout, and loads initial data.
     *
     * @param theController the application myController for data operations
     * @param theMainView the main application view for status messages
     */
    public CharacterCreatorView(DnDController theController, DnDMainView theMainView) {
        myController = theController;
        myMainView = theMainView;
        initializeComponents();
        setupLayout();
        refreshData();
    }

    /**
     * Initializes all GUI components including text fields, spinners, and combo boxes.
     * Sets up listeners for dynamic updates of subclasses and subspecies.
     */
    private void initializeComponents() {
        myNameField = new JTextField(20);
        myLevelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        myClassCombo = new JComboBox<>();
        mySubclassCombo = new JComboBox<>();
        mySpeciesCombo = new JComboBox<>();
        mySubspeciesCombo = new JComboBox<>();
        myBackgroundCombo = new JComboBox<>();
        myPlayerCombo = new JComboBox<>();
        myCampaignCombo = new JComboBox<>();

        // Add listeners for class/species changes to update subclass/subspecies options
        myClassCombo.addActionListener(e -> updateSubclasses());
        mySpeciesCombo.addActionListener(e -> updateSubspecies());

        // Initialize ability score spinners with default values
        myAbilitySpinners = new JSpinner[6];
        for (int i = 0; i < 6; i++) {
            myAbilitySpinners[i] = new JSpinner(new SpinnerNumberModel(10, 0, 30, 1));
        }
    }

    /**
     * Sets up the panel layout using GridBagLayout for form components.
     * Creates sections for character details, ability scores, and action buttons.
     */
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
        mainPanel.add(myNameField, gbc);

        // Level
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myLevelSpinner, gbc);

        // Class
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Class:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myClassCombo, gbc);

        // Subclass
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Subclass:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySubclassCombo, gbc);

        // Species
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Species:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySpeciesCombo, gbc);

        // Subspecies
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Subspecies:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(mySubspeciesCombo, gbc);

        // Background
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myBackgroundCombo, gbc);

        // Player
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myPlayerCombo, gbc);

        // Campaign
        gbc.gridx = 0; gbc.gridy = 8;
        mainPanel.add(new JLabel("Campaign:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(myCampaignCombo, gbc);

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

        // Wrap in scroll pane for better usability
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Create New Character"));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the ability score input panel with labels and spinners.
     * Arranges the six D&D ability scores (STR, DEX, CON, INT, WIS, CHA) in a grid.
     *
     * @return the configured ability score panel
     */
    private JPanel createAbilityScorePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Ability Scores"));

        String[] abilities = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

        // Add labels for each ability
        for (String ability : abilities) {
            panel.add(new JLabel(ability, JLabel.CENTER));
        }

        // Add spinners for each ability score
        for (JSpinner spinner : myAbilitySpinners) {
            panel.add(spinner);
        }

        return panel;
    }

    /**
     * Refreshes all form data by loading current information from the database.
     * Updates combo boxes with classes, species, backgrounds, players, and campaigns.
     * Also updates dependent dropdowns (subclasses and subspecies).
     */
    public void refreshData() {
        try {
            // Load classes
            myClassCombo.removeAllItems();
            List<String> classes = myController.getClasses();
            for (String dndClass : classes) {
                myClassCombo.addItem(dndClass);
            }
            myMainView.showInfoMessage("Loaded " + classes.size() + " classes");

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

            // Update subclasses and subspecies for initially selected items
            updateSubclasses();
            updateSubspecies();

            myMainView.showSuccessMessage("Character creation form loaded with " + classes.size() + " classes, " +
                    species.size() + " species, " + players.size() + " players, and " +
                    campaigns.size() + " campaigns");

        } catch (Exception e) {
            myMainView.showErrorMessage("Failed to load character creation data: " + e.getMessage());
        }
    }

    /**
     * Updates the subclass dropdown based on the currently selected class.
     * Clears existing subclasses and loads those available for the selected class.
     */
    private void updateSubclasses() {
        mySubclassCombo.removeAllItems();
        String selectedClass = (String) myClassCombo.getSelectedItem();
        if (selectedClass != null) {
            List<String> subclasses = myController.getSubclassesByClass(selectedClass);
            for (String subclass : subclasses) {
                mySubclassCombo.addItem(subclass);
            }
            myMainView.showInfoMessage("Loaded " + subclasses.size() + " subclasses for " + selectedClass);
        }
    }

    /**
     * Updates the subspecies dropdown based on the currently selected species.
     * Clears existing subspecies and loads those available for the selected species.
     */
    private void updateSubspecies() {
        mySubspeciesCombo.removeAllItems();
        String selectedSpecies = (String) mySpeciesCombo.getSelectedItem();
        if (selectedSpecies != null) {
            List<String> subspecies = myController.getSubspeciesBySpecies(selectedSpecies);
            for (String subspecie : subspecies) {
                mySubspeciesCombo.addItem(subspecie);
            }
            myMainView.showInfoMessage("Loaded " + subspecies.size() + " subspecies for " + selectedSpecies);
        }
    }

    /**
     * Generates random ability scores using the 4d6 drop lowest method.
     * This is a common D&D character generation technique that rolls four six-sided dice
     * and uses the sum of the three highest rolls for each ability score.
     */
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

    /**
     * Creates a new character with the form data and saves it to the database.
     * Validates required fields and displays appropriate success or error messages.
     * Clears the form and switches to the Characters tab upon successful creation.
     */
    private void createCharacter() {
        try {
            String charName = myNameField.getText().trim();
            if (charName.isEmpty()) {
                myMainView.showWarningMessage("Character name is required");
                return;
            }

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

            Character character = new Character(
                    charName,
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

            if (myController.createCharacter(character)) {
                myMainView.showSuccessMessage("Character '" + charName + "' created successfully!");
                clearForm();

                // Switch to Characters tab to show the new character
                myMainView.switchToCharactersTab();
            } else {
                myMainView.showErrorMessage("Failed to create character '" + charName + "' - name may already exist");
            }

        } catch (Exception e) {
            myMainView.showErrorMessage("Error creating character: " + e.getMessage());
        }
    }

    /**
     * Clears all form fields and resets them to their default values.
     * Resets character name, level to 1, and all ability scores to 10.
     */
    private void clearForm() {
        myNameField.setText("");
        myLevelSpinner.setValue(1);
        for (JSpinner spinner : myAbilitySpinners) {
            spinner.setValue(10);
        }
        myMainView.showInfoMessage("Form cleared - ready for new character");
    }
}