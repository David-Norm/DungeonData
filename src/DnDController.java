/**
 * Main controller class for the D&D Character Database Manager.
 * Coordinates between the view layer and data access objects.
 *
 * @author David Norman
 * @version Summer 2025
 */
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DnDController {
    private DnDMainView myMainView;
    private CharacterDAO myCharacterDAO;
    private PlayerDAO myPlayerDAO;
    private CampaignDAO myCampaignDAO;
    private LookupDAO myLookupDAO;
    private ReportDAO myReportDAO;

    /**
     * Constructs the main controller and initializes all DAOs and the main view.
     */
    public DnDController() {
        try {
            myCharacterDAO = new CharacterDAO();
            myPlayerDAO = new PlayerDAO();
            myCampaignDAO = new CampaignDAO();
            myLookupDAO = new LookupDAO();
            myReportDAO = new ReportDAO();

            myMainView = new DnDMainView(this);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Database connection failed: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Shows the main application window.
     */
    public void showMainView() {
        myMainView.setVisible(true);
    }

    /**
     * Retrieves detailed character information.
     *
     * @return list of character maps with related data
     */
    public List<Map<String, Object>> getCharactersWithDetails() {
        try {
            return myCharacterDAO.getCharactersWithDetails();
        } catch (SQLException e) {
            handleError("Error loading characters", e);
            return List.of();
        }
    }

    /**
     * Creates a new character in the database.
     *
     * @param theCharacter the character to create
     * @return true if creation was successful, false otherwise
     */
    public boolean createCharacter(Character theCharacter) {
        try {
            return myCharacterDAO.insertCharacter(theCharacter);
        } catch (SQLException e) {
            handleError("Error creating character", e);
            return false;
        }
    }

    /**
     * Updates an existing character in the database.
     *
     * @param theCharacter the character with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateCharacter(Character theCharacter) {
        try {
            return myCharacterDAO.updateCharacter(theCharacter);
        } catch (SQLException e) {
            handleError("Error updating character", e);
            return false;
        }
    }

    /**
     * Deletes a character from the database.
     *
     * @param theCharacterId the ID of the character to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteCharacter(String theCharacterId) {
        try {
            return myCharacterDAO.deleteCharacter(theCharacterId);
        } catch (SQLException e) {
            handleError("Error deleting character", e);
            return false;
        }
    }

    /**
     * Retrieves all characters belonging to a specific player.
     *
     * @param thePlayerId the ID of the player
     * @return list of characters owned by the player
     */
    public List<Character> getCharactersByPlayer(int thePlayerId) {
        try {
            return myCharacterDAO.getCharactersByPlayer(thePlayerId);
        } catch (SQLException e) {
            handleError("Error loading characters for player", e);
            return List.of();
        }
    }

    /**
     * Retrieves all players from the database.
     *
     * @return list of all players
     */
    public List<Player> getAllPlayers() {
        try {
            return myPlayerDAO.getAllPlayers();
        } catch (SQLException e) {
            handleError("Error loading players", e);
            return List.of();
        }
    }

    /**
     * Creates a new player in the database.
     *
     * @param thePlayer the player to create
     * @return true if creation was successful, false otherwise
     */
    public boolean createPlayer(Player thePlayer) {
        try {
            if (thePlayer.getPlayerId() == 0) {
                thePlayer.setPlayerId(myPlayerDAO.getNextPlayerId());
            }
            return myPlayerDAO.insertPlayer(thePlayer);
        } catch (SQLException e) {
            handleError("Error creating player", e);
            return false;
        }
    }

    /**
     * Retrieves all campaigns from the database.
     *
     * @return list of all campaigns
     */
    public List<Campaign> getAllCampaigns() {
        try {
            return myCampaignDAO.getAllCampaigns();
        } catch (SQLException e) {
            handleError("Error loading campaigns", e);
            return List.of();
        }
    }

    /**
     * Retrieves all class names from the database.
     *
     * @return list of class IDs
     */
    public List<String> getClasses() {
        try {
            return myLookupDAO.getClasses();
        } catch (SQLException e) {
            handleError("Error loading classes", e);
            return List.of();
        }
    }

    /**
     * Retrieves all subclass names from the database.
     *
     * @return list of subclass IDs
     */
    public List<String> getSubclasses() {
        try {
            return myLookupDAO.getSubclasses();
        } catch (SQLException e) {
            handleError("Error loading subclasses", e);
            return List.of();
        }
    }

    /**
     * Retrieves subclasses for a specific class.
     *
     * @param theClassId the class to get subclasses for
     * @return list of subclass IDs for the specified class
     */
    public List<String> getSubclassesByClass(String theClassId) {
        try {
            return myLookupDAO.getSubclassesByClass(theClassId);
        } catch (SQLException e) {
            handleError("Error loading subclasses for class: " + theClassId, e);
            return List.of();
        }
    }

    /**
     * Retrieves all species names from the database.
     *
     * @return list of species IDs
     */
    public List<String> getSpecies() {
        try {
            return myLookupDAO.getSpecies();
        } catch (SQLException e) {
            handleError("Error loading species", e);
            return List.of();
        }
    }

    /**
     * Retrieves all subspecies names from the database.
     *
     * @return list of subspecies IDs
     */
    public List<String> getSubspecies() {
        try {
            return myLookupDAO.getSubspecies();
        } catch (SQLException e) {
            handleError("Error loading subspecies", e);
            return List.of();
        }
    }

    /**
     * Retrieves subspecies for a specific species.
     *
     * @param theSpeciesId the species to get subspecies for
     * @return list of subspecies IDs for the specified species
     */
    public List<String> getSubspeciesBySpecies(String theSpeciesId) {
        try {
            return myLookupDAO.getSubspeciesBySpecies(theSpeciesId);
        } catch (SQLException e) {
            handleError("Error loading subspecies for species: " + theSpeciesId, e);
            return List.of();
        }
    }

    /**
     * Retrieves all background names from the database.
     *
     * @return list of background IDs
     */
    public List<String> getBackgrounds() {
        try {
            return myLookupDAO.getBackgrounds();
        } catch (SQLException e) {
            handleError("Error loading backgrounds", e);
            return List.of();
        }
    }

    /**
     * Retrieves all class objects with detailed information.
     *
     * @return list of DnDClass objects
     */
    public List<DnDClass> getAllClasses() {
        try {
            return myLookupDAO.getAllClasses();
        } catch (SQLException e) {
            handleError("Error loading classes", e);
            return List.of();
        }
    }

    /**
     * Retrieves all species objects with detailed information.
     *
     * @return list of Species objects
     */
    public List<Species> getAllSpecies() {
        try {
            return myLookupDAO.getAllSpecies();
        } catch (SQLException e) {
            handleError("Error loading species", e);
            return List.of();
        }
    }

    /**
     * Generates characters by class and campaign report.
     *
     * @return list of character-class-campaign data
     */
    public List<Map<String, Object>> getCharactersByClassAndCampaign() {
        try {
            return myReportDAO.getCharactersByClassAndCampaign();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    /**
     * Generates classes with most subclasses report.
     *
     * @return list of classes and their subclass counts
     */
    public List<Map<String, Object>> getClassesWithMostSubclasses() {
        try {
            return myReportDAO.getClassesWithMostSubclasses();
        } catch (SQLException e) {
            handleError("Error generating classes with most subclasses report", e);
            return List.of();
        }
    }

    /**
     * Generates above average level by species report.
     *
     * @return list of characters above species average level
     */
    public List<Map<String, Object>> getAboveAverageLevelBySpecies() {
        try {
            return myReportDAO.getAboveAverageLevelBySpecies();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    /**
     * Generates all players and characters report.
     *
     * @return list of player-character relationships
     */
    public List<Map<String, Object>> getAllPlayersAndCharacters() {
        try {
            return myReportDAO.getAllPlayersAndCharacters();
        } catch (SQLException e) {
            handleError("Error generating all players and characters report", e);
            return List.of();
        }
    }

    /**
     * Generates popular settings and military report.
     *
     * @return list of characters matching criteria
     */
    public List<Map<String, Object>> getPopularSettingsAndMilitary() {
        try {
            return myReportDAO.getPopularSettingsAndMilitary();
        } catch (SQLException e) {
            handleError("Error generating popular settings and military report", e);
            return List.of();
        }
    }

    /**
     * Generates character species and size report.
     *
     * @return list of character-species-size data
     */
    public List<Map<String, Object>> getCharacterSpeciesAndSize() {
        try {
            return myReportDAO.getCharacterSpeciesAndSize();
        } catch (SQLException e) {
            handleError("Error generating character species and size report", e);
            return List.of();
        }
    }

    /**
     * Generates player character counts report.
     *
     * @return list of players and their character counts
     */
    public List<Map<String, Object>> getPlayerCharacterCounts() {
        try {
            return myReportDAO.getPlayerCharacterCounts();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    /**
     * Generates campaign participation report.
     *
     * @return list of campaigns and their player counts
     */
    public List<Map<String, Object>> getCampaignParticipation() {
        try {
            return myReportDAO.getCampaignParticipation();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    /**
     * Generates class distribution report.
     *
     * @return list of classes and their usage statistics
     */
    public List<Map<String, Object>> getClassDistribution() {
        try {
            return myReportDAO.getClassDistribution();
        } catch (SQLException e) {
            handleError("Error generating class distribution report", e);
            return List.of();
        }
    }

    /**
     * Generates character ability modifiers report.
     *
     * @return list of characters with calculated ability modifiers
     */
    public List<Map<String, Object>> getCharacterAbilityModifiers() {
        try {
            return myReportDAO.getCharacterAbilityModifiers();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    /**
     * Shuts down the application and closes database connections.
     */
    public void shutdown() {
        try {
            DatabaseConnection.getInstance().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Handles errors by logging and displaying them to the user.
     *
     * @param theMessage the error message
     * @param theException the exception that occurred
     */
    private void handleError(String theMessage, Exception theException) {
        System.err.println(theMessage + ": " + theException.getMessage());
        theException.printStackTrace();

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(myMainView,
                    theMessage + ": " + theException.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Main method to start the application.
     *
     * @param theArgs command line arguments
     */
    public static void main(String[] theArgs) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            DnDController controller = new DnDController();
            controller.showMainView();
        });
    }
}