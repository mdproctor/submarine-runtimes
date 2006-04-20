package org.drools.xml;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.lang.descr.AndDescr;
import org.drools.lang.descr.BoundVariableDescr;
import org.drools.lang.descr.ColumnDescr;
import org.drools.lang.descr.ExistsDescr;
import org.drools.lang.descr.FieldBindingDescr;
import org.drools.lang.descr.FunctionDescr;
import org.drools.lang.descr.LiteralDescr;
import org.drools.lang.descr.NotDescr;
import org.drools.lang.descr.OrDescr;
import org.drools.lang.descr.PackageDescr;
import org.drools.lang.descr.PredicateDescr;
import org.drools.lang.descr.ReturnValueDescr;
import org.drools.lang.descr.RuleDescr;

public class XmlPackageReaderTest extends TestCase {
    public void testParsePackageName() throws Exception {
        XmlPackageReader xmlPackageReader = new XmlPackageReader( );
        xmlPackageReader.read( new InputStreamReader( getClass().getResourceAsStream( "test_ParsePackageName.xml" ) ) );
        PackageDescr packageDescr = xmlPackageReader.getPackageDescr();
        assertNotNull( packageDescr );
        assertEquals("com.sample", packageDescr.getName() );
    }
    

    public void testParseImport() throws Exception {
        XmlPackageReader xmlPackageReader = new XmlPackageReader( );
        xmlPackageReader.read( new InputStreamReader( getClass().getResourceAsStream( "test_ParseImport.xml" ) ) );
        PackageDescr packageDescr = xmlPackageReader.getPackageDescr();
        assertNotNull( packageDescr );
        assertEquals("com.sample", packageDescr.getName() );
        
        List imports = packageDescr.getImports();
        assertEquals( 2, imports.size() );
        assertEquals("java.util.HashMap", imports.get( 0 ) );
        assertEquals("org.drools.*", imports.get( 1 ) );
    }
    
    public void testParseGlobal() throws Exception {
        XmlPackageReader xmlPackageReader = new XmlPackageReader( );
        xmlPackageReader.read( new InputStreamReader( getClass().getResourceAsStream( "test_ParseGlobal.xml" ) ) );
        PackageDescr packageDescr = xmlPackageReader.getPackageDescr();
        assertNotNull( packageDescr );
        assertEquals("com.sample", packageDescr.getName() );
        
        List imports = packageDescr.getImports();
        assertEquals( 2, imports.size() );
        assertEquals("java.util.HashMap", imports.get( 0 ) );
        assertEquals("org.drools.*", imports.get( 1 ) );
        
        Map globals = packageDescr.getGlobals();
        assertEquals( 2, globals.size() );
        assertEquals("com.sample.X", globals.get( "x" ) );
        assertEquals("com.sample.Yada", globals.get( "yada" ) );              
    }
    
    public void testParseFunction() throws Exception {
        XmlPackageReader xmlPackageReader = new XmlPackageReader( );
        xmlPackageReader.read( new InputStreamReader( getClass().getResourceAsStream( "test_ParseFunction.xml" ) ) );
        PackageDescr packageDescr = xmlPackageReader.getPackageDescr();
        assertNotNull( packageDescr );
        assertEquals("com.sample", packageDescr.getName() );
        
        List imports = packageDescr.getImports();
        assertEquals( 2, imports.size() );
        assertEquals("java.util.HashMap", imports.get( 0 ) );
        assertEquals("org.drools.*", imports.get( 1 ) );
        
        Map globals = packageDescr.getGlobals();
        assertEquals( 2, globals.size() );
        assertEquals("com.sample.X", globals.get( "x" ) );
        assertEquals("com.sample.Yada", globals.get( "yada" ) );      

        FunctionDescr functionDescr = (FunctionDescr) packageDescr.getFunctions().get( 0 );
        List  names = functionDescr.getParameterNames();
        assertEquals( "foo", names.get(0) );
        assertEquals( "bada", names.get(1) );
        
        List types = functionDescr.getParameterTypes();
        assertEquals( "Bar", types.get(0) );
        assertEquals( "Bing", types.get(1) );   
        
        assertEquals( "System.out.println(\"hello world\");", functionDescr.getText().trim() );       
    }
    
    public void testParseSimpleRule() throws Exception {
        XmlPackageReader xmlPackageReader = new XmlPackageReader( );
        xmlPackageReader.read( new InputStreamReader( getClass().getResourceAsStream( "test_ParseSimpleRule.xml" ) ) );
        PackageDescr packageDescr = xmlPackageReader.getPackageDescr();
        assertNotNull( packageDescr );
        assertEquals("com.sample", packageDescr.getName() );
        
        List imports = packageDescr.getImports();
        assertEquals( 2, imports.size() );
        assertEquals("java.util.HashMap", imports.get( 0 ) );
        assertEquals("org.drools.*", imports.get( 1 ) );
        
        Map globals = packageDescr.getGlobals();
        assertEquals( 2, globals.size() );
        assertEquals("com.sample.X", globals.get( "x" ) );
        assertEquals("com.sample.Yada", globals.get( "yada" ) );      

        FunctionDescr functionDescr = (FunctionDescr) packageDescr.getFunctions().get( 0 );
        List  names = functionDescr.getParameterNames();
        assertEquals( "foo", names.get(0) );
        assertEquals( "bada", names.get(1) );
        
        List types = functionDescr.getParameterTypes();
        assertEquals( "Bar", types.get(0) );
        assertEquals( "Bing", types.get(1) );   
        
        assertEquals( "System.out.println(\"hello world\");", functionDescr.getText().trim() );
        
        RuleDescr ruleDescr = ( RuleDescr ) packageDescr.getRules().get( 0 );
        assertEquals( "my rule", ruleDescr.getName() );
       
        AndDescr lhsDescr = ruleDescr.getLhs();
        AndDescr andDescr = ( AndDescr ) lhsDescr.getDescrs().get( 0 );
        OrDescr orDescr = ( OrDescr ) lhsDescr.getDescrs().get( 1 );
        ColumnDescr column1 = ( ColumnDescr ) lhsDescr.getDescrs().get( 2 );
        assertNull( column1.getIdentifier() );
        assertEquals( "Foo", column1.getObjectType() );
        
        ColumnDescr column2 = ( ColumnDescr ) lhsDescr.getDescrs().get( 3 );
        assertEquals( "Bar", column2.getObjectType() );
        assertEquals( "bar", column2.getIdentifier() );
        
        ColumnDescr column3 = ( ColumnDescr ) lhsDescr.getDescrs().get( 4 );
        LiteralDescr literalDescr = ( LiteralDescr ) column3.getDescrs().get( 0 );
        assertEquals( "field1", literalDescr.getFieldName() );
        assertEquals( "==", literalDescr.getEvaluator() );
        assertEquals( "value1", literalDescr.getText() );
        
        PredicateDescr predicateDescr = ( PredicateDescr ) column3.getDescrs().get( 1 );
        assertEquals( "field1", predicateDescr.getFieldName() );
        assertEquals( "var1", predicateDescr.getDeclaration() );
        assertEquals( "1==1", predicateDescr.getText() );
        
        ReturnValueDescr returnValueDescr = ( ReturnValueDescr ) column3.getDescrs().get( 2 );
        assertEquals( "field1", returnValueDescr.getFieldName() );
        assertEquals( "==", returnValueDescr.getEvaluator() );
        assertEquals( "1==1", returnValueDescr.getText() );
        
        FieldBindingDescr fieldBindingDescr = ( FieldBindingDescr ) column3.getDescrs().get( 3 );
        assertEquals( "field1", fieldBindingDescr.getFieldName() );
        assertEquals( "var1", fieldBindingDescr.getIdentifier());
        
        BoundVariableDescr boundVariableDescr = ( BoundVariableDescr ) column3.getDescrs().get( 4 );
        assertEquals( "field1", boundVariableDescr.getFieldName() );
        assertEquals( "==", boundVariableDescr.getEvaluator() );
        assertEquals( "var1", boundVariableDescr.getIdentifier());      
        
        NotDescr notDescr = ( NotDescr ) lhsDescr.getDescrs().get( 5 );
        assertEquals( 1, notDescr.getDescrs().size() );
        ColumnDescr columnDescr = ( ColumnDescr ) notDescr.getDescrs().get( 0 );
        assertEquals( "Bar", columnDescr.getObjectType() );
        
        ExistsDescr existsDescr = ( ExistsDescr ) lhsDescr.getDescrs().get( 6 );
        assertEquals( 1, existsDescr.getDescrs().size() );
        columnDescr = ( ColumnDescr ) existsDescr.getDescrs().get( 0 );
        assertEquals( "Bar", columnDescr.getObjectType() );
        
        andDescr = ( AndDescr ) lhsDescr.getDescrs().get( 7 );
        assertEquals( 2, andDescr.getDescrs().size() );
        orDescr = ( OrDescr ) andDescr.getDescrs().get( 0 );
        columnDescr = ( ColumnDescr ) orDescr.getDescrs().get( 0 );
        assertEquals( "Bar", columnDescr.getObjectType() );
        columnDescr = ( ColumnDescr ) andDescr.getDescrs().get( 1 );
        assertEquals( "Yada", columnDescr.getObjectType() );        
        
        orDescr = ( OrDescr ) lhsDescr.getDescrs().get( 8 );
        assertEquals( 2,orDescr.getDescrs().size() );
        andDescr = ( AndDescr ) orDescr.getDescrs().get( 0 );
        columnDescr = ( ColumnDescr ) andDescr.getDescrs().get( 0 );
        assertEquals( "Foo", columnDescr.getObjectType() );
        columnDescr = ( ColumnDescr ) orDescr.getDescrs().get( 1 );
        assertEquals( "Zaa", columnDescr.getObjectType() );        
    }    
    
}

