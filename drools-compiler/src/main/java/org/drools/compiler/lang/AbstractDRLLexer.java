/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.compiler.lang;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognizerSharedState;

public abstract class AbstractDRLLexer extends Lexer implements DRLLexer {
    public AbstractDRLLexer() { }

    public AbstractDRLLexer(CharStream input) {
        super(input);
    }

    public AbstractDRLLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }
}
