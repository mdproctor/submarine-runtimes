package org.drools.base;
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



import junit.framework.Assert;
import junit.framework.TestCase;

public class FieldImplTest extends TestCase {
    FieldImpl field1;
    FieldImpl field2;
    FieldImpl field3;
    FieldImpl field4;
    FieldImpl field5;

    protected void setUp() throws Exception {
        super.setUp();
        this.field1 = new FieldImpl( null );
        this.field2 = new FieldImpl( null );
        this.field3 = new FieldImpl( "A" );
        this.field4 = new FieldImpl( "A" );
        this.field5 = new FieldImpl( "B" );

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        Assert.assertEquals( field1,
                             field1 );
        Assert.assertEquals( field1,
                             field2 );
        Assert.assertEquals( field3,
                             field3 );
        Assert.assertEquals( field3,
                             field4 );
        Assert.assertFalse( field1.equals( field3 ) );
        Assert.assertFalse( field3.equals( field1 ) );
        Assert.assertFalse( field3.equals( field5 ) );
    }

    /*
     * Test method for 'org.drools.base.FieldImpl.hashCode()'
     */
    public void testHashCode() {
        Assert.assertEquals( field1.hashCode(),
                             field1.hashCode() );
        Assert.assertEquals( field1.hashCode(),
                             field2.hashCode() );
        Assert.assertEquals( field3.hashCode(),
                             field3.hashCode() );
        Assert.assertEquals( field3.hashCode(),
                             field4.hashCode() );
        Assert.assertFalse( field1.hashCode() == field3.hashCode() );
        Assert.assertFalse( field3.hashCode() == field1.hashCode() );
        Assert.assertFalse( field3.hashCode() == field5.hashCode() );
    }

    /*
     * Test method for 'org.drools.base.FieldImpl.equals(Object)'
     */
    public void testEqualsObject() {

    }

}