package org.drools.compiler.builder.impl;

import org.drools.compiler.compiler.PackageRegistry;
import org.drools.compiler.compiler.TypeDeclarationError;
import org.drools.compiler.lang.descr.AbstractClassTypeDeclarationDescr;
import org.drools.compiler.lang.descr.AnnotationDescr;
import org.drools.compiler.lang.descr.EnumDeclarationDescr;
import org.drools.compiler.lang.descr.EnumLiteralDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.QualifiedName;
import org.drools.compiler.lang.descr.TypeDeclarationDescr;
import org.drools.compiler.lang.descr.TypeFieldDescr;
import org.drools.core.base.TypeResolver;
import org.drools.core.factmodel.AnnotationDefinition;
import org.drools.core.factmodel.ClassDefinition;
import org.drools.core.factmodel.EnumClassDefinition;
import org.drools.core.factmodel.EnumLiteralDefinition;
import org.drools.core.factmodel.FieldDefinition;
import org.drools.core.factmodel.traits.Thing;
import org.drools.core.factmodel.traits.Traitable;
import org.drools.core.factmodel.traits.TraitableBean;
import org.drools.core.rule.TypeDeclaration;
import org.drools.core.util.ClassUtils;
import org.drools.core.util.asm.ClassFieldInspector;
import org.kie.api.definition.type.Key;
import org.kie.api.definition.type.Position;
import org.kie.api.definition.type.Role;

import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class ClassDefinitionFactory {

    protected KnowledgeBuilderImpl kbuilder;

    public ClassDefinitionFactory( KnowledgeBuilderImpl kbuilder ) {
        this.kbuilder = kbuilder;
    }



    /**
     * Generates a bean, and adds it to the composite class loader that
     * everything is using.
     */
    public ClassDefinition generateDeclaredBean( AbstractClassTypeDeclarationDescr typeDescr,
                                                 TypeDeclaration type,
                                                 PackageRegistry pkgRegistry,
                                                 List<TypeDefinition> unresolvedTypeDefinitions,
                                                 Map<String,AbstractClassTypeDeclarationDescr> unprocesseableDescrs ) {

        ClassDefinition def = createClassDefinition( typeDescr, type );

        boolean success = true;
        success &= wireAnnotationDefs( typeDescr, type, def, pkgRegistry.getTypeResolver() );
        success &= wireEnumLiteralDefs( typeDescr, type, def );
        success &= wireFields( typeDescr, type, def, pkgRegistry, unresolvedTypeDefinitions );

        if ( ! success ) {
            unprocesseableDescrs.put( typeDescr.getType().getFullName(), typeDescr );
        }
        // attach the class definition, it will be completed later
        type.setTypeClassDef( def );

        return def;
    }

    protected ClassDefinition createClassDefinition( AbstractClassTypeDeclarationDescr typeDescr, TypeDeclaration type ) {
        // extracts type, supertype and interfaces
        String fullName = typeDescr.getType().getFullName();

        if ( type.getKind().equals( TypeDeclaration.Kind.CLASS ) ) {
            TypeDeclarationDescr tdescr = (TypeDeclarationDescr) typeDescr;
            if (tdescr.getSuperTypes().size() > 1) {
                kbuilder.addBuilderResult(new TypeDeclarationError( typeDescr, "Declared class " + fullName + "  - has more than one supertype;" ) );
                return null;
            } else if ( tdescr.getSuperTypes().isEmpty() ) {
                tdescr.addSuperType("java.lang.Object");
            }
        }

        AnnotationDescr traitableAnn = typeDescr.getAnnotation(Traitable.class.getSimpleName());
        boolean traitable = traitableAnn != null;

        String[] fullSuperTypes = new String[typeDescr.getSuperTypes().size() + 1];
        int j = 0;
        for (QualifiedName qname : typeDescr.getSuperTypes()) {
            fullSuperTypes[j++] = qname.getFullName();
        }
        fullSuperTypes[j] = Thing.class.getName();

        List<String> interfaceList = new ArrayList<String>();
        interfaceList.add(traitable ? Externalizable.class.getName() : Serializable.class.getName());
        if (traitable) {
            interfaceList.add(TraitableBean.class.getName());
        }
        String[] interfaces = interfaceList.toArray(new String[interfaceList.size()]);

        // prepares a class definition
        ClassDefinition def;
        switch ( type.getKind() ) {
            case TRAIT:
                def = new ClassDefinition( fullName,
                                           Object.class.getName(),
                                           fullSuperTypes );
                break;
            case ENUM:
                def = new EnumClassDefinition( fullName,
                                               fullSuperTypes[0],
                                               null );
                break;
            case CLASS:
            default:
                def = new ClassDefinition( fullName,
                                           fullSuperTypes[0],
                                           interfaces );
                def.setTraitable( traitable, traitableAnn != null &&
                                             traitableAnn.getValue( "logical" ) != null &&
                                             Boolean.valueOf( traitableAnn.getValue( "logical" ) ) );
        }

        return def;
    }

    protected boolean wireAnnotationDefs( AbstractClassTypeDeclarationDescr typeDescr, TypeDeclaration type, ClassDefinition def, TypeResolver resolver ) {
        for ( String annotationName : typeDescr.getAnnotationNames() ) {
            Class annotation = TypeDeclarationUtils.resolveAnnotation( annotationName,
                                                                       resolver );
            if ( annotation != null && annotation.isAnnotation() ) {
                try {
                    AnnotationDefinition annotationDefinition = AnnotationDefinition.build( annotation,
                                                                                            typeDescr.getAnnotations().get(annotationName).getValueMap(),
                                                                                            resolver );
                    def.addAnnotation(annotationDefinition);
                } catch (NoSuchMethodException nsme) {
                    kbuilder.addBuilderResult(new TypeDeclarationError(typeDescr,
                                                                       "Annotated type " + typeDescr.getType().getFullName() +
                                                                       "  - undefined property in @annotation " +
                                                                       annotationName + ": " +
                                                                       nsme.getMessage() + ";"));
                }
            }
            if (annotation == null || annotation == Role.class) {
                def.addMetaData(annotationName, typeDescr.getAnnotation(annotationName).getSingleValue());
            }
        }
        return true;
    }

    protected boolean wireEnumLiteralDefs( AbstractClassTypeDeclarationDescr typeDescr, TypeDeclaration type, ClassDefinition def ) {
        // add enum literals, if appropriate
        if ( type.getKind() == TypeDeclaration.Kind.ENUM ) {
            for (EnumLiteralDescr lit : ((EnumDeclarationDescr) typeDescr).getLiterals()) {
                ( (EnumClassDefinition) def ).addLiteral(
                        new EnumLiteralDefinition( lit.getName(), lit.getConstructorArgs() )
                );
            }
        }
        return true;
    }

    protected boolean wireFields( AbstractClassTypeDeclarationDescr typeDescr,
                                  TypeDeclaration type,
                                  ClassDefinition def,
                                  PackageRegistry pkgRegistry,
                                  List<TypeDefinition> unresolvedTypeDefinitions ) {
        // fields definitions are created. will be used by subclasses, if any.
        // Fields are SORTED in the process
        if ( ! typeDescr.getFields().isEmpty() ) {
            if ( unresolvedTypeDefinitions != null && ! unresolvedTypeDefinitions.isEmpty() ) {
                for ( TypeFieldDescr fld : typeDescr.getFields().values() ) {
                    if ( unresolvedTypeDefinitions != null ) {
                        for ( TypeDefinition typeDef : unresolvedTypeDefinitions ) {
                            if ( fld.getPattern().getObjectType().equals( typeDef.getTypeClassName() ) ) {
                                return false;
                            }
                        }
                    }
                }
            }
            PriorityQueue<FieldDefinition> fieldDefs = sortFields( typeDescr.getFields(),
                                                                   pkgRegistry.getTypeResolver(),
                                                                   kbuilder );
            int n = fieldDefs.size();
            for (int k = 0; k < n; k++) {
                FieldDefinition fld = fieldDefs.poll();
                fld.setIndex( k );
                def.addField( fld );
            }
        }
        return true;
    }


    /**
     * Sorts a bean's fields according to the positional index metadata. The
     * order is as follows (i) as defined using the @position metadata (ii) as
     * resulting from the inspection of an external java superclass, if
     * applicable (iii) in declaration order, superclasses first
     */
    protected static PriorityQueue<FieldDefinition> sortFields( Map<String, TypeFieldDescr> flds,
                                                                TypeResolver typeResolver,
                                                                KnowledgeBuilderImpl kbuilder ) {
        PriorityQueue<FieldDefinition> queue = new PriorityQueue<FieldDefinition>(flds.size());
        int maxDeclaredPos = 0;
        int curr = 0;

        BitSet occupiedPositions = new BitSet(flds.size());
        for (TypeFieldDescr field : flds.values()) {
            int pos = field.getIndex();
            if (pos >= 0) {
                occupiedPositions.set(pos);
            }
            maxDeclaredPos = Math.max(maxDeclaredPos, pos);
        }

        for ( TypeFieldDescr field : flds.values() ) {

            String typeName = field.getPattern().getObjectType();
            String typeNameKey = typeName;

            String fullFieldType = kbuilder != null ?
                                   TypeDeclarationUtils.toBuildableType( typeNameKey, kbuilder.getRootClassLoader() ) :
                                   typeNameKey;

            FieldDefinition fieldDef = new FieldDefinition( field.getFieldName(),
                                                            fullFieldType );
            // field is marked as PK
            boolean isKey = field.getAnnotation(TypeDeclaration.ATTR_KEY) != null;
            fieldDef.setKey( isKey );

            fieldDef.setDeclIndex( field.getIndex() );
            if (field.getIndex() < 0) {
                int freePos = occupiedPositions.nextClearBit(0);
                if (freePos < maxDeclaredPos) {
                    occupiedPositions.set(freePos);
                } else {
                    freePos = maxDeclaredPos + 1;
                }
                fieldDef.setPriority(freePos * 256 + curr++);
            } else {
                fieldDef.setPriority(field.getIndex() * 256 + curr++);
            }
            fieldDef.setInherited( field.isInherited() );
            fieldDef.setRecursive(  field.isRecursive() );
            fieldDef.setInitExpr( TypeDeclarationUtils.rewriteInitExprWithImports( field.getInitExpr(), typeResolver ) );

            for (String annotationName : field.getAnnotationNames()) {
                Class annotation = TypeDeclarationUtils.resolveAnnotation( annotationName,
                                                                           typeResolver );
                if (annotation != null && annotation.isAnnotation()) {
                    try {
                        AnnotationDefinition annotationDefinition = AnnotationDefinition.build( annotation,
                                                                                                field.getAnnotations().get( annotationName ).getValueMap(),
                                                                                                typeResolver );
                        fieldDef.addAnnotation( annotationDefinition );
                    } catch ( NoSuchMethodException nsme ) {

                        kbuilder.addBuilderResult( new TypeDeclarationError( field,
                                                                             "Annotated field " + field.getFieldName() +
                                                                             "  - undefined property in @annotation " +
                                                                             annotationName + ": " + nsme.getMessage() + ";" ) );
                    }
                }
                if (annotation == null || annotation == Key.class || annotation == Position.class) {
                    fieldDef.addMetaData(annotationName, field.getAnnotation(annotationName).getSingleValue());
                }
            }

            queue.add(fieldDef);

        }

        return queue;
    }


    public static void populateDefinitionFromClass( ClassDefinition def, Class<?> concrete, boolean asTrait ) {
        try {
            def.setClassName( concrete.getName() );
            if ( concrete.getSuperclass() != null ) {
                def.setSuperClass( concrete.getSuperclass().getName() );
            }

            ClassFieldInspector inspector = new ClassFieldInspector(concrete);
            Map<String, Method> methods = inspector.getGetterMethods();
            Map<String, Method> setters = inspector.getSetterMethods();
            int j = 0;
            Map<String,TypeFieldDescr> fields = new HashMap<String,TypeFieldDescr>();
            for ( String fieldName : methods.keySet() ) {
                if ( asTrait && ( "core".equals(fieldName) || "fields".equals(fieldName) ) ) {
                    continue;
                }
                if ( !inspector.isNonGetter( fieldName ) && setters.keySet().contains( fieldName ) ) {

                    Position position = null;
                    if ( ! concrete.isInterface() ) {
                        try {
                            Field fld = concrete.getDeclaredField( fieldName );
                            position = fld.getAnnotation( Position.class );
                        } catch ( NoSuchFieldException nsfe ) {
                            // @Position can only annotate fields. This x means that a getter/setter pair was found with no field
                        }
                    }

                    Class ret = methods.get( fieldName ).getReturnType();
                    TypeFieldDescr field = new TypeFieldDescr(  );
                    field.setFieldName( fieldName );
                    field.setPattern( new PatternDescr( ret.getName() ) );
                    field.setIndex( position != null ? position.value() : -1 );
                    fields.put( fieldName, field );
                }
            }
            if ( ! fields.isEmpty() ) {
                PriorityQueue<FieldDefinition> fieldDefs = sortFields( fields,
                                                                       null,
                                                                       null );
                int n = fieldDefs.size();
                for (int k = 0; k < n; k++) {
                    FieldDefinition fld = fieldDefs.poll();
                    fld.setIndex( k );
                    def.addField( fld );
                }
            }

            Set<String> interfaces = new HashSet<String>();
            Collections.addAll( interfaces, def.getInterfaces() );
            for ( Class iKlass : ClassUtils.getAllImplementedInterfaceNames( concrete ) ) {
                interfaces.add(iKlass.getName());
            }
            def.setInterfaces( interfaces.toArray( new String[ interfaces.size() ] ) );

            def.setDefinedClass( concrete );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
