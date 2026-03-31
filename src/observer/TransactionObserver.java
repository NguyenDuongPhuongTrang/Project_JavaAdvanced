package observer;

import models.trans.Transaction;

public interface TransactionObserver {
    void update(Transaction transaction);
}
