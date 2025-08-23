/**
 * Represents a Dungeons & Dragons character class.
 * Stores key attributes including the class identifier, summary,
 * casting ability (if applicable), primary ability, and secondary ability.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class DnDClass {
    private String myClassId;
    private String myClassSummary;
    private String myCastingStat;
    private String myPrimaryStat;
    private String mySecondaryStat;

    /**
     * Constructs an empty DnDClass.
     * All fields default to null until set with setters.
     */
    public DnDClass() {}

    /**
     * Constructs a {@code DnDClass} with the specified attributes.
     *
     * @param theClassId the unique identifier for the class
     * @param theClassSummary a short description of the class
     * @param theCastingStat the ability score used for spellcasting (could be null for non-casters)
     * @param thePrimaryStat the primary ability score for the class
     * @param theSecondaryStat the secondary ability score for the class
     */
    public DnDClass(String theClassId, String theClassSummary, String theCastingStat,
                    String thePrimaryStat, String theSecondaryStat) {
        myClassId = theClassId;
        myClassSummary = theClassSummary;
        myCastingStat = theCastingStat;
        myPrimaryStat = thePrimaryStat;
        mySecondaryStat = theSecondaryStat;
    }

    /**
     * Gets the unique identifier for this class.
     *
     * @return the class ID
     */
    public String getClassId() {
        return myClassId;
    }

    /**
     * Sets the unique identifier for this class.
     *
     * @param theClassId the class ID
     */
    public void setClassId(String theClassId) {
        myClassId = theClassId;
    }

    /**
     * Gets the summary description of this class.
     *
     * @return the class summary
     */
    public String getClassSummary() {
        return myClassSummary;
    }

    /**
     * Sets the summary description of this class.
     *
     * @param theClassSummary the class summary
     */
    public void setClassSummary(String theClassSummary) {
        myClassSummary = theClassSummary;
    }

    /**
     * Gets the casting ability score for this class.
     *
     * @return the casting stat, or null if not a caster
     */
    public String getCastingStat() {
        return myCastingStat;
    }

    /**
     * Sets the casting ability score for this class.
     *
     * @param theCastingStat the casting stat (could be null for non-casters)
     */
    public void setCastingStat(String theCastingStat) {
        myCastingStat = theCastingStat;
    }

    /**
     * Gets the primary ability score for this class.
     *
     * @return the primary stat
     */
    public String getPrimaryStat() {
        return myPrimaryStat;
    }

    /**
     * Sets the primary ability score for this class.
     *
     * @param thePrimaryStat the primary stat
     */
    public void setPrimaryStat(String thePrimaryStat) {
        myPrimaryStat = thePrimaryStat;
    }

    /**
     * Gets the secondary ability score for this class.
     *
     * @return the secondary stat
     */
    public String getSecondaryStat() {
        return mySecondaryStat;
    }

    /**
     * Sets the secondary ability score for this class.
     *
     * @param theSecondaryStat the secondary stat
     */
    public void setSecondaryStat(String theSecondaryStat) {
        mySecondaryStat = theSecondaryStat;
    }

    /**
     * Determines whether this class has a spellcasting statistic.
     *
     * @return true if this class has a casting stat, false otherwise
     */
    public boolean isCaster() {
        return myCastingStat != null;
    }

    /**
     * Returns the string representation of this class.
     * By default, returns the class ID.
     *
     * @return the class ID
     */
    @Override
    public String toString() {
        return myClassId;
    }
}
