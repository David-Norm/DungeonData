/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class Player {
    private int playerId;
    private String firstName;
    private String lastName;
    private String preferredContact;
    private String contactInfo;
    private String timeZone;

    // Constructors
    public Player() {}

    public Player(int playerId, String firstName, String lastName,
                  String preferredContact, String contactInfo, String timeZone) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.preferredContact = preferredContact;
        this.contactInfo = contactInfo;
        this.timeZone = timeZone;
    }

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPreferredContact() { return preferredContact; }
    public void setPreferredContact(String preferredContact) { this.preferredContact = preferredContact; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public String getFullName() {
        return firstName + (lastName != null ? " " + lastName : "");
    }

    @Override
    public String toString() {
        return getFullName() + " (ID: " + playerId + ")";
    }
}