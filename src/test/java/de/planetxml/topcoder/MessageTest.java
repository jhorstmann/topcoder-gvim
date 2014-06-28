package de.planetxml.topcoder;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import de.planetxml.topcoder.Message;

import java.util.*;

public class MessageTest extends TestCase {
    public void testCommand() {
        Message m = MessageFactory.parse("11:showBalloon!123 \"text\"");
        System.out.println(m);
        assertEquals(11, m.getBufno());
        assertEquals("showBalloon", m.getName());
        assertEquals(Message.COMMAND, m.getType());
        assertEquals(123, m.getSeqno());
        assertEquals("\"text\"", m.getStringArgs());
        assertEquals("text", m.getStringArgsDecoded());
    }

    public void testFunction() {
        Message m = MessageFactory.parse("11:getLength/123");
        assertEquals(11, m.getBufno());
        assertEquals("getLength", m.getName());
        assertEquals(Message.FUNCTION, m.getType());
        assertEquals(123, m.getSeqno());
    }

    public void testEvent() {
        Message m = MessageFactory.parse("11:keyCommand=123 \"S-F2\"");
        assertEquals(11, m.getBufno());
        assertEquals("keyCommand", m.getName());
        assertEquals(Message.EVENT, m.getType());
        assertEquals(123, m.getSeqno());
        assertEquals("\"S-F2\"", m.getStringArgs());
        assertEquals("S-F2", m.getStringArgsDecoded());
    }

    public void testAuth() {
        Message m = MessageFactory.parse("AUTH passwort");
        assertEquals("AUTH", m.getName());
        assertEquals(Message.SPECIAL, m.getType());
    }
}
