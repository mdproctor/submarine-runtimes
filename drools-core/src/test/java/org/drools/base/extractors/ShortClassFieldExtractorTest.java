package org.drools.base.extractors;

import junit.framework.Assert;

import org.drools.base.ClassFieldExtractorFactory;
import org.drools.base.TestBean;
import org.drools.spi.Extractor;

public class ShortClassFieldExtractorTest extends ClassFieldExtractorsBaseTest {
    private static final short VALUE     = 3;

    Extractor                  extractor = ClassFieldExtractorFactory.getClassFieldExtractor( TestBean.class,
                                                                                              "shortAttr" );
    TestBean                   bean      = new TestBean();

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetBooleanValue() {
        try {
            this.extractor.getBooleanValue( this.bean );
            fail( "Should have throw an exception" );
        } catch ( final Exception e ) {
            // success
        }
    }

    public void testGetByteValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getByteValue( this.bean ) );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetCharValue() {
        try {
            this.extractor.getCharValue( this.bean );
            fail( "Should have throw an exception" );
        } catch ( final Exception e ) {
            // success
        }
    }

    public void testGetDoubleValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getDoubleValue( this.bean ),
                                 0.01 );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetFloatValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getFloatValue( this.bean ),
                                 0.01 );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetIntValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getIntValue( this.bean ) );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetLongValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getLongValue( this.bean ) );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetShortValue() {
        try {
            Assert.assertEquals( ShortClassFieldExtractorTest.VALUE,
                                 this.extractor.getShortValue( this.bean ) );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }

    public void testGetValue() {
        try {
            Assert.assertEquals( new Short( ShortClassFieldExtractorTest.VALUE ),
                                 this.extractor.getValue( this.bean ) );
            Assert.assertTrue( this.extractor.getValue( this.bean ) instanceof Short );
        } catch ( final Exception e ) {
            fail( "Should not throw an exception" );
        }
    }
}
