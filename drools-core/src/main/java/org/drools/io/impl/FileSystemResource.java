package org.drools.io.impl;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.drools.core.util.StringUtils;
import org.drools.io.Resource;
import org.drools.io.internal.InternalResource;

/**
 * Borrowed gratuitously from Spring under ASL2.0.
 *
 */
public class FileSystemResource  extends BaseResource implements InternalResource, Externalizable {
    private File file;
    private long lastRead = -1;

    public FileSystemResource() {
        
    }
    
    /**
     * Create a new FileSystemResource from a File handle.
     * <p>Note: When building relative resources via {@link #createRelative},
     * the relative path will apply <i>at the same directory level</i>:
     * e.g. new File("C:/dir1"), relative path "dir2" -> "C:/dir2"!
     * If you prefer to have relative paths built underneath the given root
     * directory, use the {@link #FileSystemResource(String) constructor with a file path}
     * to append a trailing slash to the root path: "C:/dir1/", which
     * indicates this directory as root for all relative paths.
     * @param file a File handle
     */
    public FileSystemResource(File file) {
        if ( file == null ) {
            throw new IllegalArgumentException( "File must not be null" );
        }
        this.file = new File( StringUtils.cleanPath(file.getPath()) );
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( this.file );
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        this.file = (File) in.readObject();
    }

    /**
     * Create a new FileSystemResource from a file path.
     * <p>Note: When building relative resources via {@link #createRelative},
     * it makes a difference whether the specified resource base path here
     * ends with a slash or not. In the case of "C:/dir1/", relative paths
     * will be built underneath that root: e.g. relative path "dir2" ->
     * "C:/dir1/dir2". In the case of "C:/dir1", relative paths will apply
     * at the same directory level: relative path "dir2" -> "C:/dir2".
     * @param path a file path
     */
    public FileSystemResource(String path) {
        if ( path == null ) {
            throw new IllegalArgumentException( "Path must not be null" );
        }
        this.file = new File(StringUtils.cleanPath(path));
    }
    
    /**
     * This implementation opens a FileInputStream for the underlying file.
     * @see java.io.FileInputStream
     */
    public InputStream getInputStream() throws IOException {
        this.lastRead = getLastModified();
        return new FileInputStream(this.file);
    }
    
    public Reader getReader() throws IOException {
        return new InputStreamReader( getInputStream() );
    }    
    
    public File getFile() {
        return this.file;
    }       
    
    public boolean isDirectory() {
        return this.file.isDirectory();
    }
    
    public Collection<Resource> listResources() {
        File[] files = this.file.listFiles();        
        List<Resource> resources = new ArrayList<Resource>();
        
        for ( File file : files ) {
            resources.add( new FileSystemResource( file ) );
        }
        
        return resources;
    }
    
    /**
     * This implementation returns a URL for the underlying file.
     * @see java.io.File#toURI()
     */
    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }
    
    public boolean hasURL() {
        return true;
    }   
    
    public long getLastModified() {
        long date = this.file.lastModified();
        return date;
    }     
    
    public long getLastRead() {
        return this.lastRead;
    }
    
    public String toString() {
        return "[FileResource file='" + this.file.toString() + "']";
    }
    
    public boolean equals(Object object) {
        if ( object == null ) {
            return false;
        }
        
        return ( object == this || ( object instanceof FileSystemResource && file.equals( ((FileSystemResource)object).file )  ) );
    }
    
    public int hashCode() {
        return this.file.hashCode();
    }
}
