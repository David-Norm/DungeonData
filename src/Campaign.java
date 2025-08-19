/**
 * Represents a D&D campaign with game details including setting, synopsis,
 * meeting time, and maximum player capacity.
 *
 * @author David Norman
 * @version Summer 2025
 */
public class Campaign {
    private String myGameId;
    private String mySetting;
    private String mySynopsis;
    private java.sql.Timestamp myMeetingTime;
    private int myMaxPlayers;

    /**
     * Default constructor for Campaign.
     */
    public Campaign() {}

    /**
     * Constructs a Campaign with all specified parameters.
     *
     * @param theGameId the unique identifier for the campaign
     * @param theSetting the campaign setting
     * @param theSynopsis the campaign description and story
     * @param theMeetingTime the scheduled meeting time for sessions
     * @param theMaxPlayers the maximum number of players allowed
     */
    public Campaign(String theGameId, String theSetting, String theSynopsis,
                    java.sql.Timestamp theMeetingTime, int theMaxPlayers) {
        myGameId = theGameId;
        mySetting = theSetting;
        mySynopsis = theSynopsis;
        myMeetingTime = theMeetingTime;
        myMaxPlayers = theMaxPlayers;
    }

    /**
     * Gets the unique game identifier.
     *
     * @return the campaign's game ID
     */
    public String getGameId() {
        return myGameId;
    }

    /**
     * Sets the unique game identifier.
     *
     * @param theGameId the new game ID to set
     */
    public void setGameId(String theGameId) {
        myGameId = theGameId;
    }

    /**
     * Gets the campaign setting.
     *
     * @return the campaign setting
     */
    public String getSetting() {
        return mySetting;
    }

    /**
     * Sets the campaign setting.
     *
     * @param theSetting the new setting to set
     */
    public void setSetting(String theSetting) {
        mySetting = theSetting;
    }

    /**
     * Gets the campaign synopsis.
     *
     * @return the campaign synopsis/description
     */
    public String getSynopsis() {
        return mySynopsis;
    }

    /**
     * Sets the campaign synopsis.
     *
     * @param theSynopsis the new synopsis to set
     */
    public void setSynopsis(String theSynopsis) {
        mySynopsis = theSynopsis;
    }

    /**
     * Gets the scheduled meeting time.
     *
     * @return the meeting time as a Timestamp
     */
    public java.sql.Timestamp getMeetingTime() {
        return myMeetingTime;
    }

    /**
     * Sets the scheduled meeting time.
     *
     * @param theMeetingTime the new meeting time to set
     */
    public void setMeetingTime(java.sql.Timestamp theMeetingTime) {
        myMeetingTime = theMeetingTime;
    }

    /**
     * Gets the maximum number of players allowed.
     *
     * @return the maximum player count
     */
    public int getMaxPlayers() {
        return myMaxPlayers;
    }

    /**
     * Sets the maximum number of players allowed.
     *
     * @param theMaxPlayers the new maximum player count to set
     */
    public void setMaxPlayers(int theMaxPlayers) {
        myMaxPlayers = theMaxPlayers;
    }

    /**
     * Returns a string representation of the campaign.
     *
     * @return a formatted string showing game ID and setting
     */
    @Override
    public String toString() {
        return myGameId + " (" + mySetting + ")";
    }
}