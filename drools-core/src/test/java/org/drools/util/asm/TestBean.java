package org.drools.util.asm;

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

public class TestBean {

    private String  something;
    private int     number;
    private boolean blah;
    private Object[] objArray;

    public boolean isBlah() {
        return this.blah;
    }

    public void setBlah(final boolean blah) {
        this.blah = blah;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public String getSomething() {
        return this.something;
    }

    public void setSomething(final String something) {
        this.something = something;
    }

    public String fooBar() {
        return "fooBar";
    }

    public long getLongField() {
        return 424242;
    }

    public Long getOtherLongField() {
        return new Long( 42424242 );
    }

    public Object[] getObjArray() {
        return objArray;
    }

    public void setObjArray(Object[] objArray) {
        this.objArray = objArray;
    }

}