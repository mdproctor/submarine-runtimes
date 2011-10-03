package org.drools.rule.builder.dialect.asm;

import org.drools.*;
import org.drools.common.*;
import org.drools.reteoo.*;
import org.drools.rule.*;
import org.drools.spi.*;
import org.mvel2.asm.*;

import java.util.*;

import static org.mvel2.asm.Opcodes.*;

public class PredicateGenerator extends InvokerGenerator {
    public static void generate(final PredicateStub stub,
                                final Object object,
                                final Tuple tuple,
                                final Declaration[] previousDeclarations,
                                final Declaration[] localDeclarations,
                                final WorkingMemory workingMemory,
                                final Object context) {

        final LeftTuple leftTuple = (LeftTuple)tuple;
        final String[] previousDeclarationTypes = stub.getDeclarationTypes();
        final String[] localDeclarationTypes = stub.getLocalDeclarationTypes();
        final String[] globals = stub.getGlobals();
        final String[] globalTypes = stub.getGlobalTypes();

        // Sort declarations based on their offset, so it can ascend the tuple's parents stack only once
        final List<DeclarationMatcher> declarationMatchers = matchDeclarationsToTuple(previousDeclarationTypes, previousDeclarations, leftTuple);

        final ClassGenerator generator = createInvokerClassGenerator(stub, workingMemory)
                .setInterfaces(PredicateExpression.class, CompiledInvoker.class);

        generator.addMethod(ACC_PUBLIC, "createContext", generator.methodDescr(Object.class), new ClassGenerator.MethodBody() {
            public void body(MethodVisitor mv) {
                mv.visitInsn(ACONST_NULL);
                mv.visitInsn(ARETURN);
            }
        }).addMethod(ACC_PUBLIC, "evaluate", generator.methodDescr(Boolean.TYPE, Object.class, Tuple.class, Declaration[].class, Declaration[].class, WorkingMemory.class, Object.class), new String[]{"java/lang/Exception"}, new EvaluateMethod() {
            public void body(MethodVisitor mv) {
                objAstorePos = 9;

                int[] previousDeclarationsParamsPos = new int[previousDeclarations.length];

                mv.visitVarInsn(ALOAD, 2);
                cast(LeftTuple.class);
                mv.visitVarInsn(ASTORE, 7); // LeftTuple

                LeftTuple currentLeftTuple = leftTuple;                
                for (DeclarationMatcher matcher : declarationMatchers) {
                    int i = matcher.getOriginalIndex();
                    previousDeclarationsParamsPos[i] = objAstorePos;

                    currentLeftTuple = traverseTuplesUntilDeclaration(currentLeftTuple, i, matcher.getRootDistance(), 3, 7, 8);

                    mv.visitVarInsn(ALOAD, 3);
                    push(i);
                    mv.visitInsn(AALOAD); // declarations[i]
                    mv.visitVarInsn(ALOAD, 5); // workingMemory

                    mv.visitVarInsn(ALOAD, 7);
                    invokeInterface(LeftTuple.class, "getHandle", InternalFactHandle.class);
                    invokeInterface(InternalFactHandle.class, "getObject", Object.class); // tuple.getHandle().getObject()

                    storeObjectFromDeclaration(previousDeclarations[i], previousDeclarationTypes[i]);
                }

                int[] localDeclarationsParamsPos = parseDeclarations(localDeclarations, localDeclarationTypes, 4, 2, 5, false);

                // @{ruleClassName}.@{methodName}(@foreach{previousDeclarations}, @foreach{localDeclarations}, @foreach{globals})
                StringBuilder predicateMethodDescr = new StringBuilder("(");
                for (int i = 0; i < previousDeclarations.length; i++) {
                    load(previousDeclarationsParamsPos[i]); // previousDeclarations[i]
                    predicateMethodDescr.append(typeDescr(previousDeclarationTypes[i]));
                }
                for (int i = 0; i < localDeclarations.length; i++) {
                    load(localDeclarationsParamsPos[i]); // localDeclarations[i]
                    predicateMethodDescr.append(typeDescr(localDeclarationTypes[i]));
                }

                // @foreach{type : globalTypes, identifier : globals} @{type} @{identifier} = ( @{type} ) workingMemory.getGlobal( "@{identifier}" );
                parseGlobals(globals, globalTypes, 5, predicateMethodDescr);

                predicateMethodDescr.append(")Z");
                mv.visitMethodInsn(INVOKESTATIC, stub.getInternalRuleClassName(), stub.getMethodName(), predicateMethodDescr.toString());
                mv.visitInsn(IRETURN);
            }
        });

        stub.setPredicate(generator.<PredicateExpression>newInstance());
    }
}
