/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class Species {
    private String speciesId;
    private String speciesSize;
    private String speciesSummary;

    // Constructors
    public Species() {}

    public Species(String speciesId, String speciesSize, String speciesSummary) {
        this.speciesId = speciesId;
        this.speciesSize = speciesSize;
        this.speciesSummary = speciesSummary;
    }

    // Getters and Setters
    public String getSpeciesId() { return speciesId; }
    public void setSpeciesId(String speciesId) { this.speciesId = speciesId; }

    public String getSpeciesSize() { return speciesSize; }
    public void setSpeciesSize(String speciesSize) { this.speciesSize = speciesSize; }

    public String getSpeciesSummary() { return speciesSummary; }
    public void setSpeciesSummary(String speciesSummary) { this.speciesSummary = speciesSummary; }

    @Override
    public String toString() {
        return speciesId + " (" + speciesSize + ")";
    }
}