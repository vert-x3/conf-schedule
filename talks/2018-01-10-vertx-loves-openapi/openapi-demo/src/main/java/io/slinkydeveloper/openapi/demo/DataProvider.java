package io.slinkydeveloper.openapi.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class DataProvider {

    List<Transaction> transactions;

    DataProvider() {
        transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}
