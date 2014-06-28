package de.planetxml.topcoder;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import de.planetxml.topcoder.OutputParser;

import java.util.*;

public class OutputParserTest extends TestCase {
    public void testPeek() {
        assertEquals('a', new OutputParser("abc").peek());
    }

    public void testSkipWhite() {
        OutputParser p = new OutputParser("  abc");
        p.skipWhite();
        assertEquals('a', p.peek());
    }

    public void testPeekAndNext() {
        OutputParser p = new OutputParser("abc");
        assertEquals('a', p.peek());
        assertEquals('a', p.next());
        assertEquals('b', p.peek());
    }

    public void testDigits() {
        assertEquals("1", new OutputParser("1").parseDigits());
    }

    public void testDigits2() {
        assertEquals("123", new OutputParser("123").parseDigits());
    }

    public void testDigits3() {
        assertEquals("0123", new OutputParser("0123").parseDigits());
    }

    public void testDigits4() {
        assertEquals("0", new OutputParser("0").parseDigits());
    }

    public void testDigits5() {
        assertEquals("1", new OutputParser("1").parseDigits());
    }

    public void testDigits6() {
        OutputParser p = new OutputParser("123abc");
        assertEquals("123", p.parseDigits());
        assertEquals('a', p.peek());
    }

    public void testDigits7() {
        OutputParser p = new OutputParser("123.");
        assertEquals("123", p.parseDigits());
        assertEquals('.', p.peek());
    }

    public void testDigits8() {
        assertEquals(null, new OutputParser("abc").parseDigits());
    }

    public void testDigits9() {
        assertEquals(null, new OutputParser(".").parseDigits());
    }

    public void testDigits10() {
        assertEquals("1", new OutputParser("1.").parseDigits());
    }

    public void testDigits11() {
        assertEquals("123", new OutputParser("123.").parseDigits());
    }

    public void testDigits12() {
        assertEquals("123", new OutputParser("123...").parseDigits());
    }

    public void testEscape() {
        assertEquals('\n', new OutputParser("\\n").parseEscape());
    }

    public void testString() {
        assertEquals(String.valueOf(""), new OutputParser("\"\"").parse());
    }

    public void testString2() {
        assertEquals(String.valueOf("abc"), new OutputParser("\"abc\"").parse());
    }

    public void testString3() {
        assertEquals(String.valueOf(""), new OutputParser("      \"\"    ").parse());
    }

    public void testNumber() {
        assertEquals(Long.valueOf(1), new OutputParser("1").parseNumber());
    }

    public void testNumber2() {
        assertEquals(Long.valueOf(10), new OutputParser("10").parseNumber());
    }

    public void testNumber3() {
        assertEquals(Double.valueOf(10), new OutputParser("10.0").parseNumber());
    }

    public void testNumber4() {
        assertEquals(Double.valueOf(10.1), new OutputParser("10.1").parseNumber());
    }

    public void testNumber5() {
        assertEquals(Double.valueOf(-10.1), new OutputParser("-10.1").parseNumber());
    }

    public void testNumber6() {
        assertEquals(Double.valueOf(-10.123), new OutputParser("-10.123").parseNumber());
    }

    public void testNumber7() {
        assertEquals(Long.valueOf(10), new OutputParser("1e1").parseNumber());
    }

    public void testNumber8() {
        assertEquals(Long.valueOf(100), new OutputParser("1e2").parseNumber());
    }

    public void testNumber9() {
        assertEquals(Long.valueOf(10000000000l), new OutputParser("1e10").parseNumber());
    }

    public void testNumber10() {
        assertEquals(Double.valueOf(1.0), new OutputParser("1.00").parseNumber());
    }

    public void testNumber11() {
        assertEquals(Double.valueOf(1.1e1), new OutputParser("1.1e1").parseNumber());
    }

    public void testNumber12() {
        assertEquals(Double.valueOf(1.1e10), new OutputParser("1.1e10").parseNumber());
    }

    public void testEmptyList() {
        assertEquals(Collections.EMPTY_LIST, new OutputParser("  {  }  ").parse());
    }

    public void testCharacter1() {
        assertEquals(new Character('a'), new OutputParser("'a'").parse());
    }

    public void testCharacter2() {
        assertEquals(new Character('\\'), new OutputParser("'\\\\'").parse());
    }

    public void testCharacter3() {
        assertEquals(new Character('\n'), new OutputParser("'\\n'").parse());
    }

    public void testNumberList() {
        assertEquals(
                Arrays.asList(
                    new Number[] {Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)}),
                new OutputParser("{ 1, 2, 3 }").parse());
    }

    public void testNumberList2() {
        assertEquals(
                Arrays.asList(
                    new Double[] {Double.valueOf(12.3), Double.valueOf(4.5), Double.valueOf(67.123)}),
                new OutputParser("{ 12.3, 4.5  , 67.123 }").parse());
    }

    public void testStringList() {
        assertEquals(
                Arrays.asList(
                    new String[] {"abc", "\n", "xyz\"123"}),
                new OutputParser("{ \"abc\", \"\\n\"  , \"xyz\\\"123\" }").parseList());
    }

    public void testParse1() {
        assertEquals(Long.valueOf(2), new OutputParser("2").parseNumber());
    }

    public void testParse2() {
        assertEquals(Long.valueOf(14), new OutputParser("14").parse());
    }
}
