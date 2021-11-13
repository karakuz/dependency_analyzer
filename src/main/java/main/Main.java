package main;

import JavaParser.*;
import com.eclipsesource.json.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String ROOT = System.getProperty("user.dir") + "\\";
    private static final String PROJECT_FILE_PATH = "src\\ProjectFiles\\src";

    public static void main(String[] args) throws Exception {

        DirectoryChecker directoryChecker = new DirectoryChecker();
        List<String> javaFileLocations = directoryChecker.getJavaFiles(ROOT, PROJECT_FILE_PATH);


        List<List<String>> allImports = new ArrayList<>();
        List<String> classNames = new ArrayList<>();

        //Map classes = new LinkedHashMap();
        JsonObject json = Json.object();
        JsonObject classes = Json.object();

        for(String javaFileLocation : javaFileLocations){
            System.out.println("javaFileLocation: " + javaFileLocation);

            CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + javaFileLocation));

            List<String> className_ = new ArrayList<>();
            List<String> extends_ = new ArrayList<>();
            List<String> implements_ = new ArrayList<>();
            List<String> methodNames = new ArrayList<>();
            List<String> methodCalls = new ArrayList<>();
            List<String> imports = new ArrayList<>();
            List<String> packageName = new ArrayList<>();

            JsonObject classInfo = Json.object();

            VoidVisitor<List<String>> classNamesVisitor = new ClassNamesVisitor();
            classNamesVisitor.visit(cu, className_);
            String className = (className_.size() > 0) ? className_.get(0) : null;
            if(className == null)
                continue;
            System.out.println("Class Name: " + className);



            VoidVisitor<List<String>> packageDeclarationVisitor = new PackageDeclarationVisitor();
            packageDeclarationVisitor.visit(cu, packageName);

            String foundClassName = packageName.get(0) + "." + className;
            classNames.add(foundClassName);
            classes.add(foundClassName, Json.object());

            VoidVisitor<List<String>> importDeclarationVisitor = new ImportDeclarationVisitor();
            importDeclarationVisitor.visit(cu, imports);

            String[] importsAsArray = new String[imports.size()];
            importsAsArray = imports.toArray(importsAsArray);
            JsonArray importsArray = Json.array(importsAsArray);
            JsonObject classInfos = Json.object();


            VoidVisitor<List<String>> classExtendsVisitor = new ClassExtendsVisitor();
            classExtendsVisitor.visit(cu, extends_);

            String[] extendsAsArray = (extends_.size() > 0) ? new String[]{extends_.get(0)} : new String[]{};
            JsonArray extendsArray = Json.array(extendsAsArray);


            VoidVisitor<List<String>> classImplementsVisitor = new ClassImplementsVisitor();
            classImplementsVisitor.visit(cu, implements_);
            String[] implementsAsArray = (implements_.size() > 0) ? new String[]{implements_.get(0)} : new String[]{};
            JsonArray implementsArray = Json.array(implementsAsArray);


            classInfos
                    .add("imports", importsArray)
                    .add("extends", extendsArray)
                    .add("implements", implementsArray);

            classes.set(foundClassName, classInfos);

            /*VoidVisitor<Void> variableDeclarationExprVisitor = new VariableDeclerationExprVisitor();
            variableDeclarationExprVisitor.visit(cu, null);

            VoidVisitor<Void> objectCreationExprVisitor = new ObjectCreationExprVisitor();
            objectCreationExprVisitor.visit(cu, null);

            VoidVisitor<Void> fieldVisitor = new FieldVisitor();
            fieldVisitor.visit(cu, null);

            VoidVisitor<List<String>> methodVisitor = new MethodVisitor();
            methodVisitor.visit(cu, methodNames);*/
            //methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));

            /*TypeSolver typeSolver = new CombinedTypeSolver(new JavaParserTypeSolver(new File(PROJECT_FILE_PATH + "\\com\\test\\store")));
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);*/

            /*VoidVisitor<Void> methodCallVisitor = new MethodCallVisitor();
            methodCallVisitor.visit(cu, null);*/


            System.out.println("");
            allImports.add(imports);
        }


        /*CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + "\\com\\test\\Main.java"));
        VoidVisitor<Void> methodCallVisitor = new MethodCallVisitor();
        methodCallVisitor.visit(cu, null);*/

        json.add("classes", classes);
        String jsonStr = json.toString(WriterConfig.PRETTY_PRINT);
        System.out.println(jsonStr);

        /*ExcelAPI excelApi = new ExcelAPI();
        Workbook workbook = excelApi.createWorkbook();
        excelApi.writeClassDependencies(workbook, allImports, classNames);*/


    }
}
