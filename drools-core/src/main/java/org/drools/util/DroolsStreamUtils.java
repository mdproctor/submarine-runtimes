package org.drools.util;

import org.drools.common.DroolsObjectOutputStream;
import org.drools.common.DroolsObjectInputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sg0521861
 * Date: Mar 27, 2008
 * Time: 8:56:14 AM
 * Provide Drools specific streaming helper routines
 */
public class DroolsStreamUtils {

    /**
     * This routine would stream out the give object uncompressed and store the streamed contents in
     * the return byte array.  The output contents could only be read by the corresponding "streamIn"
     * method of this class.
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] streamOut(Object object) throws IOException {
        return streamOut(object, false);
    }

    /**
     * This routine would stream out the give object, uncompressed or compressed depending on the given flag,
     * and store the streamed contents in the return byte array. The output contents could only be read by
     * the corresponding "streamIn" method of this class.
     * @param object
     * @param compressed
     * @return
     * @throws IOException
     */
    public static byte[] streamOut(Object object, boolean compressed) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        streamOut(bytes, object, compressed);
        bytes.flush();
        bytes.close();
        return bytes.toByteArray();
    }

    /**
     * This method would stream out the given object to the given output stream uncompressed.
     * The output contents could only be read by the corresponding "streamIn" method of this class.
     * @param out
     * @param object
     * @throws IOException
     */
    public static void streamOut(OutputStream out, Object object) throws IOException {
        streamOut(out, object, false);
    }

    /**
     * This method would stream out the given object to the given output stream uncompressed or compressed
     * depending on the given flag.  The output contents could only be read by the corresponding "streamIn"
     * methods of this class.
     * @param out
     * @param object
     * @throws IOException
     */
    public static void streamOut(OutputStream out, Object object, boolean compressed) throws IOException {
        if (compressed) {
            out = new GZIPOutputStream(out);
        }
        try {
            new DroolsObjectOutputStream(out).writeObject(object);
            out.flush();
        } finally {
            if (compressed) {
                out.close();
            }
        }
    }

    /**
     * This method reads the contents from the given byte array and returns the object.  It is expected that
     * the contents in the given buffer was not compressed, and the content stream was written by the corresponding
     * streamOut methods of this class.
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(byte[] bytes) throws IOException, ClassNotFoundException {
        return streamIn(bytes, null);
    }

    /**
     * This method reads the contents from the given byte array and returns the object.  It is expected that
     * the contents in the given buffer was not compressed, and the content stream was written by the corresponding
     * streamOut methods of this class.
     * @param bytes
     * @param classLoader
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(byte[] bytes, ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        return streamIn(bytes, classLoader, false);
    }

    /**
     * This method reads the contents from the given byte array and returns the object.  The contents in the given
     * buffer could be compressed or uncompressed depending on the given flag.  It is assumed that the content
     * stream was written by the corresponding streamOut methods of this class.
     * @param bytes
     * @param compressed
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(byte[] bytes, boolean compressed) throws IOException, ClassNotFoundException {
        return streamIn(new ByteArrayInputStream(bytes), null, compressed);
    }

    /**
     * This method reads the contents from the given byte array and returns the object.  The contents in the given
     * buffer could be compressed or uncompressed depending on the given flag.  It is assumed that the content
     * stream was written by the corresponding streamOut methods of this class.
     * @param bytes
     * @param classLoader
     * @param compressed
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(byte[] bytes, ClassLoader classLoader, boolean compressed)
            throws IOException, ClassNotFoundException {
        return streamIn(new ByteArrayInputStream(bytes), classLoader, compressed);
    }

    /**
     * This method reads the contents from the given input stream and returns the object.  It is expected that
     * the contents in the given stream was not compressed, and it was written by the corresponding
     * streamOut methods of this class.
     * @param in
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(InputStream in) throws IOException, ClassNotFoundException {
        return streamIn(in, null, false);
    }

    /**
     * This method reads the contents from the given input stream and returns the object.  It is expected that
     * the contents in the given stream was not compressed, and it was written by the corresponding
     * streamOut methods of this class.
     * @param in
     * @param classLoader
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(InputStream in, ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        return streamIn(in, classLoader, false);
    }

    /**
     * This method reads the contents from the given input stream and returns the object.  The contents in the given
     * stream could be compressed or uncompressed depending on the given flag.  It is assumed that the content
     * stream was written by the corresponding streamOut methods of this class.
     * @param in
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object streamIn(InputStream in, ClassLoader classLoader, boolean compressed)
            throws IOException, ClassNotFoundException {
        if (compressed)
            in  = new GZIPInputStream(in);
        return new DroolsObjectInputStream(in, classLoader).readObject();
     }
}
