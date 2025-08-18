import java.sql.*;
import java.util.*;

// ReportDAO.java - Complete Data Access Object for reports and complex queries
public class ReportDAO {
    private Connection connection;

    public ReportDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

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

    public List<Map<String, Object>> getAllPlayersAndCharacters() throws SQLException {
        String query = """
            SELECT p.player_id, p.fname, COALESCE(c.char_id, 'No Characters') as char_id
            FROM player p
            LEFT JOIN characters c ON p.player_id = c.player_id
            ORDER BY p.player_id, c.char_id
            """;
        return executeQuery(query);
    }

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

    public List<Map<String, Object>> getCharacterFullDetails() throws SQLException {
        String query = """
            SELECT c.char_id, cl.class_id, sp.species_id, b.bg_id, c.lvl
            FROM characters c
            JOIN subclass sc ON c.subclass_id = sc.subclass_id
            JOIN class cl ON sc.class_id = cl.class_id
            JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            JOIN species sp ON ss.species_id = sp.species_id
            JOIN background b ON c.bg_id = b.bg_id
            ORDER BY cl.class_id, c.char_id
            """;
        return executeQuery(query);
    }

    public List<Map<String, Object>> getClassDistribution() throws SQLException {
        String query = """
            SELECT cl.class_id, COUNT(c.char_id) AS character_count,
                   ROUND(COUNT(c.char_id) * 100.0 / (SELECT COUNT(*) FROM characters), 2) AS percentage
            FROM class cl
            LEFT JOIN subclass sc ON cl.class_id = sc.class_id
            LEFT JOIN characters c ON sc.subclass_id = c.subclass_id
            GROUP BY cl.class_id
            ORDER BY character_count DESC
            """;
        return executeQuery(query);
    }

    public List<Map<String, Object>> getSpeciesSizeAnalysis() throws SQLException {
        String query = """
            SELECT sp.species_size, COUNT(c.char_id) AS character_count,
                   GROUP_CONCAT(DISTINCT sp.species_id ORDER BY sp.species_id) AS species_list
            FROM species sp
            LEFT JOIN subspecies ss ON sp.species_id = ss.species_id
            LEFT JOIN characters c ON ss.subspecies_id = c.subspecies_id
            GROUP BY sp.species_size
            ORDER BY character_count DESC
            """;
        return executeQuery(query);
    }

    public List<Map<String, Object>> getBackgroundPopularity() throws SQLException {
        String query = """
            SELECT b.bg_id AS background, COUNT(c.char_id) AS usage_count,
                   ROUND(COUNT(c.char_id) * 100.0 / (SELECT COUNT(*) FROM characters), 2) AS percentage
            FROM background b
            LEFT JOIN characters c ON b.bg_id = c.bg_id
            GROUP BY b.bg_id
            ORDER BY usage_count DESC
            """;
        return executeQuery(query);
    }

    public List<Map<String, Object>> getLevelDistribution() throws SQLException {
        String query = """
            SELECT c.lvl AS level, COUNT(*) AS character_count,
                   ROUND(AVG(c.s_str + c.s_dex + c.s_con + c.s_int + c.s_wis + c.s_cha), 2) AS avg_total_stats
            FROM characters c
            GROUP BY c.lvl
            ORDER BY c.lvl
            """;
        return executeQuery(query);
    }

    public List<Map<String, Object>> getCampaignSettingsReport() throws SQLException {
        String query = """
            SELECT g.setting, COUNT(DISTINCT g.game_id) AS campaign_count,
                   COUNT(DISTINCT c.char_id) AS total_characters,
                   COUNT(DISTINCT c.player_id) AS unique_players,
                   ROUND(AVG(c.lvl), 2) AS average_level
            FROM game g
            LEFT JOIN characters c ON g.game_id = c.game_id
            GROUP BY g.setting
            ORDER BY campaign_count DESC, total_characters DESC
            """;
        return executeQuery(query);
    }

    public Map<String, Object> getCampaignStatistics(String campaignId) throws SQLException {
        String query = """
            SELECT 
                COUNT(DISTINCT c.char_id) as character_count,
                COUNT(DISTINCT c.player_id) as player_count,
                AVG(c.lvl) as average_level,
                MIN(c.lvl) as min_level,
                MAX(c.lvl) as max_level,
                g.setting,
                g.synopsis,
                g.meeting_time,
                g.max_players
            FROM characters c
            RIGHT JOIN game g ON c.game_id = g.game_id
            WHERE g.game_id = ?
            GROUP BY g.game_id, g.setting, g.synopsis, g.meeting_time, g.max_players
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, campaignId);
            List<Map<String, Object>> results = executeQuery(stmt);
            return results.isEmpty() ? new HashMap<>() : results.get(0);
        }
    }

    // Helper method for executing PreparedStatement queries
    private List<Map<String, Object>> executeQuery(PreparedStatement stmt) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (ResultSet rs = stmt.executeQuery()) {
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