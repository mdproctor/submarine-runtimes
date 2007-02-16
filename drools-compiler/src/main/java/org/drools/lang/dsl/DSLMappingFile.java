/*
 * Copyright 2006 JBoss Inc
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

package org.drools.lang.dsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.drools.lang.dsl.DSLMappingEntry.DefaultDSLEntryMetaData;

/**
 * A helper class that handles a DSL Mapping file
 * @author etirelli
 */
public class DSLMappingFile {

    // the following pattern will be used to parse dsl mapping entries in the DSL file.
    // It is capable of parsing entries that follows the pattern:
    // [<section>][<metadata>]?<key>=<value>
    private static final Pattern pattern     = Pattern.compile( "((\\[[^\\[]*\\])\\s*(\\[([^\\[]*)\\])?)?\\s*([^=]*)=(.*)" );
    private static final String  KEYWORD     = "[keyword]";
    private static final String  CONDITION   = "[when]";
    private static final String  CONSEQUENCE = "[then]";
    //private static final String  ANY         = "[*]";

    private DSLMapping           mapping;
    private List                 errors;

    public DSLMappingFile() {
        this.mapping = new DefaultDSLMapping( );
        this.errors = Collections.EMPTY_LIST;
    }

    /**
     * Returns the DSL mapping loaded from this file
     * @return
     */
    public DSLMapping getMapping() {
        return this.mapping;
    }

    /**
     * Sets the 
     * @param mapping
     */
    public void setMapping( DSLMapping mapping ) {
        this.mapping = mapping;
    }

    /**
     * Returns the list of parsing errors
     * @return
     */
    public List getErrors() {
        return Collections.unmodifiableList( this.errors );
    }

    /**
     * Parses the file. Throws IOException in case there is any problem
     * reading the file;
     * 
     * @return true in case no error was found parsing the file. false 
     *         otherwise. Use getErrors() to check for the actual errors.
     */
    public boolean parseAndLoad( Reader dsl ) throws IOException {
        String line = null;
        int linecounter = 0;
        BufferedReader dslFileReader = new BufferedReader( dsl );
        this.mapping = new DefaultDSLMapping();
        this.errors = new LinkedList();
        while ( (line = dslFileReader.readLine()) != null ) {
            linecounter++;
            Matcher mat = pattern.matcher( line );
            if ( mat.matches() ) {
                String sectionStr = mat.group( 2 );
                String metadataStr = mat.group( 4 );
                String key = mat.group( 5 );
                String value = mat.group( 6 );

                DSLMappingEntry.Section section = DSLMappingEntry.ANY;
                if ( KEYWORD.equals( sectionStr ) ) {
                    section = DSLMappingEntry.KEYWORD;
                } else if ( CONDITION.equals( sectionStr ) ) {
                    section = DSLMappingEntry.CONDITION;
                } else if ( CONSEQUENCE.equals( sectionStr ) ) {
                    section = DSLMappingEntry.CONSEQUENCE;
                }

                DSLMappingEntry.MetaData metadata = new DefaultDSLEntryMetaData( metadataStr );

                DSLMappingEntry entry = new DefaultDSLMappingEntry( section,
                                                                    metadata,
                                                                    key,
                                                                    value );

                this.mapping.addEntry( entry );
            } else if ( !line.trim().startsWith( "#" ) ) { // it is not a comment 
                String error = "Error parsing mapping entry: " + line;
                DSLMappingParseException exception = new DSLMappingParseException( error,
                                                                                   linecounter );
                this.errors.add( exception );
            }
        }
        return this.errors.isEmpty();
    }

    /**
     * Saves current mapping into a DSL mapping file
     * @param out
     * @throws IOException
     */
    public void saveMapping( Writer out ) throws IOException {
        for ( Iterator it = mapping.getEntries().iterator(); it.hasNext(); ) {
            out.write( it.next().toString() );
            out.write( "\n" );
        }
    }
    
    /**
     * Saves the given mapping into a DSL mapping file
     * 
     * @param out
     * @param mapping
     * @throws IOException
     */
    public static void saveMapping( Writer out, DSLMapping mapping ) throws IOException {
        for ( Iterator it = mapping.getEntries().iterator(); it.hasNext(); ) {
            out.write( it.next().toString() );
            out.write( "\n" );
        }
    }
    
    /**
     * Method to return the current mapping as a String object
     * @return
     */
    public String dumpFile() {
        StringBuffer buf = new StringBuffer();
        for ( Iterator it = this.mapping.getEntries().iterator(); it.hasNext(); ) {
            buf.append( it.next() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
