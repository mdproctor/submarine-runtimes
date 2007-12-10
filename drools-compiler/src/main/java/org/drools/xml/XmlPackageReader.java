package org.drools.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;

import org.drools.lang.descr.PackageDescr;
import org.drools.xml.rules.AccumulateHandler;
import org.drools.xml.rules.AccumulateHelperHandler;
import org.drools.xml.rules.AndHandler;
import org.drools.xml.rules.CollectHandler;
import org.drools.xml.rules.EvalHandler;
import org.drools.xml.rules.ExistsHandler;
import org.drools.xml.rules.ExpressionHandler;
import org.drools.xml.rules.FieldBindingHandler;
import org.drools.xml.rules.FieldConstraintHandler;
import org.drools.xml.rules.ForallHandler;
import org.drools.xml.rules.FromHandler;
import org.drools.xml.rules.FunctionHandler;
import org.drools.xml.rules.LiteralRestrictionHandler;
import org.drools.xml.rules.NotHandler;
import org.drools.xml.rules.OrHandler;
import org.drools.xml.rules.PackageHandler;
import org.drools.xml.rules.PatternHandler;
import org.drools.xml.rules.PredicateHandler;
import org.drools.xml.rules.QualifiedIdentifierRestrictionHandler;
import org.drools.xml.rules.QueryHandler;
import org.drools.xml.rules.RestrictionConnectiveHandler;
import org.drools.xml.rules.ReturnValueRestrictionHandler;
import org.drools.xml.rules.RuleHandler;
import org.drools.xml.rules.VariableRestrictionsHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XmlPackageReader {
    private ExtensibleXmlParser parser;

    private PackageDescr        packageDescr;

    public XmlPackageReader(final SemanticModules modules) {
        this( modules, null );
    }

    public XmlPackageReader(final SemanticModules modules, final SAXParser parser) {
        if ( parser == null ) {
            this.parser = new ExtensibleXmlParser();
        } else {
            this.parser = new ExtensibleXmlParser( parser );
        }      
        this.parser.setSemanticModules( modules );
    }

    /**
     * Read a <code>RuleSet</code> from a <code>Reader</code>.
     *
     * @param reader
     *            The reader containing the rule-set.
     *
     * @return The rule-set.
     */
    public PackageDescr read(final Reader reader) throws SAXException,
                                                 IOException {
        this.packageDescr = (PackageDescr) this.parser.read( reader );
        return this.packageDescr;
    }

    /**
     * Read a <code>RuleSet</code> from an <code>InputStream</code>.
     *
     * @param inputStream
     *            The input-stream containing the rule-set.
     *
     * @return The rule-set.
     */
    public PackageDescr read(final InputStream inputStream) throws SAXException,
                                                           IOException {
        this.packageDescr = (PackageDescr) this.parser.read( inputStream );
        return this.packageDescr;
    }

    /**
     * Read a <code>RuleSet</code> from an <code>InputSource</code>.
     *
     * @param in
     *            The rule-set input-source.
     *
     * @return The rule-set.
     */
    public PackageDescr read(final InputSource in) throws SAXException,
                                                  IOException {
        this.packageDescr = (PackageDescr) this.parser.read( in );
        return this.packageDescr;
    }

    void setPackageDescr(final PackageDescr packageDescr) {
        this.packageDescr = packageDescr;
    }

    public PackageDescr getPackageDescr() {
        return this.packageDescr;
    }
}
