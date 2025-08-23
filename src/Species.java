/**
 * Represents a D&D character species/race with size category and description.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class Species {
    private String mySpeciesId;
    private String mySpeciesSize;
    private String mySpeciesSummary;

    /**
     * Default constructor for Species.
     */
    public Species() {}

    /**
     * Constructs a Species with all specified parameters.
     *
     * @param theSpeciesId the unique species identifier
     * @param theSpeciesSize the size category of the species
     * @param theSpeciesSummary the detailed species description
     */
    public Species(String theSpeciesId, String theSpeciesSize, String theSpeciesSummary) {
        mySpeciesId = theSpeciesId;
        mySpeciesSize = theSpeciesSize;
        mySpeciesSummary = theSpeciesSummary;
    }

    /**
     * Gets the unique species identifier.
     *
     * @return the species ID
     */
    public String getSpeciesId() {
        return mySpeciesId;
    }

    /**
     * Sets the unique species identifier.
     *
     * @param theSpeciesId the new species ID to set
     */
    public void setSpeciesId(String theSpeciesId) {
        mySpeciesId = theSpeciesId;
    }

    /**
     * Gets the species size category.
     *
     * @return the species size
     */
    public String getSpeciesSize() {
        return mySpeciesSize;
    }

    /**
     * Sets the species size category.
     *
     * @param theSpeciesSize the new species size to set
     */
    public void setSpeciesSize(String theSpeciesSize) {
        mySpeciesSize = theSpeciesSize;
    }

    /**
     * Gets the species description.
     *
     * @return the species summary
     */
    public String getSpeciesSummary() {
        return mySpeciesSummary;
    }

    /**
     * Sets the species description.
     *
     * @param theSpeciesSummary the new species summary to set
     */
    public void setSpeciesSummary(String theSpeciesSummary) {
        mySpeciesSummary = theSpeciesSummary;
    }

    /**
     * Returns a string representation of the species.
     *
     * @return formatted string showing species ID and size
     */
    @Override
    public String toString() {
        return mySpeciesId + " (" + mySpeciesSize + ")";
    }
}