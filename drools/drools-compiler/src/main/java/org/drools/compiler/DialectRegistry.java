package org.drools.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A Registry of DialectConfigurations. It is also responsible for issueing actions to all registered
 * dialects.
 * This Class api is subject to change.
 *
 */
public class DialectRegistry {
    private Map map;

    public DialectRegistry() {
        this.map = new HashMap();
    }

    /**
     * Add a DialectConfiguration to the registry
     * @param name
     * @param dialect
     */
    public void addDialectConfiguration(final String name,
                                        final DialectConfiguration dialect) {
        this.map.put( name,
                      dialect );
    }

    /**
     * Get a DialectConfiguration for a named dialect
     * @param name
     * @return
     */
    public DialectConfiguration getDialectConfiguration(final String name) {
        return (DialectConfiguration) this.map.get( name );
    }

    /**
     * Initialise all registered dialects for the given PackageBuilder.
     * @param builder
     */
    public void initAll(PackageBuilder builder) {
        for ( Iterator it = this.map.values().iterator(); it.hasNext(); ) {
            DialectConfiguration dialect = (DialectConfiguration) it.next();
            dialect.getDialect().init( builder );
        }
    }

    /**
     * Instruct all registered dialects to compile what ever they have attempted to build.
     *
     */
    public void compileAll() {
        for ( Iterator it = this.map.values().iterator(); it.hasNext(); ) {
            DialectConfiguration dialect = (DialectConfiguration) it.next();
            dialect.getDialect().compileAll();
        }
    }

    /**
     * Return an Iterator of DialectConfigurations
     * @return
     */
    public Iterator iterator() {
        return this.map.values().iterator();
    }

    /**
     * Add all registered Dialect results to the provided List.
     * @param list
     * @return
     */
    public List addResults(List list) {
        if ( list == null ) {
            list = new ArrayList();
        }
        for ( Iterator it = this.map.values().iterator(); it.hasNext(); ) {
            DialectConfiguration dialect = (DialectConfiguration) it.next();
            List results = dialect.getDialect().getResults();
            if ( results != null ) {
                list.addAll( results );
            }
        }
        return list;
    }

    /**
     * Iterates all registered dialects, informing them of an import added to the PackageBuilder
     * @param importEntry
     */
    public void addImport(String importEntry) {
        for ( Iterator it = this.map.values().iterator(); it.hasNext(); ) {
            DialectConfiguration dialect = (DialectConfiguration) it.next();
            dialect.getDialect().addImport( importEntry );
        }
    }

    /**
     * Iterates all registered dialects, informing them of a static imports added to the PackageBuilder
     * @param staticImportEntry
     */    
    public void addStaticImport(String staticImportEntry) {
        for ( Iterator it = this.map.values().iterator(); it.hasNext(); ) {
            DialectConfiguration dialect = (DialectConfiguration) it.next();
            dialect.getDialect().addStaticImport( staticImportEntry );
        }
    }

}
