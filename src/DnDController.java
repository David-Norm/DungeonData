import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class DnDController {
    private DnDMainView mainView;
    private CharacterDAO characterDAO;
    private PlayerDAO playerDAO;
    private CampaignDAO campaignDAO;
    private LookupDAO lookupDAO;
    private ReportDAO reportDAO;

    public DnDController() {
        try {
            // Initialize DAOs
            this.characterDAO = new CharacterDAO();
            this.playerDAO = new PlayerDAO();
            this.campaignDAO = new CampaignDAO();
            this.lookupDAO = new LookupDAO();
            this.reportDAO = new ReportDAO();

            // Initialize main view
            this.mainView = new DnDMainView(this);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Database connection failed: " + e.getMessage() +
                            "\n\nMake sure:\n" +
                            "1. MySQL server is running\n" +
                            "2. Database 'dungeondata' exists\n" +
                            "3. Username/password are correct\n" +
                            "4. MySQL Connector/J is in classpath",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void showMainView() {
        mainView.setVisible(true);
    }

    // Character operations
    public List<Map<String, Object>> getCharactersWithDetails() {
        try {
            return characterDAO.getCharactersWithDetails();
        } catch (SQLException e) {
            handleError("Error loading characters", e);
            return List.of();
        }
    }

    public boolean createCharacter(Character character) {
        try {
            return characterDAO.insertCharacter(character);
        } catch (SQLException e) {
            handleError("Error creating character", e);
            return false;
        }
    }

    public boolean updateCharacter(Character character) {
        try {
            return characterDAO.updateCharacter(character);
        } catch (SQLException e) {
            handleError("Error updating character", e);
            return false;
        }
    }

    public boolean deleteCharacter(String characterId) {
        try {
            return characterDAO.deleteCharacter(characterId);
        } catch (SQLException e) {
            handleError("Error deleting character", e);
            return false;
        }
    }

    public List<Character> getCharactersByPlayer(int playerId) {
        try {
            return characterDAO.getCharactersByPlayer(playerId);
        } catch (SQLException e) {
            handleError("Error loading characters for player", e);
            return List.of();
        }
    }

    // Player operations
    public List<Player> getAllPlayers() {
        try {
            return playerDAO.getAllPlayers();
        } catch (SQLException e) {
            handleError("Error loading players", e);
            return List.of();
        }
    }

    public boolean createPlayer(Player player) {
        try {
            if (player.getPlayerId() == 0) {
                player.setPlayerId(playerDAO.getNextPlayerId());
            }
            return playerDAO.insertPlayer(player);
        } catch (SQLException e) {
            handleError("Error creating player", e);
            return false;
        }
    }

    // Campaign operations
    public List<Campaign> getAllCampaigns() {
        try {
            return campaignDAO.getAllCampaigns();
        } catch (SQLException e) {
            handleError("Error loading campaigns", e);
            return List.of();
        }
    }

    // Lookup data operations
    public List<String> getClasses() {
        try {
            return lookupDAO.getClasses();
        } catch (SQLException e) {
            handleError("Error loading classes", e);
            return List.of();
        }
    }

    public List<String> getSubclasses() {
        try {
            return lookupDAO.getSubclasses();
        } catch (SQLException e) {
            handleError("Error loading subclasses", e);
            return List.of();
        }
    }

    public List<String> getSubclassesByClass(String classId) {
        try {
            return lookupDAO.getSubclassesByClass(classId);
        } catch (SQLException e) {
            handleError("Error loading subclasses for class: " + classId, e);
            return List.of();
        }
    }

    public List<String> getSpecies() {
        try {
            return lookupDAO.getSpecies();
        } catch (SQLException e) {
            handleError("Error loading species", e);
            return List.of();
        }
    }

    public List<String> getSubspecies() {
        try {
            return lookupDAO.getSubspecies();
        } catch (SQLException e) {
            handleError("Error loading subspecies", e);
            return List.of();
        }
    }

    public List<String> getSubspeciesBySpecies(String speciesId) {
        try {
            return lookupDAO.getSubspeciesBySpecies(speciesId);
        } catch (SQLException e) {
            handleError("Error loading subspecies for species: " + speciesId, e);
            return List.of();
        }
    }

    public List<String> getBackgrounds() {
        try {
            return lookupDAO.getBackgrounds();
        } catch (SQLException e) {
            handleError("Error loading backgrounds", e);
            return List.of();
        }
    }

    public List<DnDClass> getAllClasses() {
        try {
            return lookupDAO.getAllClasses();
        } catch (SQLException e) {
            handleError("Error loading classes", e);
            return List.of();
        }
    }

    public List<Species> getAllSpecies() {
        try {
            return lookupDAO.getAllSpecies();
        } catch (SQLException e) {
            handleError("Error loading species", e);
            return List.of();
        }
    }

    // Report operations - only the 10 from provided SQL queries
    public List<Map<String, Object>> getCharactersByClassAndCampaign() {
        try {
            return reportDAO.getCharactersByClassAndCampaign();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getClassesWithMostSubclasses() {
        try {
            return reportDAO.getClassesWithMostSubclasses();
        } catch (SQLException e) {
            handleError("Error generating classes with most subclasses report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getAboveAverageLevelBySpecies() {
        try {
            return reportDAO.getAboveAverageLevelBySpecies();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getAllPlayersAndCharacters() {
        try {
            return reportDAO.getAllPlayersAndCharacters();
        } catch (SQLException e) {
            handleError("Error generating all players and characters report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getPopularSettingsAndMilitary() {
        try {
            return reportDAO.getPopularSettingsAndMilitary();
        } catch (SQLException e) {
            handleError("Error generating popular settings and military report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getCharacterSpeciesAndSize() {
        try {
            return reportDAO.getCharacterSpeciesAndSize();
        } catch (SQLException e) {
            handleError("Error generating character species and size report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getPlayerCharacterCounts() {
        try {
            return reportDAO.getPlayerCharacterCounts();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getCampaignParticipation() {
        try {
            return reportDAO.getCampaignParticipation();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getClassDistribution() {
        try {
            return reportDAO.getClassDistribution();
        } catch (SQLException e) {
            handleError("Error generating class distribution report", e);
            return List.of();
        }
    }

    public List<Map<String, Object>> getCharacterAbilityModifiers() {
        try {
            return reportDAO.getCharacterAbilityModifiers();
        } catch (SQLException e) {
            handleError("Error generating report", e);
            return List.of();
        }
    }

    // Application lifecycle
    public void shutdown() {
        try {
            DatabaseConnection.getInstance().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(mainView,
                    message + ": " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    // Main method
    public static void main(String[] args) {
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