package org.drools.decisiontable.parser;
/*
 * Copyright 2005 JBoss Inc
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





import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.drools.decisiontable.parser.xls.ExcelParser;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 */
public class RulesheetUtil
{

    /**
     * Utility method showing how to get a rule sheet listener from a stream.
     */
    public static RuleSheetListener getRuleSheetListener(InputStream stream) throws IOException
    {
        Map listners = new HashMap( );
        RuleSheetListener listener = new RuleSheetListener( );
        listners.put( ExcelParser.DEFAULT_RULESHEET_NAME,
                      listener );
        ExcelParser parser = new ExcelParser( listners );
        parser.parseFile( stream );
        stream.close( );
        return listener;
    }
}