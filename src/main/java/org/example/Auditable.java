package org.example;

import java.util.List;

/**
 * The Auditable interface provides a method to retrieve high-value transactions.
 *
 * @author Alessandro Pomponi
 */
public interface Auditable {
    /**
     * Returns a list of high-value transactions that exceed the specified threshold.
     *
     * @param threshold the threshold amount
     * @return the list of high-value transactions
     * @throws IllegalArgumentException if the threshold is less than or equal to zero
     */
    List<Transaction> getHighValueTransactions(double threshold);
}

