/**
 * Data Access Object for Player entities.
 * Handles database operations for player data.
 *
 * @author David Norman
 * @version Summer 2025
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private Connection myConnection;

    /**
     * Constructs a PlayerDAO and establishes database connection.
     *
     * @throws SQLException if database connection fails
     */
    public PlayerDAO() throws SQLException {
        myConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Retrieves all players from the database.
     *
     * @return list of all players ordered by first name
     * @throws SQLException if database query fails
     */
    public List<Player> getAllPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();
        String query = "SELECT player_id, fname, lname, pref_contact, contact_info, time_zone FROM player ORDER BY fname";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Player player = new Player(
                        rs.getInt("player_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("pref_contact"),
                        rs.getString("contact_info"),
                        rs.getString("time_zone")
                );
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Inserts a new player into the database.
     *
     * @param thePlayer the player to insert
     * @return true if insertion was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean insertPlayer(Player thePlayer) throws SQLException {
        String query = """
            INSERT INTO player (player_id, fname, lname, pref_contact, contact_info, time_zone)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setInt(1, thePlayer.getPlayerId());
            stmt.setString(2, thePlayer.getFirstName());
            stmt.setString(3, thePlayer.getLastName());
            stmt.setString(4, thePlayer.getPreferredContact());
            stmt.setString(5, thePlayer.getContactInfo());
            stmt.setString(6, thePlayer.getTimeZone());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Gets the next available player ID.
     *
     * @return the next player ID to use
     * @throws SQLException if database query fails
     */
    public int getNextPlayerId() throws SQLException {
        String query = "SELECT COALESCE(MAX(player_id), 0) + 1 FROM player";
        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;
        }
    }
}