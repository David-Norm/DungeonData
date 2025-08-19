/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class DnDClass {
    private String classId;
    private String classSummary;
    private String castingStat;
    private String primaryStat;
    private String secondaryStat;

    // Constructors
    public DnDClass() {}

    public DnDClass(String classId, String classSummary, String castingStat,
                    String primaryStat, String secondaryStat) {
        this.classId = classId;
        this.classSummary = classSummary;
        this.castingStat = castingStat;
        this.primaryStat = primaryStat;
        this.secondaryStat = secondaryStat;
    }

    // Getters and Setters
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getClassSummary() { return classSummary; }
    public void setClassSummary(String classSummary) { this.classSummary = classSummary; }

    public String getCastingStat() { return castingStat; }
    public void setCastingStat(String castingStat) { this.castingStat = castingStat; }

    public String getPrimaryStat() { return primaryStat; }
    public void setPrimaryStat(String primaryStat) { this.primaryStat = primaryStat; }

    public String getSecondaryStat() { return secondaryStat; }
    public void setSecondaryStat(String secondaryStat) { this.secondaryStat = secondaryStat; }

    public boolean isCaster() {
        return castingStat != null;
    }

    @Override
    public String toString() {
        return classId;
    }
}
