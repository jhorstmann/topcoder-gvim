package de.planetxml.topcoder;

import java.util.*;
import java.lang.reflect.Array;

public class OutputFormatter {
    private Object  object;
    private char    openBrace;
    private char    closeBrace;
    private boolean createCode;

    public OutputFormatter(Object o) {
        this(o, '{', '}', false);
    }

    public OutputFormatter(Object o, boolean createCode) {
        this(o, '{', '}', createCode);
    }

    public OutputFormatter(Object o, char openBrace, char closeBrace) {
        this(o, openBrace, closeBrace, false);
    }

    public OutputFormatter(Object o, char openBrace, char closeBrace, boolean createCode) {
        this.object     = o;
        this.openBrace  = openBrace;
        this.closeBrace = closeBrace;
        this.createCode = createCode;
    }

    public String format() {
        return formatObject(object);
    }

    private String formatObject(Object o) {
        if (o == null) {
            return "null";
        }
        else if (o instanceof Boolean || o instanceof Byte || o instanceof Short || o instanceof Character || o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double) {
            if (createCode) {
                return "new " + o.getClass().getName() + "(" + String.valueOf(o) + ")";
            }
            else {
                return String.valueOf(o);
            }
        }
        else if (o.getClass().isArray()) {
            return formatArray(o);
        }
        else if (o instanceof Collection) {
            return formatCollection((Collection)o);
        }
        else {
            return null;
        }
    }

    private String formatArray(Object arr) {
        StringBuffer sb = new StringBuffer();

        if (createCode) {
            sb.append("new Object[] ");;
        }
        
        sb.append(openBrace);
        int len = Array.getLength(arr);
        for (int i=0; i<len; i++) {
            sb.append(formatObject(Array.get(arr, i)));
            if (i < len-1) {
                sb.append(", ");
            }
        }
        sb.append(closeBrace);

        return sb.toString();
    }

    private String formatCollection(Collection col) {
        StringBuffer sb = new StringBuffer();

        if (createCode) {
            sb.append("new Object[] ");;
        }
        
        sb.append(openBrace);
        for (Iterator it=col.iterator(); it.hasNext(); ) {
            sb.append(formatObject(it.next()));
            if (it.hasNext()) {
                sb.append(", ");
            }

        }
        sb.append(closeBrace);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(
            new OutputFormatter(
                new OutputParser("123.0").parse()).format());
        System.out.println(
            new OutputFormatter(
                new OutputParser("123.0").parse(), true).format());
        System.out.println(
            new OutputFormatter(
                new OutputParser("{1,2,3}").parse()).format());
        System.out.println(
            new OutputFormatter(
                new OutputParser("{1,2,3}").parse(), true).format());
        System.out.println(
            new OutputFormatter(new int[] {4,5,6}).format());
        System.out.println(
            new OutputFormatter(new int[] {4,5,6}, true).format());
    }
}
