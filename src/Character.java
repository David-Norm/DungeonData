/**
 * Represents a D&D character with all associated attributes including
 * class, species, background, ability scores, and campaign associations.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class Character {
    private String myCharId;
    private int myLevel;
    private String myClassId;
    private String mySubclassId;
    private String mySpeciesId;
    private String mySubspeciesId;
    private String myBackgroundId;
    private int myPlayerId;
    private String myGameId;
    private int myStrength;
    private int myDexterity;
    private int myConstitution;
    private int myIntelligence;
    private int myWisdom;
    private int myCharisma;

    /**
     * Default constructor for Character.
     */
    public Character() {}

    /**
     * Constructs a Character with all specified parameters.
     *
     * @param theCharId the unique character identifier
     * @param theLevel the character's level (1-20)
     * @param theClassId the character's main class
     * @param theSubclassId the character's subclass
     * @param theSpeciesId the character's main species
     * @param theSubspeciesId the character's subspecies
     * @param theBackgroundId the character's background
     * @param thePlayerId the ID of the owning player
     * @param theGameId the ID of the campaign
     * @param theStr the Strength ability score
     * @param theDex the Dexterity ability score
     * @param theCon the Constitution ability score
     * @param theIntel the Intelligence ability score
     * @param theWis the Wisdom ability score
     * @param theCha the Charisma ability score
     */
    public Character(String theCharId, int theLevel, String theClassId, String theSubclassId,
                     String theSpeciesId, String theSubspeciesId, String theBackgroundId,
                     int thePlayerId, String theGameId, int theStr, int theDex, int theCon,
                     int theIntel, int theWis, int theCha) {
        myCharId = theCharId;
        myLevel = theLevel;
        myClassId = theClassId;
        mySubclassId = theSubclassId;
        mySpeciesId = theSpeciesId;
        mySubspeciesId = theSubspeciesId;
        myBackgroundId = theBackgroundId;
        myPlayerId = thePlayerId;
        myGameId = theGameId;
        myStrength = theStr;
        myDexterity = theDex;
        myConstitution = theCon;
        myIntelligence = theIntel;
        myWisdom = theWis;
        myCharisma = theCha;
    }

    /**
     * Gets the character's unique identifier.
     *
     * @return the character ID
     */
    public String getCharId() {
        return myCharId;
    }

    /**
     * Sets the character's unique identifier.
     *
     * @param theCharId the new character ID to set
     */
    public void setCharId(String theCharId) {
        myCharId = theCharId;
    }

    /**
     * Gets the character's level.
     *
     * @return the character level (1-20)
     */
    public int getLevel() {
        return myLevel;
    }

    /**
     * Sets the character's level.
     *
     * @param theLevel the new level to set (1-20)
     */
    public void setLevel(int theLevel) {
        myLevel = theLevel;
    }

    /**
     * Gets the character's main class.
     *
     * @return the class ID
     */
    public String getClassId() {
        return myClassId;
    }

    /**
     * Sets the character's main class.
     *
     * @param theClassId the new class ID to set
     */
    public void setClassId(String theClassId) {
        myClassId = theClassId;
    }

    /**
     * Gets the character's subclass.
     *
     * @return the subclass ID
     */
    public String getSubclassId() {
        return mySubclassId;
    }

    /**
     * Sets the character's subclass.
     *
     * @param theSubclassId the new subclass ID to set
     */
    public void setSubclassId(String theSubclassId) {
        mySubclassId = theSubclassId;
    }

    /**
     * Gets the character's main species.
     *
     * @return the species ID
     */
    public String getSpeciesId() {
        return mySpeciesId;
    }

    /**
     * Sets the character's main species.
     *
     * @param theSpeciesId the new species ID to set
     */
    public void setSpeciesId(String theSpeciesId) {
        mySpeciesId = theSpeciesId;
    }

    /**
     * Gets the character's subspecies.
     *
     * @return the subspecies ID
     */
    public String getSubspeciesId() {
        return mySubspeciesId;
    }

    /**
     * Sets the character's subspecies.
     *
     * @param theSubspeciesId the new subspecies ID to set
     */
    public void setSubspeciesId(String theSubspeciesId) {
        mySubspeciesId = theSubspeciesId;
    }

    /**
     * Gets the character's background.
     *
     * @return the background ID
     */
    public String getBackgroundId() {
        return myBackgroundId;
    }

    /**
     * Sets the character's background.
     *
     * @param theBackgroundId the new background ID to set
     */
    public void setBackgroundId(String theBackgroundId) {
        myBackgroundId = theBackgroundId;
    }

    /**
     * Gets the ID of the player who owns this character.
     *
     * @return the player ID
     */
    public int getPlayerId() {
        return myPlayerId;
    }

    /**
     * Sets the ID of the player who owns this character.
     *
     * @param thePlayerId the new player ID to set
     */
    public void setPlayerId(int thePlayerId) {
        myPlayerId = thePlayerId;
    }

    /**
     * Gets the ID of the campaign this character belongs to.
     *
     * @return the game/campaign ID
     */
    public String getGameId() {
        return myGameId;
    }

    /**
     * Sets the ID of the campaign this character belongs to.
     *
     * @param theGameId the new game/campaign ID to set
     */
    public void setGameId(String theGameId) {
        myGameId = theGameId;
    }

    /**
     * Gets the character's Strength ability score.
     *
     * @return the Strength score
     */
    public int getStrength() {
        return myStrength;
    }

    /**
     * Sets the character's Strength ability score.
     *
     * @param theStrength the new Strength score to set
     */
    public void setStrength(int theStrength) {
        myStrength = theStrength;
    }

    /**
     * Gets the character's Dexterity ability score.
     *
     * @return the Dexterity score
     */
    public int getDexterity() {
        return myDexterity;
    }

    /**
     * Sets the character's Dexterity ability score.
     *
     * @param theDexterity the new Dexterity score to set
     */
    public void setDexterity(int theDexterity) {
        myDexterity = theDexterity;
    }

    /**
     * Gets the character's Constitution ability score.
     *
     * @return the Constitution score
     */
    public int getConstitution() {
        return myConstitution;
    }

    /**
     * Sets the character's Constitution ability score.
     *
     * @param theConstitution the new Constitution score to set
     */
    public void setConstitution(int theConstitution) {
        myConstitution = theConstitution;
    }

    /**
     * Gets the character's Intelligence ability score.
     *
     * @return the Intelligence score
     */
    public int getIntelligence() {
        return myIntelligence;
    }

    /**
     * Sets the character's Intelligence ability score.
     *
     * @param theIntelligence the new Intelligence score to set
     */
    public void setIntelligence(int theIntelligence) {
        myIntelligence = theIntelligence;
    }

    /**
     * Gets the character's Wisdom ability score.
     *
     * @return the Wisdom score
     */
    public int getWisdom() {
        return myWisdom;
    }

    /**
     * Sets the character's Wisdom ability score.
     *
     * @param theWisdom the new Wisdom score to set
     */
    public void setWisdom(int theWisdom) {
        myWisdom = theWisdom;
    }

    /**
     * Gets the character's Charisma ability score.
     *
     * @return the Charisma score
     */
    public int getCharisma() {
        return myCharisma;
    }

    /**
     * Sets the character's Charisma ability score.
     *
     * @param theCharisma the new Charisma score to set
     */
    public void setCharisma(int theCharisma) {
        myCharisma = theCharisma;
    }

    /**
     * Calculates the ability modifier for a given ability score.
     *
     * @param theAbility the ability name (str/strength, dex/dexterity, etc.)
     * @return the calculated ability modifier
     */
    public int getAbilityModifier(String theAbility) {
        int score = switch (theAbility.toLowerCase()) {
            case "str", "strength" -> myStrength;
            case "dex", "dexterity" -> myDexterity;
            case "con", "constitution" -> myConstitution;
            case "int", "intelligence" -> myIntelligence;
            case "wis", "wisdom" -> myWisdom;
            case "cha", "charisma" -> myCharisma;
            default -> 10;
        };
        return (score - 10) / 2;
    }

    /**
     * Returns a formatted string showing both ability score and modifier.
     *
     * @param theAbility the ability name to format
     * @return formatted string with score and modifier
     */
    public String getFormattedAbilityScore(String theAbility) {
        int score = switch (theAbility.toLowerCase()) {
            case "str", "strength" -> myStrength;
            case "dex", "dexterity" -> myDexterity;
            case "con", "constitution" -> myConstitution;
            case "int", "intelligence" -> myIntelligence;
            case "wis", "wisdom" -> myWisdom;
            case "cha", "charisma" -> myCharisma;
            default -> 10;
        };
        int modifier = (score - 10) / 2;
        String modStr = modifier >= 0 ? "+" + modifier : String.valueOf(modifier);
        return score + " (" + modStr + ")";
    }

    /**
     * Gets the full class description including subclass if available.
     *
     * @return formatted class string with subclass in parentheses if present
     */
    public String getFullClass() {
        if (mySubclassId != null && !mySubclassId.isEmpty()) {
            return myClassId + " (" + mySubclassId + ")";
        }
        return myClassId;
    }

    /**
     * Gets the full species description including subspecies if available.
     *
     * @return formatted species string with subspecies in parentheses if present
     */
    public String getFullSpecies() {
        if (mySubspeciesId != null && !mySubspeciesId.isEmpty()) {
            return mySpeciesId + " (" + mySubspeciesId + ")";
        }
        return mySpeciesId;
    }

    /**
     * Returns a string representation of the character.
     *
     * @return formatted string showing character name, level, and class
     */
    @Override
    public String toString() {
        return myCharId + " (Level " + myLevel + " " + getFullClass() + ")";
    }
}