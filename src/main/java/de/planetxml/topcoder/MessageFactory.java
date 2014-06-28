package de.planetxml.topcoder;

import java.util.regex.*;

public class MessageFactory {
    private MessageFactory() { }

    public static Message special(String name) {
        return new Message(name);
    }

    public static Message auth(String password) {
        return new Message(Message.AUTH, password);
    }

    public static Message reply(int seqno, String args) {
        return new Message(seqno, args);
    }

    public static Message command(int bufno, String name, int seqno, String args) {
        return new Message(bufno, name, Message.COMMAND, seqno, args);
    }

    public static Message function(int bufno, String name, int seqno, String args) {
        return new Message(bufno, name, Message.FUNCTION, seqno, args);
    }

    public static Message parse(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }
        else if (message.startsWith(Message.ACCEPT)) {
            return special(Message.ACCEPT);
        }
        else if (message.startsWith("AUTH ")) {
            return auth(message.substring(5));
        }
        else if (message.startsWith(Message.DISCONNECT)) {
            return special(Message.DISCONNECT);
        }
        else if (message.startsWith(Message.DETACH)) {
            return new Message(Message.DETACH);
        }
        else if (message.startsWith(Message.REJECT)) {
            return new Message(Message.REJECT);
        }

        Pattern p = Pattern.compile("(-?\\d+)(:(\\w+)([!/=])(\\d+))?( (.+))?");
        Matcher m = p.matcher(message);
        if (m.matches()) {
            int    bufno = Integer.parseInt(m.group(1));
            String args  = m.group(7);

            if (m.group(2) != null) {
                return new Message(
                        bufno,
                        m.group(3),
                        m.group(4).charAt(0),
                        Integer.parseInt(m.group(5)),
                        args);
            }
            else {
                return new Message(bufno, args);
            }
        }
        else {
            throw new IllegalArgumentException("Cannot parse message " + message);
        }
    }
}
