package org.drools.compiler.rule.builder.dialect.asm;

import org.drools.core.WorkingMemory;
import org.drools.core.rule.Declaration;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.core.rule.builder.dialect.asm.ClassGenerator;
import org.drools.core.rule.builder.dialect.asm.InvokerDataProvider;
import org.drools.core.rule.builder.dialect.asm.PredicateGenerator;
import org.drools.core.rule.builder.dialect.asm.PredicateStub;
import org.drools.core.spi.CompiledInvoker;
import org.drools.core.spi.PredicateExpression;
import org.drools.core.spi.Tuple;
import org.mvel2.asm.Label;
import org.mvel2.asm.MethodVisitor;

import java.util.Map;

import static org.mvel2.asm.Opcodes.ACC_PRIVATE;
import static org.mvel2.asm.Opcodes.ACC_PUBLIC;
import static org.mvel2.asm.Opcodes.ACONST_NULL;
import static org.mvel2.asm.Opcodes.ALOAD;
import static org.mvel2.asm.Opcodes.ARETURN;
import static org.mvel2.asm.Opcodes.ASTORE;
import static org.mvel2.asm.Opcodes.ATHROW;
import static org.mvel2.asm.Opcodes.DUP;
import static org.mvel2.asm.Opcodes.GOTO;
import static org.mvel2.asm.Opcodes.IFNONNULL;
import static org.mvel2.asm.Opcodes.IRETURN;
import static org.mvel2.asm.Opcodes.MONITORENTER;
import static org.mvel2.asm.Opcodes.MONITOREXIT;
import static org.mvel2.asm.Opcodes.RETURN;

public class ASMPredicateStubBuilder extends AbstractASMPredicateBuilder {

    protected byte[] createPredicateBytecode(final RuleBuildContext ruleContext, final Map vars) {
        final InvokerDataProvider data = new InvokerContext(vars);
        final ClassGenerator generator = InvokerGenerator.createInvokerStubGenerator(data, ruleContext);
        createStubPredicate(generator, data, vars);
        return generator.generateBytecode();
    }

    private void createStubPredicate(final ClassGenerator generator, final InvokerDataProvider data, final Map vars) {
        generator.setInterfaces(PredicateStub.class, CompiledInvoker.class)
                .addField(ACC_PRIVATE, "predicate", PredicateExpression.class);

        generator.addMethod(ACC_PUBLIC, "createContext", generator.methodDescr(Object.class), new ClassGenerator.MethodBody() {
            public void body(MethodVisitor mv) {
                mv.visitInsn(ACONST_NULL);
                mv.visitInsn(ARETURN);
            }
        }).addMethod(ACC_PUBLIC, "evaluate", generator.methodDescr(Boolean.TYPE, Object.class, Tuple.class, Declaration[].class, Declaration[].class, WorkingMemory.class, Object.class), new String[]{"java/lang/Exception"}, new ClassGenerator.MethodBody() {
            public void body(MethodVisitor mv) {
                Label syncStart = new Label();
                Label syncEnd = new Label();
                Label l1 = new Label();
                Label l2 = new Label();
                mv.visitTryCatchBlock(syncStart, l1, l2, null);
                Label l3 = new Label();
                mv.visitTryCatchBlock(l2, l3, l2, null);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitInsn(DUP);
                mv.visitVarInsn(ASTORE, 7);
                // synchronized(this) {
                mv.visitInsn(MONITORENTER);
                mv.visitLabel(syncStart);
                getFieldFromThis("predicate", PredicateExpression.class);
                // if (predicate == null) ...
                Label ifNotInitialized = new Label();
                mv.visitJumpInsn(IFNONNULL, ifNotInitialized);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitVarInsn(ALOAD, 5);
                // ... PredicateGenerator.generate(this, tuple, declarations, declarations, workingMemory)
                invokeStatic(PredicateGenerator.class, "generate", null, PredicateStub.class, Tuple.class, Declaration[].class, Declaration[].class, WorkingMemory.class);
                mv.visitLabel(ifNotInitialized);
                mv.visitVarInsn(ALOAD, 7);
                mv.visitInsn(MONITOREXIT);
                mv.visitLabel(l1);
                mv.visitJumpInsn(GOTO, syncEnd);
                mv.visitLabel(l2);
                mv.visitVarInsn(ASTORE, 8);
                mv.visitVarInsn(ALOAD, 7);
                mv.visitInsn(MONITOREXIT);
                mv.visitLabel(l3);
                mv.visitVarInsn(ALOAD, 8);
                mv.visitInsn(ATHROW);
                mv.visitLabel(syncEnd);
                // } end of synchronized
                getFieldFromThis("predicate", PredicateExpression.class);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitVarInsn(ALOAD, 5);
                mv.visitVarInsn(ALOAD, 6);
                invokeInterface(PredicateExpression.class, "evaluate", Boolean.TYPE, Object.class, Tuple.class, Declaration[].class, Declaration[].class, WorkingMemory.class, Object.class);
                mv.visitInsn(IRETURN);
            }
        }).addMethod(ACC_PUBLIC, "setPredicate", generator.methodDescr(null, PredicateExpression.class), new ClassGenerator.MethodBody() {
            public void body(MethodVisitor mv) {
                putFieldInThisFromRegistry("predicate", PredicateExpression.class, 1);
                mv.visitInsn(RETURN);
            }
        });
    }
 }
