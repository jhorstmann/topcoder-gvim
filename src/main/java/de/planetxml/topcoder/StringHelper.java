package de.planetxml.topcoder;

import java.io.IOException;
import java.io.Reader;

public class StringHelper {
    public static String readLine(Reader reader) throws IOException {
        int ch = reader.read();
        if (ch == -1) {
            return null;
        }
        else {
            StringBuilder sb = new StringBuilder(256);
            sb.append((char)ch);
            while (ch != '\n') {
                ch = reader.read();
                if (ch == -1) {
                    break;
                }
                else {
                    sb.append((char)ch);
                }
            }
            return sb.toString();
        }
    }

    public static String quote(String str) {
        return quote(str, '"');
    }

    public static String quote(String str, char quote) {
        return quote + escape(str, quote) + quote;
    }

    public static String escape(String str) {
        return escape(str, '"');
    }

    public static String escape(String str, char quote) {
        StringBuffer sb = new StringBuffer();
        int len = str.length();
        for (int i=0; i<len; i++) {
            char ch = str.charAt(i);
            if (ch == '\t') {
                sb.append("\\t");
            }
            else if (ch == '\r') {
                sb.append("\\r");
            }
            else if (ch == '\n') {
                sb.append("\\n");
            }
            else if (ch == '\\') {
                sb.append("\\\\");
            }
            else if (ch == quote) {
                sb.append('\\');
                sb.append(quote);
            }
            else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String parse(String str) {
        return new Parser(str).parseString();
    }
}
