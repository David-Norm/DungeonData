/**
 * Data Access Object for Campaign entities.
 * Handles database operations for campaign data.
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

public class CampaignDAO {
    private Connection myConnection;

    /**
     * Constructs a CampaignDAO and establishes database connection.
     *
     * @throws SQLException if database connection fails
     */
    public CampaignDAO() throws SQLException {
        myConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Retrieves all campaigns from the database.
     *
     * @return list of all campaigns ordered by game ID
     * @throws SQLException if database query fails
     */
    public List<Campaign> getAllCampaigns() throws SQLException {
        List<Campaign> campaigns = new ArrayList<>();
        String query = "SELECT game_id, setting, synopsis, meeting_time, max_players FROM game ORDER BY game_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Campaign campaign = new Campaign(
                        rs.getString("game_id"),
                        rs.getString("setting"),
                        rs.getString("synopsis"),
                        rs.getTimestamp("meeting_time"),
                        rs.getInt("max_players")
                );
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }
}
