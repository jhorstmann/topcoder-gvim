package de.planetxml.topcoder;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CodeProcessor {
    private static final Pattern begin   = Pattern.compile("\\s*//#\\s*begin\\b.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern end     = Pattern.compile("\\s*//#\\s*end\\b.*"  , Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern comment = Pattern.compile("\\s*//.*", Pattern.DOTALL);

    private static final Pattern print   = Pattern.compile("\\s*System.(out|err).print(ln)?\\(.*\\);\\s*");

    private BufferedReader reader;

    public CodeProcessor(final String code) {
        this(new StringReader(code));
    }

    public CodeProcessor(final Reader reader) {
        this.reader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
    }

    private String processPrint(String line) {
        Matcher m = print.matcher(line);
        if (!m.matches()) {
            return line;
        }
        else {
            return "";
        }
    }

    private String processComment(String line) {
        Matcher m = comment.matcher(line);
        if (m.matches()) {
            return "";
        }
        else {
            return line;
        }
    }

    public String process() throws IOException {
        StringWriter result = new StringWriter();
        int nesting = 0;

        String line;

        while ((line = StringHelper.readLine(reader)) != null) {
            Matcher m = begin.matcher(line);
            if (m.matches()) {
                nesting++;
            }
            else {
                m = end.matcher(line);
                if (m.matches()) {
                    if (nesting == 0) {
                        throw new IllegalArgumentException("Invalid nesting");
                    }
                    nesting--;
                }
                else if (nesting == 0) {
                    line = processComment(line);
                    if (line != null && line.length() > 0) {
                        line = processPrint(line);
                        if (line != null && line.length() > 0) {
                            result.write(line);
                        }
                    }
                }
            }
        }

        return result.toString();
    }
}
