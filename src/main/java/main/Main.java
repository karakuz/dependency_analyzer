package main;

import ExcelAPI.ExcelAPI;
import JavaParser.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String ROOT = System.getProperty("user.dir") + "\\";
    private static final String PROJECT_FILE_PATH = "src\\ProjectFiles\\src";

    public static void main(String[] args) throws Exception {


        DirectoryChecker directoryChecker = new DirectoryChecker();
        List<String> javaFileLocations = directoryChecker.getJavaFiles(ROOT, PROJECT_FILE_PATH);


        List<List<String>> allImports = new ArrayList<>();

        for(String javaFileLocation : javaFileLocations){
            System.out.println("javaFileLocation: " + javaFileLocation);

            CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + javaFileLocation));

            List<String> className_ = new ArrayList<>();
            List<String> methodNames = new ArrayList<>();
            List<String> methodCalls = new ArrayList<>();
            List<String> imports = new ArrayList<>();


            VoidVisitor<List<String>> classVisitor = new ClassVisitor();
            classVisitor.visit(cu, className_);
            String className = (className_.size() > 0) ? className_.get(0) : null;
            if(className == null)
                continue;
            System.out.println("Class Name: " + className);

            VoidVisitor<Void> packageDeclarationVisitor = new PackageDeclarationVisitor();
            packageDeclarationVisitor.visit(cu, null);

            VoidVisitor<List<String>> importDeclarationVisitor = new ImportDeclarationVisitor();
            importDeclarationVisitor.visit(cu, imports);

            VoidVisitor<Void> variableDeclarationExprVisitor = new VariableDeclerationExprVisitor();
            variableDeclarationExprVisitor.visit(cu, null);

            VoidVisitor<Void> objectCreationExprVisitor = new ObjectCreationExprVisitor();
            objectCreationExprVisitor.visit(cu, null);

            VoidVisitor<Void> fieldVisitor = new FieldVisitor();
            fieldVisitor.visit(cu, null);

            VoidVisitor<List<String>> methodVisitor = new MethodVisitor();
            methodVisitor.visit(cu, methodNames);
            //methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));

            /*TypeSolver typeSolver = new CombinedTypeSolver(new JavaParserTypeSolver(new File(PROJECT_FILE_PATH + "\\com\\test\\store")));
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);*/

            VoidVisitor<Void> methodCallVisitor = new MethodCallVisitor();
            methodCallVisitor.visit(cu, null);


            System.out.println("");
            allImports.add(imports);
        }

        CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + "\\com\\test\\Main.java"));
        VoidVisitor<Void> methodCallVisitor = new MethodCallVisitor();
        methodCallVisitor.visit(cu, null);


        ExcelAPI excelApi = new ExcelAPI();
        /*Workbook workbook = excelApi.testWorksheet();
        excelApi.saveWorksheet(workbook);*/

        excelApi.test(allImports, javaFileLocations);
    }
}
