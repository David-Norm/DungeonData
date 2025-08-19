/**
 * Data Access Object for Character entities.
 * Handles database operations for character data including CRUD operations.
 *
 * @author David Norman
 * @version Summer 2025
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterDAO {
    private Connection myConnection;

    /**
     * Constructs a CharacterDAO and establishes database connection.
     *
     * @throws SQLException if database connection fails
     */
    public CharacterDAO() throws SQLException {
        myConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Retrieves all characters from the database.
     *
     * @return list of all characters ordered by character ID
     * @throws SQLException if database query fails
     */
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

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
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

    /**
     * Retrieves detailed character information with related data.
     *
     * @return list of character maps with player and campaign details
     * @throws SQLException if database query fails
     */
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

    /**
     * Inserts a new character into the database.
     *
     * @param theCharacter the character to insert
     * @return true if insertion was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean insertCharacter(Character theCharacter) throws SQLException {
        String query = """
            INSERT INTO characters (char_id, lvl, subclass_id, subspecies_id, bg_id, 
                                  player_id, game_id, s_str, s_dex, s_con, s_int, s_wis, s_cha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setString(1, theCharacter.getCharId());
            stmt.setInt(2, theCharacter.getLevel());
            stmt.setString(3, theCharacter.getSubclassId());
            stmt.setString(4, theCharacter.getSubspeciesId());
            stmt.setString(5, theCharacter.getBackgroundId());
            stmt.setInt(6, theCharacter.getPlayerId());
            stmt.setString(7, theCharacter.getGameId());
            stmt.setInt(8, theCharacter.getStrength());
            stmt.setInt(9, theCharacter.getDexterity());
            stmt.setInt(10, theCharacter.getConstitution());
            stmt.setInt(11, theCharacter.getIntelligence());
            stmt.setInt(12, theCharacter.getWisdom());
            stmt.setInt(13, theCharacter.getCharisma());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing character in the database.
     *
     * @param theCharacter the character with updated information
     * @return true if update was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updateCharacter(Character theCharacter) throws SQLException {
        String query = """
            UPDATE characters 
            SET lvl = ?, subclass_id = ?, subspecies_id = ?, bg_id = ?, 
                player_id = ?, game_id = ?, s_str = ?, s_dex = ?, s_con = ?, 
                s_int = ?, s_wis = ?, s_cha = ?
            WHERE char_id = ?
            """;

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setInt(1, theCharacter.getLevel());
            stmt.setString(2, theCharacter.getSubclassId());
            stmt.setString(3, theCharacter.getSubspeciesId());
            stmt.setString(4, theCharacter.getBackgroundId());
            stmt.setInt(5, theCharacter.getPlayerId());
            stmt.setString(6, theCharacter.getGameId());
            stmt.setInt(7, theCharacter.getStrength());
            stmt.setInt(8, theCharacter.getDexterity());
            stmt.setInt(9, theCharacter.getConstitution());
            stmt.setInt(10, theCharacter.getIntelligence());
            stmt.setInt(11, theCharacter.getWisdom());
            stmt.setInt(12, theCharacter.getCharisma());
            stmt.setString(13, theCharacter.getCharId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a character from the database.
     *
     * @param theCharacterId the ID of the character to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean deleteCharacter(String theCharacterId) throws SQLException {
        String query = "DELETE FROM characters WHERE char_id = ?";
        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setString(1, theCharacterId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all characters belonging to a specific player.
     *
     * @param thePlayerId the ID of the player
     * @return list of characters owned by the player
     * @throws SQLException if database query fails
     */
    public List<Character> getCharactersByPlayer(int thePlayerId) throws SQLException {
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

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setInt(1, thePlayerId);
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