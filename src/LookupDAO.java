import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LookupDAO {
    private Connection connection;

    public LookupDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<String> getSubclasses() throws SQLException {
        List<String> subclasses = new ArrayList<>();
        String query = "SELECT subclass_id FROM subclass ORDER BY subclass_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subclasses.add(rs.getString("subclass_id"));
            }
        }
        return subclasses;
    }

    public List<String> getSubspecies() throws SQLException {
        List<String> subspecies = new ArrayList<>();
        String query = "SELECT subspecies_id FROM subspecies ORDER BY subspecies_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subspecies.add(rs.getString("subspecies_id"));
            }
        }
        return subspecies;
    }

    public List<String> getBackgrounds() throws SQLException {
        List<String> backgrounds = new ArrayList<>();
        String query = "SELECT bg_id FROM background ORDER BY bg_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                backgrounds.add(rs.getString("bg_id"));
            }
        }
        return backgrounds;
    }

    public List<DnDClass> getAllClasses() throws SQLException {
        List<DnDClass> classes = new ArrayList<>();
        String query = "SELECT class_id, class_summary, casting_stat, primary_stat, secondary_stat FROM class ORDER BY class_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
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

    public List<Species> getAllSpecies() throws SQLException {
        List<Species> species = new ArrayList<>();
        String query = "SELECT species_id, species_size, species_summary FROM species ORDER BY species_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
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