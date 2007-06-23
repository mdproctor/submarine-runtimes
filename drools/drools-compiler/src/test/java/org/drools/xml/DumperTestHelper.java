/**
 * 
 */
package org.drools.xml;

import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.drools.compiler.DrlParser;
import org.drools.lang.DrlDumper;
import org.drools.lang.descr.PackageDescr;

/**
 * Helper Class for both xml and drl Dump Tests
 * 
 * @author fernandomeyer
 */
public class DumperTestHelper extends Assert {
    
    public static void XmlFile(String filename) throws Exception {

        XmlPackageReader xmlPackageReader = new XmlPackageReader();
        xmlPackageReader.read( new InputStreamReader( DumperTestHelper.class.getResourceAsStream( filename ) ) );
        final PackageDescr pkgOriginal = xmlPackageReader.getPackageDescr();

        final XmlDumper dumper = new XmlDumper();
        final String result = dumper.dump( pkgOriginal );
        
        
        System.out.println(result);

        String buffer = readFile( filename );
        
        System.out.println( buffer);

        assertEqualsIgnoreWhitespace( buffer,
                                      result );
        assertNotNull( result );
    }

    public static void DrlFile(String filename) throws Exception {

        DrlParser parser = new DrlParser();
        final PackageDescr pkgOriginal = parser.parse( new InputStreamReader( DumperTestHelper.class.getResourceAsStream( filename ) ) );
        final DrlDumper dumper = new DrlDumper();
        final String result = dumper.dump( pkgOriginal );

        parser = new DrlParser();
        String buffer = readFile( filename );
        assertEqualsIgnoreWhitespace( buffer.toString(),
                                      result );

    }

    private static void assertEqualsIgnoreWhitespace(final String expected,
                                              final String actual) {
        final String cleanExpected = expected.replaceAll( "\\s+",
                                                          "" );
        final String cleanActual = actual.replaceAll( "\\s+",
                                                      "" );
        assertEquals( cleanExpected,
                      cleanActual );
    }

    private static String readFile(final String file) throws IOException {
        final InputStreamReader reader = new InputStreamReader( DumperTestHelper.class.getResourceAsStream( file ) );
        final StringBuffer text = new StringBuffer();
        final char[] buf = new char[1024];
        int len = 0;

        while ( (len = reader.read( buf )) >= 0 ) {
            text.append( buf,
                         0,
                         len );
        }
        return text.toString();
    }
}
