package de.planetxml.topcoder;

import java.util.*;

public class OutputParser extends Parser {
    public OutputParser(CharSequence in) {
        super(in);
    }

    public Object parse() {
        skipWhite();
        int ch = peek();
        if (ch == '{') {
            return parseList();
        }
        else if (ch == '\'') {
            return parseCharacter();
        }
        else {
            return parseStringOrNumber();
        }
    }

    public Character parseCharacter() {
        consume('\'');
        int ch = peek();
        if (ch == '\\') {
            ch = (char)parseEscape();
        }
        else {
            ch = next();
        }
        consume('\'');

        return Character.valueOf((char)ch);
    }

    public List parseList() {
        consume('{');
        List result = new LinkedList();
        int  ch;
        do {
            skipWhite();
            ch = peek();
            switch (ch) {
                case '}':
                    next();
                    return result;
                case '"':
                    result.add(parseString());
                    break;
                case '\'':
                    result.add(parseCharacter());
                    break;
                case '-':
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    result.add(parseNumber());
                    break;
                default:
                    return null;
            }
            skipWhite();
            ch = peek();
            if (ch == ',') {
                next();
            }
        }
        while (ch == ',');

        return result;
    }
}
