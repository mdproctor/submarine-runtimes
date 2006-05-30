package org.drools.decisiontable;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Some basic unit tests for converter utility.
 */
public class SpreadsheetCompilerUnitTest extends TestCase {

    public void testLoadFromClassPath() {
        final SpreadsheetCompiler converter = new SpreadsheetCompiler();
        final String drl = converter.compile( "/data/MultiSheetDST.xls",
                                        InputType.XLS );

        assertNotNull( drl );

        assertTrue( drl.indexOf( "rule \"How cool am I_12\"" ) > drl.indexOf( "rule \"How cool am I_11\"" ) );
        assertTrue( drl.indexOf( "import example.model.User;" ) > -1 );
        assertTrue( drl.indexOf( "import example.model.Car;" ) > -1 );
    }

    public void testLoadSpecificWorksheet() {
        final SpreadsheetCompiler converter = new SpreadsheetCompiler();
        final InputStream stream = this.getClass().getResourceAsStream( "/data/MultiSheetDST.xls" );
        final String drl = converter.compile( stream,
                                        "Another Sheet" );
        assertNotNull( drl );
    }

    public void testLoadCsv() {
        final SpreadsheetCompiler converter = new SpreadsheetCompiler();
        final InputStream stream = this.getClass().getResourceAsStream( "/data/ComplexWorkbook.csv" );
        final String drl = converter.compile( stream,
                                        InputType.CSV );
        assertNotNull( drl );
        assertTrue( drl.indexOf( "myObject.setIsValid(1, 2)" ) > 0 );
        assertTrue( drl.indexOf( "myObject.size () > 50" ) > 0 );
        //System.out.println(drl);
    }

    public void testLoadBasic() {
        final SpreadsheetCompiler converter = new SpreadsheetCompiler();
        final InputStream stream = this.getClass().getResourceAsStream( "/data/BasicWorkbook.xls" );
        final String drl = converter.compile( stream,
                                        InputType.XLS );

        assertNotNull( drl );
        assertTrue( drl.indexOf( "This is a function block" ) > -1 );
        assertTrue( drl.indexOf( "global Class1 obj1;" ) > -1 );
        //System.out.println(drl);
    }

}