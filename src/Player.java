/**
 * Represents a player in the D&D system with contact information
 * and preferences for communication and scheduling.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class Player {
    private int myPlayerId;
    private String myFirstName;
    private String myLastName;
    private String myPreferredContact;
    private String myContactInfo;
    private String myTimeZone;

    /**
     * Default constructor for Player.
     */
    public Player() {}

    /**
     * Constructs a Player with all specified parameters.
     *
     * @param thePlayerId the unique player identifier
     * @param theFirstName the player's first name
     * @param theLastName the player's last name
     * @param thePreferredContact the preferred contact method
     * @param theContactInfo the contact information
     * @param theTimeZone the player's time zone
     */
    public Player(int thePlayerId, String theFirstName, String theLastName,
                  String thePreferredContact, String theContactInfo, String theTimeZone) {
        myPlayerId = thePlayerId;
        myFirstName = theFirstName;
        myLastName = theLastName;
        myPreferredContact = thePreferredContact;
        myContactInfo = theContactInfo;
        myTimeZone = theTimeZone;
    }

    /**
     * Gets the unique player identifier.
     *
     * @return the player ID
     */
    public int getPlayerId() {
        return myPlayerId;
    }

    /**
     * Sets the unique player identifier.
     *
     * @param thePlayerId the new player ID to set
     */
    public void setPlayerId(int thePlayerId) {
        myPlayerId = thePlayerId;
    }

    /**
     * Gets the player's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return myFirstName;
    }

    /**
     * Sets the player's first name.
     *
     * @param theFirstName the new first name to set
     */
    public void setFirstName(String theFirstName) {
        myFirstName = theFirstName;
    }

    /**
     * Gets the player's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return myLastName;
    }

    /**
     * Sets the player's last name.
     *
     * @param theLastName the new last name to set
     */
    public void setLastName(String theLastName) {
        myLastName = theLastName;
    }

    /**
     * Gets the player's preferred contact method.
     *
     * @return the preferred contact method
     */
    public String getPreferredContact() {
        return myPreferredContact;
    }

    /**
     * Sets the player's preferred contact method.
     *
     * @param thePreferredContact the new preferred contact method to set
     */
    public void setPreferredContact(String thePreferredContact) {
        myPreferredContact = thePreferredContact;
    }

    /**
     * Gets the player's contact information.
     *
     * @return the contact information
     */
    public String getContactInfo() {
        return myContactInfo;
    }

    /**
     * Sets the player's contact information.
     *
     * @param theContactInfo the new contact information to set
     */
    public void setContactInfo(String theContactInfo) {
        myContactInfo = theContactInfo;
    }

    /**
     * Gets the player's time zone.
     *
     * @return the time zone
     */
    public String getTimeZone() {
        return myTimeZone;
    }

    /**
     * Sets the player's time zone.
     *
     * @param theTimeZone the new time zone to set
     */
    public void setTimeZone(String theTimeZone) {
        myTimeZone = theTimeZone;
    }

    /**
     * Gets the player's full name by combining first and last names.
     *
     * @return the full name (first name + last name if present)
     */
    public String getFullName() {
        return myFirstName + (myLastName != null ? " " + myLastName : "");
    }

    /**
     * Returns a string representation of the player.
     *
     * @return formatted string showing full name and player ID
     */
    @Override
    public String toString() {
        return getFullName() + " (ID: " + myPlayerId + ")";
    }
}
