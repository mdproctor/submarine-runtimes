/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.lang;

import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;

/**
 * A mismatched token exception that properly resolves ID tokens
 * into soft keywords
 */
public class DroolsMismatchedTokenException extends MismatchedTokenException {
    private static final long serialVersionUID = -3708332833521751402L;
    private String            tokenText;

    public DroolsMismatchedTokenException() {
        super();
    }

    public DroolsMismatchedTokenException(int expecting,
                                          String tokenText,
                                          IntStream input) {
        super( expecting,
               input );
        this.tokenText = tokenText;
    }

    public String getTokenText() {
        return tokenText;
    }
    
    @Override
    public String toString() {
        return "DroolsMismatchedTokenException("+getUnexpectedType()+"!="+expecting+( tokenText != null ? "["+tokenText+"]" : "" )+")";
    }
}
