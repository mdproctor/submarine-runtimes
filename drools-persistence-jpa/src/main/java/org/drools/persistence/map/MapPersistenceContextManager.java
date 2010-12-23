package org.drools.persistence.map;

import org.drools.persistence.PersistenceContext;
import org.drools.persistence.PersistenceContextManager;

public class MapPersistenceContextManager
    implements
    PersistenceContextManager {

    private PersistenceContext persistenceContext;
    
    public MapPersistenceContextManager(AbstractStorage storage) {
        persistenceContext = new MapBasedPersistenceContext(storage);
    }
    
    public PersistenceContext getApplicationScopedPersistenceContext() {
        return persistenceContext;
    }

    public PersistenceContext getCommandScopedPersistenceContext() {
        return persistenceContext;
    }

    public void beginCommandScopedEntityManager() {
    }

    public void endCommandScopedEntityManager() {
    }

    public void dispose() {
        persistenceContext.close();
    }

}
