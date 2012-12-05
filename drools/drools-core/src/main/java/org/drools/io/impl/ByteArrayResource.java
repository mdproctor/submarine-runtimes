/*
 * Copyright 2010 JBoss Inc
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

package org.drools.io.impl;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.drools.io.internal.InternalResource;
import org.kie.io.Resource;

public class ByteArrayResource extends BaseResource
    implements
    InternalResource,
    Externalizable {

    private byte[] bytes;

    @Override
    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        super.readExternal( in );
        bytes = (byte[]) in.readObject();
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal( out );
        out.writeObject( bytes );
    }
    
    public ByteArrayResource(byte[] bytes) {
        if ( bytes == null || bytes.length == 0 ) {
            throw new IllegalArgumentException( "bytes cannot be null" );
        }
        this.bytes = bytes;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream( this.bytes );
    }
    
    public Reader getReader() throws IOException {
        return new InputStreamReader( getInputStream() );
    }
    
    public boolean hasURL() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException( "byte[] cannot be resolved to URL" );
    }
    
    public long getLastModified() {
        throw new IllegalStateException( "reader does have a modified date" );
    }
    
    public long getLastRead() {
        throw new IllegalStateException( "reader does have a modified date" );
    }
    
    public boolean isDirectory() {
        return false;
    }

    public Collection<Resource> listResources() {
        throw new RuntimeException( "This Resource cannot be listed, or is not a directory" );
    }

    public boolean equals(Object object) {
        return (object == this || (object instanceof ByteArrayResource
                && Arrays.equals( ((ByteArrayResource) object).bytes, this.bytes )));
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }
    
    public String toString() {
        return "[ByteArrayResource resource=" + this.bytes + "]";
    }


}
