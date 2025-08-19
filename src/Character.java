/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class Character {
    private String charId;
    private int level;
    private String classId;        // Added: main class
    private String subclassId;
    private String speciesId;      // Added: main species
    private String subspeciesId;
    private String backgroundId;
    private int playerId;
    private String gameId;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    // Constructors
    public Character() {}

    public Character(String charId, int level, String classId, String subclassId,
                     String speciesId, String subspeciesId, String backgroundId,
                     int playerId, String gameId, int str, int dex, int con,
                     int intel, int wis, int cha) {
        this.charId = charId;
        this.level = level;
        this.classId = classId;
        this.subclassId = subclassId;
        this.speciesId = speciesId;
        this.subspeciesId = subspeciesId;
        this.backgroundId = backgroundId;
        this.playerId = playerId;
        this.gameId = gameId;
        this.strength = str;
        this.dexterity = dex;
        this.constitution = con;
        this.intelligence = intel;
        this.wisdom = wis;
        this.charisma = cha;
    }

    // Getters and Setters
    public String getCharId() { return charId; }
    public void setCharId(String charId) { this.charId = charId; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getSubclassId() { return subclassId; }
    public void setSubclassId(String subclassId) { this.subclassId = subclassId; }

    public String getSpeciesId() { return speciesId; }
    public void setSpeciesId(String speciesId) { this.speciesId = speciesId; }

    public String getSubspeciesId() { return subspeciesId; }
    public void setSubspeciesId(String subspeciesId) { this.subspeciesId = subspeciesId; }

    public String getBackgroundId() { return backgroundId; }
    public void setBackgroundId(String backgroundId) { this.backgroundId = backgroundId; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getConstitution() { return constitution; }
    public void setConstitution(int constitution) { this.constitution = constitution; }

    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public int getWisdom() { return wisdom; }
    public void setWisdom(int wisdom) { this.wisdom = wisdom; }

    public int getCharisma() { return charisma; }
    public void setCharisma(int charisma) { this.charisma = charisma; }

    // Utility methods for D&D calculations
    public int getAbilityModifier(String ability) {
        int score = switch (ability.toLowerCase()) {
            case "str", "strength" -> strength;
            case "dex", "dexterity" -> dexterity;
            case "con", "constitution" -> constitution;
            case "int", "intelligence" -> intelligence;
            case "wis", "wisdom" -> wisdom;
            case "cha", "charisma" -> charisma;
            default -> 10;
        };
        return (score - 10) / 2;
    }

    public String getFormattedAbilityScore(String ability) {
        int score = switch (ability.toLowerCase()) {
            case "str", "strength" -> strength;
            case "dex", "dexterity" -> dexterity;
            case "con", "constitution" -> constitution;
            case "int", "intelligence" -> intelligence;
            case "wis", "wisdom" -> wisdom;
            case "cha", "charisma" -> charisma;
            default -> 10;
        };
        int modifier = (score - 10) / 2;
        String modStr = modifier >= 0 ? "+" + modifier : String.valueOf(modifier);
        return score + " (" + modStr + ")";
    }

    // Helper methods for displaying character info
    public String getFullClass() {
        if (subclassId != null && !subclassId.isEmpty()) {
            return classId + " (" + subclassId + ")";
        }
        return classId;
    }

    public String getFullSpecies() {
        if (subspeciesId != null && !subspeciesId.isEmpty()) {
            return speciesId + " (" + subspeciesId + ")";
        }
        return speciesId;
    }

    @Override
    public String toString() {
        return charId + " (Level " + level + " " + getFullClass() + ")";
    }
}