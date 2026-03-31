package observer;

import models.trans.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionSubject {
    private static TransactionSubject instance;
    private List<TransactionObserver> observers = new ArrayList<>();

    private TransactionSubject(){}

    public static TransactionSubject getInstance() {
        if (instance == null) {
            instance = new TransactionSubject();
        }
        return instance;
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Transaction transaction) {
        for (TransactionObserver o : observers) {
            o.update(transaction);
        }
    }
}