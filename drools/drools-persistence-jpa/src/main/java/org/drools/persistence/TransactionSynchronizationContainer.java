package org.drools.persistence;

import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionSynchronizationContainer implements TransactionSynchronization {

    private static Logger logger = LoggerFactory.getLogger( TransactionSynchronizationContainer.class );
    public static final String RESOURCE_KEY = "org.drools.persistence.txsync.container";

    private Set<TransactionSynchronization> synchronizations = new TreeSet<TransactionSynchronization>();

    @Override
    public void beforeCompletion() {
        for (TransactionSynchronization txSync : synchronizations) {
            txSync.beforeCompletion();
        }
    }

    @Override
    public void afterCompletion(int status) {
        for (TransactionSynchronization txSync : synchronizations) {
            txSync.afterCompletion(status);
        }
    }

    public void addTransactionSynchronization(TransactionSynchronization txSync) {
        this.synchronizations.add(txSync);
        logger.debug("Adding sync {} total syncs ", txSync, synchronizations.size());
    }
}
