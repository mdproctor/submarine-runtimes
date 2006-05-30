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
package org.drools.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.drools.asm.Label;
import org.drools.asm.MethodVisitor;
import org.drools.asm.Opcodes;

/**
 * A node that represents a LOOKUPSWITCH instruction.
 * 
 * @author Eric Bruneton
 */
public class LookupSwitchInsnNode extends AbstractInsnNode {

    /**
     * Beginning of the default handler block.
     */
    public Label dflt;

    /**
     * The values of the keys. This list is a list of {@link Integer} objects.
     */
    public List  keys;

    /**
     * Beginnings of the handler blocks. This list is a list of {@link Label}
     * objects.
     */
    public List  labels;

    /**
     * Constructs a new {@link LookupSwitchInsnNode}.
     * 
     * @param dflt beginning of the default handler block.
     * @param keys the values of the keys.
     * @param labels beginnings of the handler blocks. <tt>labels[i]</tt> is
     *        the beginning of the handler block for the <tt>keys[i]</tt> key.
     */
    public LookupSwitchInsnNode(final Label dflt,
                                final int[] keys,
                                final Label[] labels) {
        super( Opcodes.LOOKUPSWITCH );
        this.dflt = dflt;
        this.keys = new ArrayList( keys == null ? 0 : keys.length );
        this.labels = new ArrayList( labels == null ? 0 : labels.length );
        if ( keys != null ) {
            for ( int i = 0; i < keys.length; ++i ) {
                this.keys.add( new Integer( keys[i] ) );
            }
        }
        if ( labels != null ) {
            this.labels.addAll( Arrays.asList( labels ) );
        }
    }

    public void accept(final MethodVisitor mv) {
        final int[] keys = new int[this.keys.size()];
        for ( int i = 0; i < keys.length; ++i ) {
            keys[i] = ((Integer) this.keys.get( i )).intValue();
        }
        final Label[] labels = new Label[this.labels.size()];
        this.labels.toArray( labels );
        mv.visitLookupSwitchInsn( this.dflt,
                                  keys,
                                  labels );
    }

    public int getType() {
        return AbstractInsnNode.LOOKUPSWITCH_INSN;
    }
}
