package org.drools.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.drools.spi.ColumnExtractor;
import org.drools.spi.Extractor;
import org.drools.spi.FieldConstraint;
import org.drools.spi.ObjectType;

public class Column
    implements
    Serializable {
    private final ObjectType objectType;
    private List             constraints = Collections.EMPTY_LIST;
    final Declaration        declaration;
    private final int        index;

    // this is the negative offset of the related fact inside a tuple. i.e:
    // tuple_fact_index = column_index - offset; 
    private final int        offset;

    public Column(int index,
                  ObjectType objectType) {
        this( index,
              0,
              objectType,
              null );
    }

    public Column(int index,
                  ObjectType objectType,
                  String identifier)
    {
        this( index,
              0,
              objectType,
              identifier );
    }

    public Column(int index,
                  int offset,
                  ObjectType objectType,
                  String identifier)
    {
        this.index = index;
        this.offset = offset;
        this.objectType = objectType;
        if ( identifier != null ) {
            this.declaration = new Declaration( identifier,
                                                new ColumnExtractor( objectType ),
                                                index-offset );
        } else {
            this.declaration = null;
        }
    }

    public ObjectType getObjectType() {
        return this.objectType;
    }

    public List getConstraints() {
        return Collections.unmodifiableList( this.constraints );
    }

    public void addConstraint(FieldConstraint constraint) {
        if ( this.constraints == Collections.EMPTY_LIST ) {
            this.constraints = new ArrayList( 1 );
        }
        this.constraints.add( constraint );
    }

    public Declaration addDeclaration(String identifier,
                                      Extractor extractor) {
        if ( this.constraints == Collections.EMPTY_LIST ) {
            this.constraints = new ArrayList( 1 );
        }
        Declaration declaration = new Declaration( identifier,
                                                   extractor,
                                                   index-offset );
        this.constraints.add( declaration );
        return declaration;

    }

    public boolean isBound() {
        return (this.declaration != null);
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }
    
    public int getFactIndex() {
        return this.index - this.offset;
    }

    public String toString() {
        return "Column type='" + this.objectType + "', index='" + this.index + "', offset='" + this.getOffset() + "', identifer='" + this.declaration.getIdentifier() + "'";
    }

}
