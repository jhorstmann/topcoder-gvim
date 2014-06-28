package de.planetxml.topcoder;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class EditorTestSuite extends TestCase {
    public static Test suite() {
        TestSuite ts = new TestSuite();
        ts.addTestSuite(OutputParserTest.class);
        ts.addTestSuite(MessageTest.class);
        ts.addTestSuite(CodeProcessorTest.class);
        ts.addTestSuite(StringHelperTest.class);

        return ts;
    }
}
