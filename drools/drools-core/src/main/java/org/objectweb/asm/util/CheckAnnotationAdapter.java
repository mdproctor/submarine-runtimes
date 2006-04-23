/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2005 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.objectweb.asm.util;

import org.drools.asm.AnnotationVisitor;
import org.drools.asm.Type;

/**
 * An {@link AnnotationVisitor} that checks that its methods are properly used.
 * 
 * @author Eric Bruneton
 */
public class CheckAnnotationAdapter implements AnnotationVisitor {

    private AnnotationVisitor av;

    private boolean named;

    private boolean end;

    public CheckAnnotationAdapter(final AnnotationVisitor av) {
        this(av, true);
    }

    CheckAnnotationAdapter(
        final AnnotationVisitor av,
        final boolean named)
    {
        this.av = av;
        this.named = named;
    }

    public void visit(final String name, final Object value) {
        checkEnd();
        checkName(name);
        if (!(value instanceof Byte || value instanceof Boolean
                || value instanceof Character || value instanceof Short
                || value instanceof Integer || value instanceof Long
                || value instanceof Float || value instanceof Double
                || value instanceof String || value instanceof Type
                || value instanceof byte[] || value instanceof boolean[]
                || value instanceof char[] || value instanceof short[]
                || value instanceof int[] || value instanceof long[]
                || value instanceof float[] || value instanceof double[]))
        {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        av.visit(name, value);
    }

    public void visitEnum(
        final String name,
        final String desc,
        final String value)
    {
        checkEnd();
        checkName(name);
        CheckMethodAdapter.checkDesc(desc, false);
        if (value == null) {
            throw new IllegalArgumentException("Invalid enum value");
        }
        av.visitEnum(name, desc, value);
    }

    public AnnotationVisitor visitAnnotation(
        final String name,
        final String desc)
    {
        checkEnd();
        checkName(name);
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(av.visitAnnotation(name, desc));
    }

    public AnnotationVisitor visitArray(final String name) {
        checkEnd();
        checkName(name);
        return new CheckAnnotationAdapter(av.visitArray(name), false);
    }

    public void visitEnd() {
        checkEnd();
        end = true;
        av.visitEnd();
    }

    private void checkEnd() {
        if (end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }

    private void checkName(final String name) {
        if (named && name == null) {
            throw new IllegalArgumentException("Annotation value name must not be null");
        }
    }
}
