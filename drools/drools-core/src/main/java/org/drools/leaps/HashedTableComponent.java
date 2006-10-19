package org.drools.leaps;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.drools.base.evaluators.Operator;
import org.drools.leaps.util.Table;
import org.drools.leaps.util.TableIterator;
import org.drools.rule.Declaration;
import org.drools.rule.VariableConstraint;
import org.drools.spi.Tuple;

public class HashedTableComponent
    implements
    Serializable {
    private final ColumnConstraints constraints;

    private final int               numberOfVariableConstraints;

    private final Map               buckets;

    private final Comparator        comparator;

    private final static Integer    DEFAULT_HASH = new Integer( 0 );

    private final Table             noConstraintsTable;

    public HashedTableComponent(final ColumnConstraints constraints,
                                final Comparator comparator) {
        this.constraints = constraints;
        this.comparator = comparator;
        this.buckets = new HashMap();
        this.numberOfVariableConstraints = this.constraints.getBetaContraints().length;
        this.noConstraintsTable = new Table( this.comparator );
    }

    public void add(final LeapsFactHandle factHandle) {
        final Table table = this.getTable( factHandle );
        if ( table != null ) {
            table.add( factHandle );
            factHandle.addHash( table );
        }
    }

    public TableIterator reverseOrderIterator(final Tuple tuple) {
        final Table table = this.getTable( tuple );
        if ( table != null ) {
            return table.reverseOrderIterator();
        } else {
            return null;
        }
    }

    public TableIterator iteratorFromPositionToTableEnd(final Tuple tuple,
                                                        final LeapsFactHandle startFactHandle) {
        final Table table = this.getTable( tuple );
        if ( table != null ) {
            return table.iteratorFromPositionToTableEnd( startFactHandle );
        } else {
            return null;
        }
    }

    public TableIterator iteratorFromPositionToTableStart(final Tuple tuple,
                                                          final LeapsFactHandle startFactHandle,
                                                          final LeapsFactHandle currentFactHandle) {
        final Table table = this.getTable( tuple );
        if ( table != null ) {
            return table.iteratorFromPositionToTableStart( startFactHandle,
                                                           currentFactHandle );
        } else {
            return null;
        }
    }

    private Table getTable(final Tuple tuple) {
        Table ret = null;
        if ( this.numberOfVariableConstraints > 0 ) {
            Map currentMap = this.buckets;
            for ( int i = 0; (i < this.numberOfVariableConstraints) && (currentMap != null); i++ ) {
                Integer hash = HashedTableComponent.DEFAULT_HASH;
                if ( this.constraints.getBetaContraints()[i] instanceof VariableConstraint && ((VariableConstraint) this.constraints.getBetaContraints()[i]).getEvaluator().getOperator() == Operator.EQUAL ) {
                    final Declaration declaration = this.constraints.getBetaContraints()[i].getRequiredDeclarations()[0];
                    final Object select = declaration.getValue( tuple.get( declaration.getColumn().getFactIndex() ).getObject() );
                    if ( select != null ) {
                        hash = new Integer( select.hashCode() );
                    }
                }
                // put facts at the very bottom / last instance
                if ( i != (this.numberOfVariableConstraints - 1) ) {
                    // we can not have null as a value to the key
                    currentMap = (Map) currentMap.get( hash );
                } else {
                    ret = (Table) currentMap.get( hash );
                }
            }
        } else {
            ret = this.noConstraintsTable;
        }
        return ret;
    }

    private Table getTable(final LeapsFactHandle factHandle) {
        Table ret = null;
        Map currentMap = this.buckets;
        if ( this.constraints.isAllowedAlpha( factHandle,
                                              null,
                                              null ) ) {
            if ( this.numberOfVariableConstraints > 0 ) {
                for ( int i = 0; i < this.numberOfVariableConstraints; i++ ) {
                    Integer hash = HashedTableComponent.DEFAULT_HASH;
                    if ( this.constraints.getBetaContraints()[i] instanceof VariableConstraint && ((VariableConstraint) this.constraints.getBetaContraints()[i]).getEvaluator().getOperator() == Operator.EQUAL ) {
                        final Object select = ((VariableConstraint) this.constraints.getBetaContraints()[i]).getFieldExtractor().getValue( factHandle.getObject() );
                        if ( select != null ) {
                            hash = new Integer( select.hashCode() );
                        }
                    }
                    // put facts at the very bottom / last instance
                    if ( i != (this.numberOfVariableConstraints - 1) ) {
                        // we can not have null as a value to the key
                        Map map = (Map) currentMap.get( hash );
                        if ( map == null ) {
                            map = new HashMap();
                            currentMap.put( hash,
                                            map );
                        }
                        currentMap = map;
                    } else {
                        Table table = (Table) currentMap.get( hash );
                        if ( table == null ) {
                            table = new Table( this.comparator );
                            currentMap.put( hash,
                                            table );
                        }
                        ret = table;
                    }
                }
            } else {
                return this.noConstraintsTable;
            }
        }
        return ret;
    }

}
