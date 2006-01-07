package org.drools.rule;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.TestCase;

import org.drools.Cheese;
import org.drools.spi.ClassFieldExtractor;
import org.drools.spi.ClassObjectType;
import org.drools.spi.Extractor;
import org.drools.spi.FieldExtractor;
import org.drools.spi.ObjectType;

public class DeclarationTest extends TestCase {

    public void testDeclaration() throws IntrospectionException {
        FieldExtractor extractor = new ClassFieldExtractor( Cheese.class,
                                                            getIndex( Cheese.class,
                                                                      "type" ) );

        /* Bind the extractor to a decleration */
        /* Declarations know the column they derive their value from */
        Declaration declaration = new Declaration( 3,
                                                   "typeOfCheese",
                                                   extractor,
                                                   5 );
        assertEquals( 3,
                      declaration.getIndex() );

        assertEquals( "typeOfCheese",
                      declaration.getIdentifier() );

        assertSame( String.class,
                    ( (ClassObjectType) declaration.getObjectType()).getClassType() );

        assertSame( extractor,
                    declaration.getExtractor() );

        assertEquals( 5,
                      declaration.getColumn() );

    }

    public void testGetFieldValue() throws IntrospectionException {
        FieldExtractor extractor = new ClassFieldExtractor( Cheese.class,
                                                            getIndex( Cheese.class,
                                                                      "type" ) );

        /* Bind the extractor to a decleration */
        /* Declarations know the column they derive their value from */
        Declaration declaration = new Declaration( 3,
                                                   "typeOfCheese",
                                                   extractor,
                                                   5 );

        /* Create some facts */
        Cheese cheddar = new Cheese( "cheddar",
                                     5 );

        /* Check we can extract Declarations correctly */
        assertEquals( "cheddar",
                      declaration.getValue( cheddar ) );
    }

    public static int getIndex(Class clazz,
                               String name) throws IntrospectionException {
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo( clazz ).getPropertyDescriptors();
        for ( int i = 0; i < descriptors.length; i++ ) {
            if ( descriptors[i].getName().equals( name ) ) {
                return i;
            }
        }
        return -1;
    }
}
