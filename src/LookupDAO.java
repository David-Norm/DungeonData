import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for lookup data including classes, species, and backgrounds.
 * Provides methods to retrieve reference data from the database.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class LookupDAO {
    private Connection myConnection;

    /**
     * Constructs a LookupDAO and establishes database connection.
     *
     * @throws SQLException if database connection fails
     */
    public LookupDAO() throws SQLException {
        myConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Retrieves all class names from the database.
     *
     * @return list of class IDs ordered alphabetically
     * @throws SQLException if database query fails
     */
    public List<String> getClasses() throws SQLException {
        List<String> classes = new ArrayList<>();
        String query = "SELECT class_id FROM class ORDER BY class_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(rs.getString("class_id"));
            }
        }
        return classes;
    }

    /**
     * Retrieves all subclass names from the database.
     *
     * @return list of subclass IDs ordered alphabetically
     * @throws SQLException if database query fails
     */
    public List<String> getSubclasses() throws SQLException {
        List<String> subclasses = new ArrayList<>();
        String query = "SELECT subclass_id FROM subclass ORDER BY subclass_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subclasses.add(rs.getString("subclass_id"));
            }
        }
        return subclasses;
    }

    /**
     * Retrieves subclasses for a specific class.
     *
     * @param theClassId the class to get subclasses for
     * @return list of subclass IDs for the specified class
     * @throws SQLException if database query fails
     */
    public List<String> getSubclassesByClass(String theClassId) throws SQLException {
        List<String> subclasses = new ArrayList<>();
        String query = "SELECT subclass_id FROM subclass WHERE class_id = ? ORDER BY subclass_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setString(1, theClassId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subclasses.add(rs.getString("subclass_id"));
                }
            }
        }
        return subclasses;
    }

    /**
     * Retrieves all species names from the database.
     *
     * @return list of species IDs ordered alphabetically
     * @throws SQLException if database query fails
     */
    public List<String> getSpecies() throws SQLException {
        List<String> species = new ArrayList<>();
        String query = "SELECT species_id FROM species ORDER BY species_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                species.add(rs.getString("species_id"));
            }
        }
        return species;
    }

    /**
     * Retrieves all subspecies names from the database.
     *
     * @return list of subspecies IDs ordered alphabetically
     * @throws SQLException if database query fails
     */
    public List<String> getSubspecies() throws SQLException {
        List<String> subspecies = new ArrayList<>();
        String query = "SELECT subspecies_id FROM subspecies ORDER BY subspecies_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subspecies.add(rs.getString("subspecies_id"));
            }
        }
        return subspecies;
    }

    /**
     * Retrieves subspecies for a specific species.
     *
     * @param theSpeciesId the species to get subspecies for
     * @return list of subspecies IDs for the specified species
     * @throws SQLException if database query fails
     */
    public List<String> getSubspeciesBySpecies(String theSpeciesId) throws SQLException {
        List<String> subspecies = new ArrayList<>();
        String query = "SELECT subspecies_id FROM subspecies WHERE species_id = ? ORDER BY subspecies_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query)) {
            stmt.setString(1, theSpeciesId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subspecies.add(rs.getString("subspecies_id"));
                }
            }
        }
        return subspecies;
    }

    /**
     * Retrieves all background names from the database.
     *
     * @return list of background IDs ordered alphabetically
     * @throws SQLException if database query fails
     */
    public List<String> getBackgrounds() throws SQLException {
        List<String> backgrounds = new ArrayList<>();
        String query = "SELECT bg_id FROM background ORDER BY bg_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                backgrounds.add(rs.getString("bg_id"));
            }
        }
        return backgrounds;
    }

    /**
     * Retrieves all class objects with detailed information.
     *
     * @return list of DnDClass objects ordered by class ID
     * @throws SQLException if database query fails
     */
    public List<DnDClass> getAllClasses() throws SQLException {
        List<DnDClass> classes = new ArrayList<>();
        String query = "SELECT class_id, class_summary, casting_stat, primary_stat, secondary_stat FROM class ORDER BY class_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DnDClass dndClass = new DnDClass(
                        rs.getString("class_id"),
                        rs.getString("class_summary"),
                        rs.getString("casting_stat"),
                        rs.getString("primary_stat"),
                        rs.getString("secondary_stat")
                );
                classes.add(dndClass);
            }
        }
        return classes;
    }

    /**
     * Retrieves all species objects with detailed information.
     *
     * @return list of Species objects ordered by species ID
     * @throws SQLException if database query fails
     */
    public List<Species> getAllSpecies() throws SQLException {
        List<Species> species = new ArrayList<>();
        String query = "SELECT species_id, species_size, species_summary FROM species ORDER BY species_id";

        try (PreparedStatement stmt = myConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Species sp = new Species(
                        rs.getString("species_id"),
                        rs.getString("species_size"),
                        rs.getString("species_summary")
                );
                species.add(sp);
            }
        }
        return species;
    }
}