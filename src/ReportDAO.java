import java.sql.*;
import java.util.*;

// ReportDAO.java - Data Access Object for the 10 provided SQL queries only
public class ReportDAO {
    private Connection connection;

    public ReportDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // SQL Query 1 - Which characters belong to which class/subclass and which campaign they're in
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

    // SQL Query 2 - List classes that have more subclasses than any other class has characters
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

    // SQL Query 3 - Find characters whose level is above average of other characters in the same species
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

    // SQL Query 4 - Shows all players and all characters
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

    // SQL Query 5 - Find all characters with common settings or characters with Soldier background
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

    // SQL Query 6 - List each character with their species and size
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

    // SQL Query 7 - Shows how many characters each player controls
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

    // SQL Query 8 - List all campaigns and how many unique players are participating in each
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

    // SQL Query 9 - Class Distribution Analysis
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

    // SQL Query 10 - Show characters and their ability modifiers
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

    // Helper method for executing String queries
    private List<Map<String, Object>> executeQuery(String query) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
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