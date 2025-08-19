/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class Campaign {
    private String gameId;
    private String setting;
    private String synopsis;
    private java.sql.Timestamp meetingTime;
    private int maxPlayers;

    // Constructors
    public Campaign() {}

    public Campaign(String gameId, String setting, String synopsis,
                    java.sql.Timestamp meetingTime, int maxPlayers) {
        this.gameId = gameId;
        this.setting = setting;
        this.synopsis = synopsis;
        this.meetingTime = meetingTime;
        this.maxPlayers = maxPlayers;
    }

    // Getters and Setters
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getSetting() { return setting; }
    public void setSetting(String setting) { this.setting = setting; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public java.sql.Timestamp getMeetingTime() { return meetingTime; }
    public void setMeetingTime(java.sql.Timestamp meetingTime) { this.meetingTime = meetingTime; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    @Override
    public String toString() {
        return gameId + " (" + setting + ")";
    }
}
