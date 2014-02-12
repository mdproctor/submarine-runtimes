package org.drools.core.definitions;

import org.drools.core.base.ClassFieldAccessorCache;
import org.drools.core.base.ClassFieldAccessorStore;
import org.drools.core.base.TypeResolver;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.factmodel.traits.TraitRegistry;
import org.drools.core.facttemplates.FactTemplate;
import org.drools.core.rule.DialectRuntimeRegistry;
import org.drools.core.rule.Function;
import org.drools.core.rule.ImportDeclaration;
import org.drools.core.rule.TypeDeclaration;
import org.drools.core.rule.WindowDeclaration;
import org.kie.api.definition.process.Process;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.runtime.rule.AccumulateFunction;
import org.kie.internal.definition.KnowledgePackage;

import java.io.Externalizable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InternalKnowledgePackage extends KnowledgePackage, Externalizable {

    void clear();
    void checkValidity();
    boolean isValid();

    void setNeedStreamMode();

    void resetErrors();
    void setError( String summary );

    Map<String, String> getGlobals();
    Map<String, Process> getRuleFlows();
    Map<String, TypeDeclaration> getTypeDeclarations();
    Map<String, Function> getFunctions();
    Map<String, ImportDeclaration> getImports();
    Map<String, WindowDeclaration> getWindowDeclarations();
    Map<String, AccumulateFunction> getAccumulateFunctions();

    Set<String> getEntryPointIds();
    Set<String> getStaticImports();

    void addFunction( Function function );
    void addGlobal( String identifier, Class<?> clazz );
    void addEntryPointId( String id );
    void addWindowDeclaration( WindowDeclaration window );
    void addRule( RuleImpl rule );
    void addProcess( Process process );
    void addTypeDeclaration( TypeDeclaration typeDecl );
    void addFactTemplate( FactTemplate factTemplate );
    void addImport( ImportDeclaration importDecl );
    void addAccumulateFunction( String name, AccumulateFunction function );
    void addStaticImport( String functionImport );

    void removeFunction( String functionName );
    void removeRuleFlow( String id );
    void removeRule( RuleImpl rule );

    boolean removeObjectsGeneratedFromResource(Resource resource);
    List<RuleImpl> removeRulesGeneratedFromResource(Resource resource);
    List<Function> removeFunctionsGeneratedFromResource(Resource resource);

    DialectRuntimeRegistry getDialectRuntimeRegistry();
    void setDialectRuntimeRegistry(DialectRuntimeRegistry dialectRuntimeRegistry);

    RuleImpl getRule( String name );
    FactType getFactType( String typeName );
    TypeDeclaration getTypeDeclaration( Class<?> clazz );
    TypeDeclaration getTypeDeclaration( String type );
    FactTemplate getFactTemplate( String name );

    ClassLoader getPackageClassLoader();

    TypeResolver getTypeResolver();
    void setTypeResolver( TypeResolver typeResolver );

    ClassFieldAccessorStore getClassFieldAccessorStore();

    void setClassFieldAccessorCache( ClassFieldAccessorCache classFieldAccessorCache );

    InternalKnowledgePackage deepCloneIfAlreadyInUse(ClassLoader classLoader);

    boolean hasTraitRegistry();
    TraitRegistry getTraitRegistry();

    boolean isEvent( Class clazz );
}
