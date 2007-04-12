package org.drools.rule;

import org.drools.common.InternalWorkingMemory;
import org.drools.reteoo.ReteTuple;
import org.drools.spi.Extractor;
import org.drools.spi.Restriction;

public class AndCompositeRestriction extends AbstractCompositeRestriction {

    private static final long serialVersionUID = 320;

    public AndCompositeRestriction(final Restriction[] restriction) {
        super( restriction );
    }

    public boolean isAllowed(final Extractor extractor,
                             final Object object,
                             final InternalWorkingMemory workingMemory) {
        for ( int i = 0, ilength = this.restrictions.length; i < ilength; i++ ) {
            if ( !this.restrictions[i].isAllowed( extractor,
                                                  object,
                                                  workingMemory ) ) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllowedCachedLeft(final ContextEntry context,
                                       final Object object) {
        for ( int i = 0, ilength = this.restrictions.length; i < ilength; i++ ) {
            if ( !this.restrictions[i].isAllowedCachedLeft( this.contextEntry.contextEntries[i],
                                                            object ) ) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllowedCachedRight(final ReteTuple tuple,
                                        final ContextEntry context) {
        for ( int i = 0, ilength = this.restrictions.length; i < ilength; i++ ) {
            if ( !this.restrictions[i].isAllowedCachedRight( tuple,
                                                             this.contextEntry.contextEntries[i] ) ) {
                return false;
            }
        }
        return true;
    }

    public ContextEntry getContextEntry() {
        return this.contextEntry;
    }
}
