package org.drools.core.common;

import org.drools.core.marshalling.impl.MarshallerReaderContext;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.rule.EntryPoint;
import org.drools.core.rule.Rule;
import org.drools.core.spi.PropagationContext;

public interface PropagationContextFactory {

    public PropagationContext createPropagationContext(final long number,
                                                       final int type,
                                                       final Rule rule,
                                                       final LeftTuple leftTuple,
                                                       final InternalFactHandle factHandle,
                                                       final EntryPoint entryPoint,
                                                       final long modificationMask,
                                                       final Class<?> modifiedClass,
                                                       final MarshallerReaderContext readerContext);

    public PropagationContext createPropagationContext(final long number,
                                                       final int type,
                                                       final Rule rule,
                                                       final LeftTuple leftTuple,
                                                       final InternalFactHandle factHandle,
                                                       final EntryPoint entryPoint,
                                                       final MarshallerReaderContext readerContext);

    public PropagationContext createPropagationContext(final long number,
                                                       final int type,
                                                       final Rule rule,
                                                       final LeftTuple leftTuple,
                                                       final InternalFactHandle factHandle,
                                                       final int activeActivations,
                                                       final int dormantActivations,
                                                       final EntryPoint entryPoint,
                                                       final long modificationMask);

    public PropagationContext createPropagationContext(final long number,
                                                       final int type,
                                                       final Rule rule,
                                                       final LeftTuple leftTuple,
                                                       final InternalFactHandle factHandle,
                                                       final EntryPoint entryPoint);

    public PropagationContext createPropagationContext(final long number,
                                                       final int type,
                                                       final Rule rule,
                                                       final LeftTuple leftTuple,
                                                       final InternalFactHandle factHandle);

}