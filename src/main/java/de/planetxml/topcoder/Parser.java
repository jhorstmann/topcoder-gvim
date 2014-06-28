package de.planetxml.topcoder;

import java.util.regex.*;

public class Parser {
    CharSequence input;
    int          position;

    public Parser(CharSequence in) {
        this(in, 0);
    }

    public Parser(CharSequence in, int pos) {
        input    = in;
        position = pos;
    }

    int peek() {
        return input.length() > position ? input.charAt(position) : -1;
    }

    int next() {
        return input.length() > position ? input.charAt(position++) : -1;
    }

    void skipWhite() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

    void consume(char ch) {
        int tmp = next();
        if (tmp != ch) {
            throw new IllegalStateException("Expected '" + ch + "', got '" + StringHelper.escape(String.valueOf((char)tmp), '\'') + "' at " + position);
        }
    }

    public int getPosition() {
        return position;
    }

    public Object parseStringOrNumber() {
        int ch = peek();
        switch (ch) {
            case '"':
                return parseString();
            case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                return parseNumber();
            default:
                return null;
        }
    }

    public String parseString() {
        consume('"');
        StringBuffer sb = new StringBuffer();
        while (true) {
            int ch = peek();
            if (ch == '\\') {
                sb.append((char)parseEscape());
            }
            else if (ch == '"') {
                next();
                break;
            }
            else if (ch >= 32 && (ch < 127 || ch > 160)) {
                sb.append((char)ch);
                next();
            }
            else {
                throw new IllegalArgumentException("Invalid char code " + ch);
            }
        }
        return sb.toString();
    }

    public int parseEscape() {
        consume('\\');
        int ch = peek();
        if (ch == 't') {
            next();
            return '\t';
        }
        else if (ch == 'r') {
            next();
            return '\r';
        }
        else if (ch == 'n') {
            next();
            return '\n';
        }
        else if (ch == '"') {
            next();
            return '"';
        }
        else {
            next();
            return ch;
        }
    }

    public Number parseNumber() {
        Pattern p = Pattern.compile("-?(?:0|[1-9][0-9]*)(\\.[0-9]+)?(?:[eE][0-9]+)?");
        Matcher m = p.matcher(input.subSequence(position, input.length()));
        if (m.lookingAt()) {
            position += m.end();
            if (m.group(1) != null) {
                return Double.valueOf(m.group());
            }
            else {
                return Long.valueOf((long)Double.parseDouble(m.group()));
            }
        }
        else {
            return null;
        }
    }

    /**
     * @deprecated
     */
    public Number parseNumberOld() {
        String n = parseNumberInternal();
        return n == null ? null : Double.valueOf(n);
    }

    /**
     * @deprecated
     */
    private String parseNumberInternal() {
        StringBuffer sb = new StringBuffer();
        int ch = peek();

        // optional sign
        if (ch == '-') {
            sb.append((char)ch);
            next();
            ch = peek();
        }

        if (ch == '0') {
            sb.append((char)ch);
            next();
        }
        else if (ch >= '1' && ch <= '9') {
            sb.append(parseDigits());
        }
        else {
            return null;
        }

        ch = peek();

        if (ch == '.') {
            // optional Fraction
            sb.append((char)ch);
            next();
            sb.append(parseDigits());

            ch = peek();
        }

        // optional exponent
        if (ch == 'e' || ch == 'E') {
            sb.append((char)ch);
            next();
            sb.append(parseDigits());
        }

        // should never throw a NumberFormatException
        return sb.toString();
    }

    /**
     * @deprecated
     */
    public String parseDigits() {
        StringBuffer sb = new StringBuffer();
        int ch = peek();
        if (ch >= '0' && ch <= '9') {
            sb.append((char)ch);
            next();
            while (true) {
                ch = peek();
                if (ch >= '0' && ch <= '9') {
                    sb.append((char)ch);
                    next();
                }
                else {
                    break;
                }
            }
        }
        else {
            return null;
        }

        return sb.toString();
    }
}
