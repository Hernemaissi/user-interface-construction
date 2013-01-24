package fi.manageme;

/**
 * @author Risto Virtanen
 */
public class BonusCard implements Comparable {

    public final int id;
    public final String name;
    public final double bonusPercentage;

    public BonusCard(int id, String name, long bonusPercentage) {
        this.id = id;
        this.name = name;
        this.bonusPercentage = bonusPercentage;
    }

    @Override
    public int compareTo(Object o) {
        BonusCard anotherCard = (BonusCard) o;
        return this.name.compareTo(anotherCard.name);
    }
}
