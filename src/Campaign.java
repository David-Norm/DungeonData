/**
 * Represents a D&D campaign with game details including setting, synopsis,
 * and meeting time.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class Campaign {
    private String myGameId;
    private String mySetting;
    private String mySynopsis;
    private java.sql.Timestamp myMeetingTime;

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
     */
    public Campaign(String theGameId, String theSetting, String theSynopsis,
                    java.sql.Timestamp theMeetingTime) {
        myGameId = theGameId;
        mySetting = theSetting;
        mySynopsis = theSynopsis;
        myMeetingTime = theMeetingTime;
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
     * Returns a string representation of the campaign.
     *
     * @return a formatted string showing game ID and setting
     */
    @Override
    public String toString() {
        return myGameId + " (" + mySetting + ")";
    }
}