import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {
    private Connection connection;

    public CampaignDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Campaign> getAllCampaigns() throws SQLException {
        List<Campaign> campaigns = new ArrayList<>();
        String query = "SELECT game_id, setting, synopsis, meeting_time, max_players FROM game ORDER BY game_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
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