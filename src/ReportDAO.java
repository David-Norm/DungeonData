import java.sql.*;
import java.util.*;

/**
 * Data Access Object for generating reports.
 * Contains methods for executing complex queries for reporting purposes.
 *
 * @author David Norman
 * @version Summer 2025
 */
public class ReportDAO {
    private Connection myConnection;

    /**
     * Constructs a ReportDAO and establishes database connection.
     *
     * @throws SQLException if database connection fails
     */
    public ReportDAO() throws SQLException {
        myConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Gets characters grouped by class/subclass and campaign.
     *
     * @return list of character-class-campaign data
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getCharactersByClassAndCampaign() throws SQLException {
        String query = """
            SELECT c.char_id AS character_name, cl.class_id AS class, sc.subclass_id AS subclass, g.game_id AS campaign
            FROM characters c
            JOIN subclass sc ON c.subclass_id = sc.subclass_id
            JOIN class cl ON sc.class_id = cl.class_id
            JOIN game g ON c.game_id = g.game_id
            ORDER BY cl.class_id, c.char_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets classes with the most subclasses.
     *
     * @return list of classes and their subclass counts
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getClassesWithMostSubclasses() throws SQLException {
        String query = """
            SELECT cl.class_id, COUNT(sc.subclass_id) AS subclass_count
            FROM class cl
            JOIN subclass sc ON cl.class_id = sc.class_id
            WHERE cl.class_id IN (
                SELECT cl2.class_id
                FROM class cl2
                JOIN subclass sc2 ON cl2.class_id = sc2.class_id
                GROUP BY cl2.class_id
                HAVING COUNT(sc2.subclass_id) > ANY (
                    SELECT COUNT(c.char_id)
                    FROM characters c
                    JOIN subclass sc3 ON c.subclass_id = sc3.subclass_id
                    GROUP BY sc3.class_id
                )
            )
            GROUP BY cl.class_id
            ORDER BY subclass_count DESC
            """;
        return executeQuery(query);
    }

    /**
     * Gets characters with above-average level for their species.
     *
     * @return list of characters above species average level
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getAboveAverageLevelBySpecies() throws SQLException {
        String query = """
            SELECT c1.char_id, c1.lvl, sp.species_id
            FROM characters c1
            JOIN subspecies ss ON c1.subspecies_id = ss.subspecies_id
            JOIN species sp ON ss.species_id = sp.species_id
            WHERE c1.lvl > (
                SELECT AVG(c2.lvl)
                FROM characters c2
                JOIN subspecies ss2 ON c2.subspecies_id = ss2.subspecies_id
                JOIN species sp2 ON ss2.species_id = sp2.species_id
                WHERE sp2.species_id = sp.species_id
            )
            ORDER BY sp.species_id, c1.lvl DESC
            """;
        return executeQuery(query);
    }

    /**
     * Gets all players and their characters.
     *
     * @return list of player-character relationships
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getAllPlayersAndCharacters() throws SQLException {
        String query = """
            SELECT p.player_id, p.fname, c.char_id
            FROM player p
            LEFT JOIN characters c ON p.player_id = c.player_id
            UNION
            SELECT p.player_id, p.fname, c.char_id
            FROM player p
            RIGHT JOIN characters c ON p.player_id = c.player_id
            ORDER BY player_id, char_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets characters in popular settings or with military backgrounds.
     *
     * @return list of characters matching criteria
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getPopularSettingsAndMilitary() throws SQLException {
        String query = """
            SELECT c.char_id, 'Popular Setting' as reason, g.setting
            FROM characters c
            JOIN game g ON c.game_id = g.game_id
            WHERE g.setting IN ('Forgotten Realms', 'Eberron', 'Dragonlance')
            UNION
            SELECT c.char_id, 'Military Background' as reason, c.bg_id
            FROM characters c
            WHERE c.bg_id = 'Soldier'
            ORDER BY char_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets character species and size information.
     *
     * @return list of character-species-size data
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getCharacterSpeciesAndSize() throws SQLException {
        String query = """
            SELECT c.char_id, sp.species_id, sp.species_size
            FROM characters c
            JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            JOIN species sp ON ss.species_id = sp.species_id
            ORDER BY sp.species_size, sp.species_id, c.char_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets player character counts.
     *
     * @return list of players and their character counts
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getPlayerCharacterCounts() throws SQLException {
        String query = """
            SELECT p.player_id, p.fname, COUNT(c.char_id) AS character_count
            FROM player p
            LEFT JOIN characters c ON p.player_id = c.player_id
            GROUP BY p.player_id, p.fname
            ORDER BY character_count DESC, p.fname
            """;
        return executeQuery(query);
    }

    /**
     * Gets campaign participation statistics.
     *
     * @return list of campaigns and their player counts
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getCampaignParticipation() throws SQLException {
        String query = """
            SELECT g.game_id, g.setting, COUNT(DISTINCT c.player_id) AS num_players
            FROM game g
            LEFT JOIN characters c ON g.game_id = c.game_id
            GROUP BY g.game_id, g.setting
            ORDER BY num_players DESC, g.game_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets class distribution statistics.
     *
     * @return list of classes and their usage statistics
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getClassDistribution() throws SQLException {
        String query = """
            SELECT cl.class_id, COUNT(c.char_id) AS character_count,
                   ROUND(COUNT(c.char_id) * 100.0 / (SELECT COUNT(*) FROM characters), 2) AS percentage
            FROM class cl
            LEFT JOIN subclass sc ON cl.class_id = sc.class_id
            LEFT JOIN characters c ON sc.subclass_id = c.subclass_id
            GROUP BY cl.class_id
            ORDER BY character_count DESC, cl.class_id
            """;
        return executeQuery(query);
    }

    /**
     * Gets character ability modifiers.
     *
     * @return list of characters with calculated ability modifiers
     * @throws SQLException if database query fails
     */
    public List<Map<String, Object>> getCharacterAbilityModifiers() throws SQLException {
        String query = """
            SELECT
              c.char_id AS name,
              FLOOR((c.s_str - 10) / 2) AS str_mod,
              FLOOR((c.s_dex - 10) / 2) AS dex_mod,
              FLOOR((c.s_con - 10) / 2) AS con_mod,
              FLOOR((c.s_int - 10) / 2) AS int_mod,
              FLOOR((c.s_wis - 10) / 2) AS wis_mod,
              FLOOR((c.s_cha - 10) / 2) AS cha_mod,
              cl.class_id AS class,
              sp.species_id AS species
            FROM characters c
            JOIN subclass sc ON c.subclass_id = sc.subclass_id
            JOIN class cl ON sc.class_id = cl.class_id
            JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            JOIN species sp ON ss.species_id = sp.species_id
            ORDER BY cl.class_id, c.char_id
            """;
        return executeQuery(query);
    }

    /**
     * Executes a query and returns results as a list of maps.
     *
     * @param theQuery the SQL query to execute
     * @return list of result maps
     * @throws SQLException if database query fails
     */
    private List<Map<String, Object>> executeQuery(String theQuery) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = myConnection.prepareStatement(theQuery);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        }
        return results;
    }
}
