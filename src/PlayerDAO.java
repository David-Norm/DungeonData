import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private Connection connection;

    public PlayerDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Player> getAllPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();
        String query = "SELECT player_id, fname, lname, pref_contact, contact_info, time_zone FROM player ORDER BY fname";

        try (PreparedStatement stmt = connection.prepareStatement(query);
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

    public boolean insertPlayer(Player player) throws SQLException {
        String query = """
            INSERT INTO player (player_id, fname, lname, pref_contact, contact_info, time_zone)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, player.getPlayerId());
            stmt.setString(2, player.getFirstName());
            stmt.setString(3, player.getLastName());
            stmt.setString(4, player.getPreferredContact());
            stmt.setString(5, player.getContactInfo());
            stmt.setString(6, player.getTimeZone());

            return stmt.executeUpdate() > 0;
        }
    }

    public int getNextPlayerId() throws SQLException {
        String query = "SELECT COALESCE(MAX(player_id), 0) + 1 FROM player";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;
        }
    }
}