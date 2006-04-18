package org.drools.decisiontable.model;
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





import java.util.StringTokenizer;

/**
 * @author <a href="mailto:ricardo.rojas@bluesoft.cl"> Ricardo Rojas </a>
 * 
 */
public class Duration extends DRLElement
    implements
    DRLJavaEmitter
{

    public String _snippet;

    /**
     * @param snippet
     *            The snippet to set.
     */
    public void setSnippet(String snippet)
    {
        _snippet = snippet;
    }

    public String getSnippet()
    {
        return _snippet;
    }

	public void renderDRL(DRLOutput out) {
		if (isCommented()) out.writeLine("#" + getComment());
		out.writeLine("\tduration " + _snippet);
		
	}
}