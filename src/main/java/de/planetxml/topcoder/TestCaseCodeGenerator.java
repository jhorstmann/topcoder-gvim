package de.planetxml.topcoder;

import com.topcoder.shared.problem.DataType;
import com.topcoder.shared.problem.TestCase;
import com.topcoder.shared.problem.InvalidTypeException;

import com.topcoder.client.contestant.ProblemComponentModel;

import java.util.Iterator;
import java.util.List;

public class TestCaseCodeGenerator extends CodeGenerator {

    public TestCaseCodeGenerator(ProblemComponentModel problem) {
        super(problem);
    }

    protected String encodeTestCases() {
        String code
            = "\n"
            + "    //#BEGIN Tests\n"
            + "    public static final void main(final String[] args) {\n"
            + "        long t1, t2;\n"
            + "        " + encodeDataType(returnType) + " received, expected;\n"
            ;

        for (int i=0; i<testCases.length; i++) {
            code += encodeTestCase(testCases[i]);
        }

        code += "    }\n";
        code += "    //#END Tests\n";

        return code;
    }

    private String encodeTestCase(TestCase testCase) {
        String code
            = "\n"
            + "        t1 = System.currentTimeMillis();\n"
            //+ "        received = " + encodeTestCaseCall(testCase) + ";\n"
            //+ "        t2 = System.currentTimeMillis();\n"
            //+ "        expected = " + encodeTestCaseOutput(testCase) + "; // " + testCase.getOutput() + "\n"
            + "        System.out.println(\"Receive : \" + " + encodeToStringCall(returnType, "received=" + encodeTestCaseCall(testCase)) + ");\n"
            + "        System.out.println(\"Expect  : \" + " + encodeToStringCall(returnType, "expected=" + encodeTestCaseOutput(testCase)) + ");\n"
            + "        System.out.println((" + encodeEqualsCall(returnType, "received", "expected") + " ? \"Passed\" : \"Failed\") + \" in \" + (double)(System.currentTimeMillis()-t1)/1000 + \" seconds\");\n"
            + "        System.out.println();\n"
            ;
        return code;
    }

    private String encodeTestCaseOutput(TestCase testCase) {
        return encodeDataTypeLiteral(returnType, new OutputParser(testCase.getOutput()).parse(), "            ");
        //return StringHelper.escape(String.valueOf(new OutputParser(testCase.getOutput()).parse()));
    }

    private String encodeToStringCall(DataType dataType, String param) {
        return dataType.getDimension() > 0
            ? ("java.util.Arrays.toString(" + param +  ")")
            : "String.valueOf(" + param + ")";
    }

    private String encodeEqualsCall(DataType dataType, String param1, String param2) {
        if (dataType.getDimension() > 0) {
            return "java.util.Arrays.equals(" + param1 + ", " + param2 +  ")";
        }
        else {
            String name = dataType.getBaseName();
            if ("int".equals(name) || "long".equals(name) || "double".equals(name)
                    || "float".equals(name) || "char".equals(name) || "short".equals(name) || "byte".equals(name) || "boolean".equals(name)) {
                return "(" + param1 + " == " + param2 + ")";
            }
            else {
                return param1 + ".equals(" + param2 + ")";
            }
        }
    }

    private String encodeTestCaseCall(TestCase testCase) {
        return "new " + className + "()." + methodName + "(" + encodeTestCaseInput(testCase) + ")";
    }

    private String encodeDataTypeLiteral(DataType dataType, Object value, String indent) {
        if (dataType.getDimension() >= 1) {
            if (value instanceof List) {
                String res = "new " + encodeDataType(dataType) + "{";

                DataType itemType;
                
                try {
                    itemType = dataType.reduceDimension(); // line 91
                }
                catch (InvalidTypeException e) {
                    //throw new IllegalStateException(e);
                    System.err.println("dataType : " + dataType);
                    System.err.println("dimension : " + dataType.getDimension());
                    System.err.println("description : " + dataType.getDescription());
                    e.printStackTrace();
                    // this seems to work instead
                    itemType = new DataType(dataType.getDescription().replaceFirst("\\[\\]", ""));
                }

                for (Iterator it=((List)value).iterator(); it.hasNext(); ) {
                    Object item = it.next();

                    res += "\n" + indent + encodeDataTypeLiteral(itemType, item, indent+"    ");

                    if (it.hasNext()) {
                        res += ",";
                    }
                }

                res += "}";

                return res;
            }
            else {
                throw new IllegalArgumentException("Expected " + encodeDataType(dataType) + ", got " + value);
            }
        }
        else {
            if (value == null) {
                return "null";
            }
            else if (value instanceof Boolean || value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
                return String.valueOf(value);
            }
            else if (value instanceof Character) {
                return StringHelper.quote(String.valueOf(((Character)value).charValue()), '\'');
            }
            else if (value instanceof String) {
                return StringHelper.quote((String)value);
            }
            else {
                throw new IllegalArgumentException("Can not encode " + value);
            }
        }
    }

    private String encodeTestCaseInput(TestCase testCase) {
        String[] input = testCase.getInput();
        String   code = "";

        assert(input.length == paramTypes.length);

        for (int i=0; i<input.length; i++) {
            code += "\n            ";
            code += encodeDataTypeLiteral(paramTypes[i], new OutputParser(input[i]).parse(), "                ");
            /*
            if (paramTypes[i].getDimension() == 0) {
                code += input[i];
            }
            else {
                code += "new " + encodeDataType(paramTypes[i]) + input[i];
                //code += "new " + encodeDataType(paramTypes[i]) + new OutputParser(input[i]).parse();
            }
            */
            if (i < paramTypes.length-1) {
                code += ",";
            }
        }
        return code;
    }
}
