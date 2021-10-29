package main;

import ExcelAPI.ExcelAPI;
import JavaParser.MethodNamePrinter;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String ROOT = System.getProperty("user.dir") + "\\";
    private static final String PROJECT_FILE_PATH = "src\\ProjectFiles";

    public static void main(String[] args) throws Exception {
        DirectoryChecker directoryChecker = new DirectoryChecker();
        List<String> javaFileLocations = directoryChecker.getJavaFiles(ROOT, PROJECT_FILE_PATH);

        for(String javaFileLocation : javaFileLocations){
            System.out.println("javaFileLocation: " + javaFileLocation);

            CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + javaFileLocation));

            VoidVisitor<Void> methodNameVisitor = new MethodNamePrinter();
            methodNameVisitor.visit(cu, null);
        }


        ExcelAPI excelApi = new ExcelAPI();
        Workbook workbook = excelApi.testWorksheet();
        excelApi.saveWorksheet(workbook);
    }
}
