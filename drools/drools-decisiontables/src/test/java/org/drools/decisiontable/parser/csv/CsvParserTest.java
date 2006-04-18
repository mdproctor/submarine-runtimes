package org.drools.decisiontable.parser.csv;
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






import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.decisiontable.parser.SheetListener;

public class CsvParserTest extends TestCase
{

    public void testCsv()
    {
        MockSheetListener listener = new MockSheetListener();
        CsvLineParser lineParser = new CsvLineParser();
        CsvParser parser = new CsvParser(listener, lineParser);
        
        parser.parseFile(getClass().getResourceAsStream("/data/TestCsv.csv"));
        assertEquals("A", listener.getCell(0, 0));
        assertEquals("B", listener.getCell(0, 1));
        assertEquals("", listener.getCell(2, 0));
        assertEquals("C", listener.getCell(1, 0));
        assertEquals("D", listener.getCell(1, 1));  
        assertEquals("E", listener.getCell(1, 3));

    }

    static class MockSheetListener
        implements
        SheetListener
    {

        Map data = new HashMap( );

        public String getCell(int row,
                              int col)
        {
            return (String) data.get( cellKey( row,
                                               col ) );
        }

        public void startSheet(String name)
        {
        }

        public void finishSheet()
        {
        }

        public void newRow(int rowNumber,
                           int columns)
        {

        }

        public void newCell(int row,
                            int column,
                            String value)
        {

            data.put( cellKey( row,
                               column ),
                      value );
        }

        String cellKey(int row,
                       int column)
        {
            return "R" + row + "C" + column;
        }

    }
}