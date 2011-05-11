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

package org.drools.audit;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.drools.WorkingMemory;
import org.drools.audit.event.LogEvent;
import org.drools.event.KnowledgeRuntimeEventManager;

import com.thoughtworks.xstream.XStream;

/**
 * A logger of events generated by a working memory.
 * It stores its information in a file that can be specified.
 * All the events logged are written to the file when the
 * writeToDisk() method is invoked.  The log will contain all
 * the events logged serialized to XML using XStream.
 * Every time a new logger is created, the old event log will
 * be overwritten.
 * 
 * TODO: make this class more scalable, for example
 *  - logging to several files if log becomes too large
 *  - automatically write updates to file at certain time intervals
 *  - ...
 */
public class WorkingMemoryFileLogger extends WorkingMemoryLogger {

    private List<LogEvent> events  = new ArrayList<LogEvent>();
    private String     fileName          = "event";
    private int        maxEventsInMemory = 1000;
    private int        nbOfFile          = 0;
    private boolean    split             = true;
    private boolean    initialized       = false;
    protected boolean terminate = false;

    public WorkingMemoryFileLogger() {
    }

    /**
     * Creates a new WorkingMemoryFileLogger for the given working memory.
     * @param workingMemoryEventManager
     */
    public WorkingMemoryFileLogger(final WorkingMemory workingMemory) {
        super( workingMemory );
    }

    public WorkingMemoryFileLogger(final KnowledgeRuntimeEventManager session) {
        super( session );
    }

    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        events  = (List<LogEvent>)in.readObject();
        fileName    = (String) in.readObject();
        maxEventsInMemory   = in.readInt();
        nbOfFile            = in.readInt();
        split               = in.readBoolean();
        initialized         = in.readBoolean();
        terminate = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(events);
        out.writeObject(fileName);
        out.writeInt(maxEventsInMemory);
        out.writeInt(nbOfFile);
        out.writeBoolean(split);
        out.writeBoolean(initialized);
        out.writeBoolean( terminate );
    }

    /**
     * Sets the name of the file the events are logged in.
     * No extensions should be given since .log is automatically appended
     * to the file name.
     * The default is an event.log file in the current working directory.
     * This can be a path relative to the current working directory
     * (e.g. "mydir/subDir/myLogFile"), or an absolute path 
     * (e.g. "C:/myLogFile").
     * 
     * @param fileName The name of the file the events should be logged in.
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * All events in the log are written to file.
     * The log is automatically cleared afterwards.
     */
    public void writeToDisk() {
        if (!initialized) {
            initializeLog();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(this.fileName + (this.nbOfFile == 0 ? ".log" : this.nbOfFile + ".log"), true );
            final XStream xstream = new XStream();
            List<LogEvent> eventsToWrite = null;
            synchronized (this.events) {
                eventsToWrite = new ArrayList<LogEvent>(this.events);
                clear();
            }
            for (LogEvent event : eventsToWrite) {
                fileWriter.write(xstream.toXML(event) + "\n");
            }
            if (split) {
                fileWriter.write("</object-stream>");
                this.nbOfFile++;
                initialized = false;
            }
        } catch ( final FileNotFoundException exc ) {
            throw new RuntimeException( "Could not create the log file.  Please make sure that directory that the log file should be placed in does exist." );
        } catch ( final Throwable t ) {
            t.printStackTrace( System.err );
        } finally {
            if( fileWriter != null ) { try { fileWriter.close(); } catch(Exception e) {} }
        }
        if(terminate) {
            terminateLog();
        }
    }
    
    private void initializeLog() {
        try {
            FileWriter writer = new FileWriter(this.fileName + (this.nbOfFile == 0 ? ".log" : this.nbOfFile + ".log"), false);
            writer.append("<object-stream>\n");
            writer.close();
            initialized = true;
        } catch ( final FileNotFoundException exc ) {
            throw new RuntimeException( "Could not create the log file.  Please make sure that directory that the log file should be placed in does exist." );
        } catch ( final Throwable t ) {
            t.printStackTrace( System.err );
        }
    }
    
    private void terminateLog() {
        try {
            FileWriter writer = new FileWriter(this.fileName + (this.nbOfFile == 0 ? ".log" : this.nbOfFile + ".log"), true);
            writer.append("</object-stream>\n");
            writer.close();
            terminate = false;
        } catch ( final FileNotFoundException exc ) {
            throw new RuntimeException( "Could not terminate the log file.  Please make sure that directory that the log file should be placed in does exist." );
        } catch ( final Throwable t ) {
            t.printStackTrace( System.err );
        }
    }

    /**
     * Clears all the events in the log.
     */
    private void clear() {
        synchronized (this.events) {
            this.events.clear();
        }
    }

    /**
     * Sets the maximum number of log events that are allowed in memory.
     * If this number is reached, all events are written to file.
     * The default is 1000.
     * 
     * @param maxEventsInMemory The maximum number of events in memory.
     */
    public void setMaxEventsInMemory(final int maxEventsInMemory) {
        this.maxEventsInMemory = maxEventsInMemory;
    }

    /**
     * @see org.drools.audit.WorkingMemoryLogger
     */
    public void logEventCreated(final LogEvent logEvent) {
        synchronized (this.events) {
            this.events.add( logEvent );
            if ( this.events.size() > this.maxEventsInMemory ) {
                writeToDisk();
            }
        }
    }
    
    public void setSplit(boolean split) {
        this.split = split;
    }
    
    public void stop() {
        terminate=true;
        writeToDisk();
    }

}
