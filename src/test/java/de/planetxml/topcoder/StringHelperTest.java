package de.planetxml.topcoder;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class StringHelperTest extends TestCase {
    public void testReadLine1() throws IOException {
        Reader r = new StringReader("test");
        assertEquals("test", StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
    }

    public void testReadLine2() throws IOException {
        Reader r = new StringReader("test\n");
        assertEquals("test\n", StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
    }

    public void testReadLine3() throws IOException {
        Reader r = new StringReader("test\r\n");
        assertEquals("test\r\n", StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
    }

    public void testReadLine4() throws IOException {
        Reader r = new StringReader("test\nabc");
        assertEquals("test\n", StringHelper.readLine(r));
        assertEquals("abc"  , StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
        assertEquals(null  , StringHelper.readLine(r));
    }

    public void testEscape1() {
        assertEquals("abc", StringHelper.escape("abc"));
        assertEquals("\\n", StringHelper.escape("\n"));
        assertEquals("\\r", StringHelper.escape("\r"));
        assertEquals("\\t", StringHelper.escape("\t"));
        assertEquals("\\\"", StringHelper.escape("\""));
        assertEquals("'", StringHelper.escape("'"));
        assertEquals("\\'", StringHelper.escape("'", '\''));
    }
}
