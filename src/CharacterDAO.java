import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterDAO {
    private Connection connection;

    public CharacterDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Character> getAllCharacters() throws SQLException {
        List<Character> characters = new ArrayList<>();
        String query = """
            SELECT c.char_id, c.lvl, c.subclass_id, c.subspecies_id, c.bg_id, 
                   c.player_id, c.game_id, c.s_str, c.s_dex, c.s_con, 
                   c.s_int, c.s_wis, c.s_cha,
                   sc.class_id, ss.species_id
            FROM characters c
            LEFT JOIN subclass sc ON c.subclass_id = sc.subclass_id
            LEFT JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            ORDER BY c.char_id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Character character = new Character(
                        rs.getString("char_id"),
                        rs.getInt("lvl"),
                        rs.getString("class_id"),
                        rs.getString("subclass_id"),
                        rs.getString("species_id"),
                        rs.getString("subspecies_id"),
                        rs.getString("bg_id"),
                        rs.getInt("player_id"),
                        rs.getString("game_id"),
                        rs.getInt("s_str"),
                        rs.getInt("s_dex"),
                        rs.getInt("s_con"),
                        rs.getInt("s_int"),
                        rs.getInt("s_wis"),
                        rs.getInt("s_cha")
                );
                characters.add(character);
            }
        }
        return characters;
    }

    public List<Map<String, Object>> getCharactersWithDetails() throws SQLException {
        String query = """
            SELECT c.char_id, c.lvl, sc.subclass_id, ss.subspecies_id, 
                   c.bg_id, p.fname, p.lname, g.game_id,
                   c.s_str, c.s_dex, c.s_con, c.s_int, c.s_wis, c.s_cha,
                   cl.class_id, sp.species_id
            FROM characters c
            LEFT JOIN player p ON c.player_id = p.player_id
            LEFT JOIN game g ON c.game_id = g.game_id
            LEFT JOIN subclass sc ON c.subclass_id = sc.subclass_id
            LEFT JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            LEFT JOIN class cl ON sc.class_id = cl.class_id
            LEFT JOIN species sp ON ss.species_id = sp.species_id
            ORDER BY c.char_id
            """;

        return executeQuery(query);
    }

    public boolean insertCharacter(Character character) throws SQLException {
        String query = """
            INSERT INTO characters (char_id, lvl, subclass_id, subspecies_id, bg_id, 
                                  player_id, game_id, s_str, s_dex, s_con, s_int, s_wis, s_cha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, character.getCharId());
            stmt.setInt(2, character.getLevel());
            stmt.setString(3, character.getSubclassId());
            stmt.setString(4, character.getSubspeciesId());
            stmt.setString(5, character.getBackgroundId());
            stmt.setInt(6, character.getPlayerId());
            stmt.setString(7, character.getGameId());
            stmt.setInt(8, character.getStrength());
            stmt.setInt(9, character.getDexterity());
            stmt.setInt(10, character.getConstitution());
            stmt.setInt(11, character.getIntelligence());
            stmt.setInt(12, character.getWisdom());
            stmt.setInt(13, character.getCharisma());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateCharacter(Character character) throws SQLException {
        String query = """
            UPDATE characters 
            SET lvl = ?, subclass_id = ?, subspecies_id = ?, bg_id = ?, 
                player_id = ?, game_id = ?, s_str = ?, s_dex = ?, s_con = ?, 
                s_int = ?, s_wis = ?, s_cha = ?
            WHERE char_id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, character.getLevel());
            stmt.setString(2, character.getSubclassId());
            stmt.setString(3, character.getSubspeciesId());
            stmt.setString(4, character.getBackgroundId());
            stmt.setInt(5, character.getPlayerId());
            stmt.setString(6, character.getGameId());
            stmt.setInt(7, character.getStrength());
            stmt.setInt(8, character.getDexterity());
            stmt.setInt(9, character.getConstitution());
            stmt.setInt(10, character.getIntelligence());
            stmt.setInt(11, character.getWisdom());
            stmt.setInt(12, character.getCharisma());
            stmt.setString(13, character.getCharId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCharacter(String characterId) throws SQLException {
        String query = "DELETE FROM characters WHERE char_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, characterId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Character> getCharactersByPlayer(int playerId) throws SQLException {
        List<Character> characters = new ArrayList<>();
        String query = """
            SELECT c.char_id, c.lvl, c.subclass_id, c.subspecies_id, c.bg_id, 
                   c.player_id, c.game_id, c.s_str, c.s_dex, c.s_con, c.s_int, c.s_wis, c.s_cha,
                   sc.class_id, ss.species_id
            FROM characters c
            LEFT JOIN subclass sc ON c.subclass_id = sc.subclass_id
            LEFT JOIN subspecies ss ON c.subspecies_id = ss.subspecies_id
            WHERE c.player_id = ?
            ORDER BY c.char_id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Character character = new Character(
                            rs.getString("char_id"),
                            rs.getInt("lvl"),
                            rs.getString("class_id"),
                            rs.getString("subclass_id"),
                            rs.getString("species_id"),
                            rs.getString("subspecies_id"),
                            rs.getString("bg_id"),
                            rs.getInt("player_id"),
                            rs.getString("game_id"),
                            rs.getInt("s_str"),
                            rs.getInt("s_dex"),
                            rs.getInt("s_con"),
                            rs.getInt("s_int"),
                            rs.getInt("s_wis"),
                            rs.getInt("s_cha")
                    );
                    characters.add(character);
                }
            }
        }
        return characters;
    }

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