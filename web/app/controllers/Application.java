package controllers;

import models.Bank;
import models.BonusCard;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    private static final List<Bank> banks;
    private static final List<BonusCard> bonusCards;

    static {
        banks = new ArrayList<Bank>();

        final Bank bankOne = new Bank(0, "Bank One");
        final Map<String, Double> bankOneSixMonths = new HashMap<String, Double>();
        bankOneSixMonths.put(Bank.INTEREST_RATE, 1D);
        bankOneSixMonths.put(Bank.OPENING_FEE, 10D);
        bankOneSixMonths.put(Bank.TRANSACTION_FEE, 4D);
        bankOne.valueMap.put(6, bankOneSixMonths);
        final Map<String, Double> bankOneNineMonths = new HashMap<String, Double>();
        bankOneNineMonths.put(Bank.INTEREST_RATE, 3D);
        bankOneNineMonths.put(Bank.OPENING_FEE, 20D);
        bankOneNineMonths.put(Bank.TRANSACTION_FEE, 5D);
        bankOne.valueMap.put(9, bankOneNineMonths);
        final Map<String, Double> bankOneTwelveMonths = new HashMap<String, Double>();
        bankOneTwelveMonths.put(Bank.INTEREST_RATE, 4D);
        bankOneTwelveMonths.put(Bank.OPENING_FEE, 30D);
        bankOneTwelveMonths.put(Bank.TRANSACTION_FEE, 6D);
        bankOne.valueMap.put(12, bankOneTwelveMonths);
        banks.add(bankOne);

        final Bank bankTwo = new Bank(1, "Bank Two");
        final Map<String, Double> bankTwoSixMonths = new HashMap<String, Double>();
        bankTwoSixMonths.put(Bank.INTEREST_RATE, 5D);
        bankTwoSixMonths.put(Bank.OPENING_FEE, 50D);
        bankTwoSixMonths.put(Bank.TRANSACTION_FEE, 1D);
        bankTwo.valueMap.put(6, bankTwoSixMonths);
        final Map<String, Double> bankTwoNineMonths = new HashMap<String, Double>();
        bankTwoNineMonths.put(Bank.INTEREST_RATE, 7D);
        bankTwoNineMonths.put(Bank.OPENING_FEE, 10D);
        bankTwoNineMonths.put(Bank.TRANSACTION_FEE, 3D);
        bankTwo.valueMap.put(9, bankTwoNineMonths);
        final Map<String, Double> bankTwoTwelveMonths = new HashMap<String, Double>();
        bankTwoTwelveMonths.put(Bank.INTEREST_RATE, 6D);
        bankTwoTwelveMonths.put(Bank.OPENING_FEE, 30D);
        bankTwoTwelveMonths.put(Bank.TRANSACTION_FEE, 5D);
        bankTwo.valueMap.put(12, bankTwoTwelveMonths);
        banks.add(bankTwo);

        Collections.sort(banks);

        bonusCards = new ArrayList<BonusCard>();

        final BonusCard bonusCardOne = new BonusCard(0, "Bonus Card One", 3);
        bonusCards.add(bonusCardOne);
        final BonusCard bonusCardTwo = new BonusCard(1, "Bonus Card Two", 5);
        bonusCards.add(bonusCardTwo);
        Collections.sort(bonusCards);

    }

    public static void index() {
        final List<Bank> bankList = banks;
        render(bankList);
    }

    public static void postForm(@Required long inputLoanAmount, int inputBank, long inputRegularIncome,
            long inputRegularBills, long inputCreditCard) {
        if(validation.hasErrors()) {
            response.status = 400;
            renderJSON(validation.errors().toArray());
        }

        Logger.info(inputLoanAmount + " " + inputBank + " " + inputRegularIncome + " " + inputRegularBills
                + " " + inputCreditCard);

        final Bank bank = banks.get(inputBank);
        Logger.info("Bank: " + bank.name);
        final Map<Integer, Map<String, Double>> valueMap = bank.valueMap;
        final Map<Integer, Map<String, Double>> loanMap = new HashMap<Integer, Map<String, Double>>();
        for (Integer key : valueMap.keySet()) {
            final Map<String, Double> loanMonthMap = new HashMap<String, Double>(valueMap.get(key));
            loanMap.put(key, loanMonthMap);
            final double rate = (double)loanMonthMap.get(Bank.INTEREST_RATE) / 100;
            final double monthlyPayment =
                    (inputLoanAmount / ((1 - Math.pow(1 + (rate / 12.0 ), -key)) / (rate /12)))
                            + loanMonthMap.get(Bank.TRANSACTION_FEE);
            final double totalPayment = monthlyPayment * key + loanMonthMap.get(Bank.OPENING_FEE);
            loanMonthMap.put(Bank.MONTHLY_PAYMENT, monthlyPayment);
            loanMonthMap.put(Bank.TOTAL_AMOUNT, totalPayment);
        }

        final Map<String, Double> bonusCardAmounts = new HashMap<String, Double>();
        for (BonusCard bonusCard : bonusCards) {
            bonusCardAmounts.put(bonusCard.name, (inputLoanAmount * (bonusCard.bonusPercentage / 100)));
        }

        Map<String, Object> objectsToRender = new HashMap<String, Object>();
        objectsToRender.put("loanMap", loanMap);
        objectsToRender.put("bonusCardAmounts", bonusCardAmounts);

        renderJSON(objectsToRender);
    }

}