package org.drools.io.impl;

import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.io.Resource;
import org.drools.io.internal.InternalResource;
import org.drools.util.ClassLoaderUtil;
import org.drools.util.ClassUtils;
import org.drools.util.StringUtils;

/**
 * Borrowed gratuitously from Spring under ASL2.0.
 *
 *+
 */

public class ClassPathResource extends BaseResource 
    implements
    InternalResource, Externalizable  {
    private String      path;
    private ClassLoader classLoader;
    private Class       clazz;
    private long        lastRead;
    
    public ClassPathResource() {
        
    }

    public ClassPathResource(String path) {
        this( path,
              null,
              null );
    }

    public ClassPathResource(String path,
                             Class clazz) {
        this( path,
              clazz,
              null );
    }

    public ClassPathResource(String path,
                             ClassLoader classLoader) {
        this( path,
              null,
              classLoader );
    }

    public ClassPathResource(String path,
                             Class clazz,
                             ClassLoader classLoader) {
        if ( path == null ) {
            throw new IllegalArgumentException( "path cannot be null" );
        }
        this.path = path;
        this.clazz = clazz;
        this.classLoader =  ClassLoaderUtil.getClassLoader( classLoader, clazz );        
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( this.path );
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        this.path = (String) in.readObject();
    }    

    /**
     * This implementation opens an InputStream for the given class path resource.
     * @see java.lang.ClassLoader#getResourceAsStream(String)
     * @see java.lang.Class#getResourceAsStream(String)
     */
    public InputStream getInputStream() throws IOException {
        InputStream is = null;
        if ( this.clazz != null ) {
            is = this.clazz.getResourceAsStream( this.path );
        } 
        
        if ( is == null ) {
            is = this.classLoader.getResourceAsStream( this.path );
        }

        if ( is == null ) {
            throw new FileNotFoundException( "'" + this.path + "' cannot be opened because it does not exist" );
        }
        this.lastRead = getLastModified();
        return is;
    }

    /**
     * This implementation returns a URL for the underlying class path resource.
     * @see java.lang.ClassLoader#getResource(String)
     * @see java.lang.Class#getResource(String)
     */
    public URL getURL() throws IOException {
        URL url = null;
        if ( this.clazz != null ) {
            url = this.clazz.getResource( this.path );
        } 
        
        if ( url == null ) {
            url = this.classLoader.getResource( this.path );
        }

        if ( url == null ) {
            throw new FileNotFoundException( "'" + this.path + "' cannot be opened because it does not exist" );
        }
        return url;
    }

    public boolean hasURL() {
        return true;
    }

    public long getLastModified() {
        try {
            URLConnection conn = getURL().openConnection();
            long date = conn.getLastModified();
            return date;
        } catch ( IOException e ) {
            throw new RuntimeException( "Unable to get LastMofified for ClasspathResource",
                                        e );
        }
    }

    public long getLastRead() {
        return this.lastRead;
    }

    public Reader getReader() throws IOException {
        return new InputStreamReader( getInputStream() );
    }
    
    public boolean isDirectory() {
        try {
            URL url = getURL();

            if ( !"file".equals( url.getProtocol() ) ) {
                return false;
            }

            File file = new File( StringUtils.toURI( url.toString() ).getSchemeSpecificPart() );
            
            return file.isDirectory();
        } catch ( Exception e ) {
            return false;
        }
    }

    public Collection<Resource> listResources() {
        try {
            URL url = getURL();

            if ( "file".equals( url.getProtocol() ) ) {                            
                File dir = new File( StringUtils.toURI( url.toString() ).getSchemeSpecificPart() );
                
                List<Resource> resources = new ArrayList<Resource>();
                
                for ( File file : dir.listFiles() ) {
                    resources.add( new FileSystemResource( file ) );
                }
                
                return resources;
            }
        } catch ( Exception e ) {
            // swollow as we'll throw an exception anyway            
        }
        
        throw new RuntimeException( "This Resource cannot be listed, or is not a directory" );
    }    

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public Class getClazz() {
        return this.clazz;
    }
    
    public boolean equals(Object object) {
        if ( object == null || !(object instanceof ClassPathResource) ) {
            return false;
        }

        ClassPathResource other = (ClassPathResource) object;
        if ( !this.path.equals( other.path ) ) {
            return false;
        }

        return this.clazz == other.clazz && this.classLoader == other.classLoader;
    }

    public int hashCode() {
        return this.path.hashCode();
    }

    public String toString() {
        return "[ClassPathResource path='" + this.path + "']";
    }


}
