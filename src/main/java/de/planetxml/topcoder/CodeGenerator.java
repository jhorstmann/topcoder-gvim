package de.planetxml.topcoder;

import com.topcoder.shared.problem.DataType;
import com.topcoder.shared.problem.TestCase;

import com.topcoder.client.contestant.ProblemComponentModel;

public class CodeGenerator {
    protected String     className;
    protected String     methodName;

    protected DataType[] paramTypes;
    protected String[]   paramNames;
    protected DataType   returnType;
    protected TestCase[] testCases;

    public CodeGenerator(ProblemComponentModel problem) {
        className  = problem.getClassName();
        methodName = problem.getMethodName();

        paramTypes = problem.getParamTypes();
        paramNames = problem.getParamNames();
        returnType = problem.getReturnType();
        testCases  = problem.getTestCases();
    }

    public String generate() {
        String code
            = encodeImports()
            + "public class " + className + " {\n"
            + "    public " + encodeDataType(returnType) + " " + methodName + "(" + encodeParameters() + ") {\n"
            + "    }\n"
            + encodeTestCases()
            + "}\n"
            ;

        return code;
    }

    protected String encodeImports() {
        String code
            = "import static java.awt.geom.Point2D.distance;\n"
            + "import static java.awt.geom.Point2D.distanceSq;\n"
            //+ "import static java.awt.geom.Line2D.linesIntersect;\n";
            //+ "import static java.awt.geom.Line2D.ptLineDist;\n";
            //+ "import static java.awt.geom.Line2D.ptLineDistSq;\n";
            //+ "import static java.awt.geom.Line2D.ptSegDist;\n";
            //+ "import static java.awt.geom.Line2D.ptSegDistSq;\n";
            //+ "\n"
            //+ "import static java.lang.Math.max;\n"
            //+ "import static java.lang.Math.min;\n"
            //+ "import static java.lang.Math.ceil;\n"
            //+ "import static java.lang.Math.floor;\n"
            //+ "import static java.lang.Math.abs;\n"
            //+ "import static java.lang.Math.sqrt;\n"
            //+ "\n"
            //+ "import static java.util.Arrays.asList;\n"
            //+ "import static java.util.Arrays.sort;\n"
            //+ "import static java.util.Collections.sort;\n"
            + "\n"
            + "import java.util.*;\n"
            + "import java.util.regex.*;\n"
            + "\n"
            ;

        return code;
    }

    protected String encodeParameters() {
        assert(paramTypes.length == paramNames.length);
        String code = "";
        for (int i=0; i<paramTypes.length; i++) {
            code += encodeDataType(paramTypes[i]) + " " + paramNames[i];
            if (i < paramTypes.length-1) {
                code += ", ";
            }
        }
        return code;
    }

    protected String encodeTestCases() {
        return "";
    }

    protected String encodeDataType(DataType dataType) {
        String code = dataType.getBaseName();
        for (int i=dataType.getDimension()-1; i>=0; --i) {
            code += "[]";
        }
        return code;
    }
}
