package org.example;

import java.util.List;

public interface Auditable {
    List<Transaction> getHighValueTransactions(double threshold);
}
