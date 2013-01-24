package fi.manageme;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Risto Virtanen
 */
public class Bank implements Comparable {

    public static final String INTEREST_RATE = "INTEREST_RATE";
    public static final String OPENING_FEE = "OPENING_FEE";
    public static final String TRANSACTION_FEE = "TRANSACTION_FEE";
    public static final String MONTHLY_PAYMENT = "MONTHLY_PAYMENT";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";

    public final int id;
    public final String name;
    public Map<Integer, Map<String, Double>> valueMap = new HashMap<Integer, Map<String, Double>>();

    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public int compareTo(Object o) {
        return this.id - ((Bank) o).id;
    }
}
