/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

package org.drools.core.reteoo.test.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.MismatchedNotSetException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

/**
 * Helper class that generates DroolsParserException with user friendly error
 * messages.
 * 
 * @see DroolsParserException
 */
public class DroolsParserExceptionFactory {
    public final static String MISMATCHED_TOKEN_MESSAGE_COMPLETE = "Line %1$d:%2$d mismatched input '%3$s' expecting '%4$s'%5$s";
    public final static String MISMATCHED_TOKEN_MESSAGE_PART = "Line %1$d:%2$d mismatched input '%3$s'%4$s";
    public final static String MISMATCHED_TREE_NODE_MESSAGE_COMPLETE = "Line %1$d:%2$d mismatched tree node '%3$s' expecting '%4$s'%5$s";
    public final static String MISMATCHED_TREE_NODE_MESSAGE_PART = "Line %1$d:%2$d mismatched tree node '%3$s'%4$s";
    public final static String NO_VIABLE_ALT_MESSAGE = "Line %1$d:%2$d no viable alternative at input '%3$s'%4$s";
    public final static String EARLY_EXIT_MESSAGE = "Line %1$d:%2$d required (...)+ loop did not match anything at input '%3$s'%4$s";
    public final static String MISMATCHED_SET_MESSAGE = "Line %1$d:%2$d mismatched input '%3$' expecting set '%4$s'%5$s.";
    public final static String MISMATCHED_NOT_SET_MESSAGE = "Line %1$d:%2$d mismatched input '%3$' expecting set '%4$s'%5$s";
    public final static String FAILED_PREDICATE_MESSAGE = "Line %1$d:%2$d rule '%3$s' failed predicate: {%4$s}?%5$s";
    public final static String TRAILING_SEMI_COLON_NOT_ALLOWED_MESSAGE = "Line %1$d:%2$d trailing semi-colon not allowed%3$s";
    public final static String PARSER_LOCATION_MESSAGE_COMPLETE = " in %1$s %2$s";
    public final static String PARSER_LOCATION_MESSAGE_PART = " in %1$s";

    private String[] tokenNames = null;
    private Stack<Map<DroolsParaphraseTypes, String>> paraphrases = null;

    /**
     * DroolsParserErrorMessages constructor.
     *
     * @param tokenNames
     *            tokenNames generated by ANTLR
     * @param paraphrases
     *            paraphrases parser structure
     */
    public DroolsParserExceptionFactory(String[] tokenNames,
            Stack<Map<DroolsParaphraseTypes, String>> paraphrases) {
        this.tokenNames = tokenNames;
        this.paraphrases = paraphrases;
    }

    /**
     * This method creates a DroolsParserException for trailing semicolon
     * exception, full of information.
     *
     * @param line
     *            line number
     * @param column
     *            column position
     * @param offset
     *            char offset
     * @return DroolsParserException filled.
     */
    public DroolsParserException createTrailingSemicolonException(int line,
            int column, int offset) {
        String message = String
                .format(
                        DroolsParserExceptionFactory.TRAILING_SEMI_COLON_NOT_ALLOWED_MESSAGE,
                        line, column, formatParserLocation());

        return new DroolsParserException("ERR 104", message, line, column,
                offset, null);
    }

    /**
     * This method creates a DroolsParserException full of information.
     *
     * @param e
     *            original exception
     * @return DroolsParserException filled.
     */
    public DroolsParserException createDroolsException(RecognitionException e) {
        List<String> codeAndMessage = createErrorMessage(e);
        return new DroolsParserException(codeAndMessage.get(1), codeAndMessage
                .get(0), e.line, e.charPositionInLine, e.index, e);
    }

    /**
     * This will take a RecognitionException, and create a sensible error
     * message out of it
     */
    private List<String> createErrorMessage(RecognitionException e) {
        List<String> codeAndMessage = new ArrayList<String>(2);
        String message = "";
        if (e instanceof MismatchedTokenException) {
            MismatchedTokenException mte = (MismatchedTokenException) e;
            if (tokenNames != null && mte.expecting >= 0 && mte.expecting < tokenNames.length) {
                message = String
                        .format(
                                DroolsParserExceptionFactory.MISMATCHED_TOKEN_MESSAGE_COMPLETE,
                                e.line, e.charPositionInLine,
                                getBetterToken(e.token),
                                getBetterToken(mte.expecting),
                                formatParserLocation());
                codeAndMessage.add(message);
                codeAndMessage.add("ERR 102");
            } else {
                message = String
                        .format(
                                DroolsParserExceptionFactory.MISMATCHED_TOKEN_MESSAGE_PART,
                                e.line, e.charPositionInLine,
                                getBetterToken(e.token), formatParserLocation());
                codeAndMessage.add(message);
                codeAndMessage.add("ERR 102");
            }
        } else if (e instanceof MismatchedTreeNodeException) {
            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException) e;
            if (mtne.expecting >= 0 && mtne.expecting < tokenNames.length) {
                message = String
                        .format(
                                DroolsParserExceptionFactory.MISMATCHED_TREE_NODE_MESSAGE_COMPLETE,
                                e.line, e.charPositionInLine,
                                getBetterToken(e.token),
                                getBetterToken(mtne.expecting),
                                formatParserLocation());
                codeAndMessage.add(message);
                codeAndMessage.add("ERR 106");
            } else {
                message = String
                        .format(
                                DroolsParserExceptionFactory.MISMATCHED_TREE_NODE_MESSAGE_PART,
                                e.line, e.charPositionInLine,
                                getBetterToken(e.token), formatParserLocation());
                codeAndMessage.add(message);
                codeAndMessage.add("ERR 106");
            }
        } else if (e instanceof NoViableAltException) {
            // NoViableAltException nvae = (NoViableAltException) e;
            message = String.format(
                    DroolsParserExceptionFactory.NO_VIABLE_ALT_MESSAGE, e.line,
                    e.charPositionInLine, getBetterToken(e.token),
                    formatParserLocation());
            codeAndMessage.add(message);
            codeAndMessage.add("ERR 101");
        } else if (e instanceof EarlyExitException) {
            // EarlyExitException eee = (EarlyExitException) e;
            message = String.format(
                    DroolsParserExceptionFactory.EARLY_EXIT_MESSAGE, e.line,
                    e.charPositionInLine, getBetterToken(e.token),
                    formatParserLocation());
            codeAndMessage.add(message);
            codeAndMessage.add("ERR 105");
        } else if (e instanceof MismatchedSetException) {
            MismatchedSetException mse = (MismatchedSetException) e;
            message = String.format(
                    DroolsParserExceptionFactory.MISMATCHED_SET_MESSAGE,
                    e.line, e.charPositionInLine, getBetterToken(e.token),
                    mse.expecting, formatParserLocation());
            codeAndMessage.add(message);
            codeAndMessage.add("ERR 107");
        } else if (e instanceof MismatchedNotSetException) {
            MismatchedNotSetException mse = (MismatchedNotSetException) e;
            message = String.format(
                    DroolsParserExceptionFactory.MISMATCHED_NOT_SET_MESSAGE,
                    e.line, e.charPositionInLine, getBetterToken(e.token),
                    mse.expecting, formatParserLocation());
            codeAndMessage.add(message);
            codeAndMessage.add("ERR 108");
        } else if (e instanceof FailedPredicateException) {
            FailedPredicateException fpe = (FailedPredicateException) e;
            message = String.format(
                    DroolsParserExceptionFactory.FAILED_PREDICATE_MESSAGE,
                    e.line, e.charPositionInLine, fpe.ruleName,
                    fpe.predicateText, formatParserLocation());
            codeAndMessage.add(message);
            codeAndMessage.add("ERR 103");
        }
        if (codeAndMessage.get(0).length() == 0) {
            codeAndMessage.add("?????");
        }
        return codeAndMessage;
    }

    /**
     * This will take Paraphrases stack, and create a sensible location
     */
    private String formatParserLocation() {
        StringBuilder sb = new StringBuilder();
        if (paraphrases != null){
            for (Map<DroolsParaphraseTypes, String> map : paraphrases) {
                for (Entry<DroolsParaphraseTypes, String> activeEntry : map
                        .entrySet()) {
                    if (activeEntry.getValue().length() == 0) {
                        sb.append(String.format(PARSER_LOCATION_MESSAGE_PART,
                                getLocationName(activeEntry.getKey())));
                    } else {
                        sb.append(String.format(PARSER_LOCATION_MESSAGE_COMPLETE,
                                getLocationName(activeEntry.getKey()), activeEntry
                                        .getValue()));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string based on Paraphrase Type
     *
     * @param type
     *            Paraphrase Type
     * @return a string representing the
     */
    private String getLocationName(DroolsParaphraseTypes type) {
        switch (type) {
        case TESTCASE:
            return "TestCase";
        case SETUP:
            return "Setup";
        case TEARDOWN:
            return "TearDown";
        case TEST:
            return "Test";
        case STEP:
            return "Step";
        case COMMAND:
            return "Command";
        default:
            return "";
        }
    }

    /**
     * Helper method that creates a user friendly token definition
     *
     * @param token
     *            token
     * @return user friendly token definition
     */
    private String getBetterToken(Token token) {
        if (token == null){
            return "";
        }
        return getBetterToken(token.getType(), token.getText());
    }

    /**
     * Helper method that creates a user friendly token definition
     *
     * @param tokenType
     *            token type
     * @return user friendly token definition
     */
    private String getBetterToken(int tokenType) {
        return getBetterToken(tokenType, null);
    }

    /**
     * Helper method that creates a user friendly token definition
     *
     * @param tokenType
     *            token type
     * @param defaultValue
     *            default value for identifier token, may be null
     * @return user friendly token definition
     */
    private String getBetterToken(int tokenType, String defaultValue) {
        switch (tokenType) {
        default:
            return ( tokenType < 0 || tokenType > tokenNames.length ) ? "<EOF>"
                    : tokenNames[tokenType];
        }
    }
}
