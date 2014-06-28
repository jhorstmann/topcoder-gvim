package de.planetxml.topcoder;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.IOException;

public class CodeProcessorTest extends TestCase {
    public void testProcessEmpty() throws IOException {
        assertEquals("", new CodeProcessor("").process());
    }

    public void testProcessOneline() throws IOException {
        assertEquals("test", new CodeProcessor("test").process());
    }

    public void testProcess1() throws IOException {
        assertEquals("test\n", new CodeProcessor("test\n").process());
    }

    public void testProcess2() throws IOException {
        assertEquals("\ntest\n", new CodeProcessor("\ntest\n").process());
    }

    public void testProcess3() throws IOException {
        assertEquals("\n", new CodeProcessor("\n").process());
    }

    public void testProcess4() throws IOException {
        assertEquals("test\nabc\n", new CodeProcessor("test\nabc\n").process());
    }

    public void testRemovePrint1() throws IOException {
        assertEquals("", new CodeProcessor("System.out.println();").process());
    }

    public void testRemovePrint2() throws IOException {
        assertEquals("", new CodeProcessor("System.out.println();\n").process());
    }

    public void testRemovePrint3() throws IOException {
        assertEquals("test\n", new CodeProcessor("test\nSystem.out.println();\n").process());
    }

    public void testRemovePrint4() throws IOException {
        assertEquals("test\ntest", new CodeProcessor("test\nSystem.out.println();\ntest").process());
    }

    public void testRemovePrint5() throws IOException {
        assertEquals("test\ntest", new CodeProcessor("test\nSystem.out.println();System.out.println(\"aslhaslfhasdf\");\ntest").process());
    }

    public void testRemoveBlock1() throws IOException {
        assertEquals("test\ntest\n", new CodeProcessor("test\n  //#begin\nlkshdflkashdfklhasdf\n //#end\ntest\n").process());
    }

    public void testRemoveBlock2() throws IOException {
        assertEquals("test\n  abc  \ntest\n", new CodeProcessor("test\n  abc  \n//#begin\nlkshdflkashdfklhasdf\n //#end\ntest\n").process());
    }

    public void testRemoveBlockNested() throws IOException {
        assertEquals("test\ntest\n", new CodeProcessor("test\n  //#begin\n //#begin\nalsdhlaksdf\n//#end\nlkshdflkashdfklhasdf\n //#end\ntest\n").process());
    }

    public void testCommentInString() throws IOException {
        assertEquals("test(\"http://example.com\");", new CodeProcessor("test(\"http://example.com\");").process());
    }
}
