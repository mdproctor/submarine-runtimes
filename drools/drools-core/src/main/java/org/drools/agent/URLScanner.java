package org.drools.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.RuleBase;
import org.drools.RuntimeDroolsException;
import org.drools.rule.Package;

public class URLScanner extends PackageProvider {

    //this is the URLs we are managing
    URL[]       urls;
    
    //this is only set if we are using a local cache - only fall back on this
    //when URL connection is not available.
    FileScanner localCacheFileScanner;
    
    //this is used to access the remote resources
    IHttpClient httpClient = new HttpClientImpl();
    
    //a record of the last updated URL timestamps
    Map         lastUpdated = new HashMap();

    File        localCacheDir;

    void configure(Properties config) {
        List uriList = RuleAgent.list( config.getProperty( RuleAgent.URLS ) );
        urls = new URL[uriList.size()];
        for ( int i = 0; i < uriList.size(); i++ ) {
            String url = (String) uriList.get( i );
            try {
                urls[i] = new URL( url );
            } catch ( MalformedURLException e ) {
                throw new RuntimeException( "The URL " + url + " is not valid.",
                                            e );
            }
        }

        //if we have a local cache, check its all kosher
        String localCache = config.getProperty( RuleAgent.LOCAL_URL_CACHE );
        if ( localCache != null ) {
            localCacheDir = new File( localCache );
            if ( !localCacheDir.isDirectory() ) {
                throw new RuntimeDroolsException( "The local cache dir " + localCache + " is a file, not a directory." );
            }
            this.localCacheFileScanner = new FileScanner();
            this.localCacheFileScanner.setFiles( getFiles( urls, localCacheDir ) );
        }
    }

    File[] getFiles(URL[] urls, File cacheDir) {
        File[] fs = new File[urls.length];
        for ( int i = 0; i < urls.length; i++ ) {
            URL u = urls[i];
            File f = getLocalCacheFileForURL( cacheDir, u );
            fs[i] = f;
        }
        return fs;
    }

    static File getLocalCacheFileForURL(File cacheDir, URL u) {
        File f;
        try {
            f = new File( cacheDir,
                          URLEncoder.encode(  u.toExternalForm(), "UTF-8" ) );
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeDroolsException( e );
        }
        return f;
    }



    void updateRuleBase(RuleBase rb, boolean removeExistingPackages) {
        Package[] changes = null;
        try {
            changes = getChangeSet();
            applyChanges( rb, removeExistingPackages, changes );
        } catch ( IOException e ) {
            if (this.localCacheFileScanner != null) {
                localCacheFileScanner.updateRuleBase( rb, removeExistingPackages );
            }
            //TODO: add in logging
        }
    }
    
    private Package[] getChangeSet() throws IOException {
        if ( this.urls == null ) return new Package[0];
        List list = new ArrayList();
        for ( int i = 0; i < urls.length; i++ ) {
            URL u = urls[i];
            if ( hasChanged( u, this.lastUpdated) ) {
                Package p = readPackage( u );
                if ( p == null ) return null;
                list.add( p );
                if (localCacheDir != null) {
                    writeLocalCacheCopy(p, u, localCacheDir);
                }
            }
        }
        return (Package[]) list.toArray( new Package[list.size()] );
    }

    private void writeLocalCacheCopy(Package p, URL u, File localCacheDir) {
        File local = getLocalCacheFileForURL( localCacheDir, u );
        if (local.exists()) local.delete();
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(local));
            out.writeObject( p );
            out.flush();
            out.close();
        } catch (IOException e) {
            //TODO: log this event
        }
        
    }

    private Package readPackage(URL u) throws IOException {
        return httpClient.fetchPackage( u );        
    }

    private boolean hasChanged(URL u, Map updates) throws IOException {
        LastUpdatedPing pong = httpClient.checkLastUpdated( u );
        if (pong.isError()) {
            throw new IOException("Was unable to reach server.");
            //TODO: log stuff here.
        }
        
        String url = u.toExternalForm();
        if ( !updates.containsKey( url ) ) {
            updates.put( url,
                         new Long( pong.lastUpdated ) );
            return true;
        } else {
            Long last = (Long) updates.get( url );
            if ( last.intValue() < pong.lastUpdated ) {
                updates.put( url,
                             new Long( pong.lastUpdated ) );
                return true;
            } else {
                return false;
            }
        }        
    }    
    
    public String toString() {
        String s = "URLScanner monitoring URLs: ";
        if (this.urls != null) {
            for ( int i = 0; i < urls.length; i++ ) {
                URL url = urls[i];
                s = s + " " + url.toExternalForm();
            }
        }
        if (this.localCacheDir != null) {
            s = s + " with local cache dir of " + this.localCacheDir.getPath();
        }
        return s;
    }

}
