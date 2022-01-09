package main;


import com.eclipsesource.json.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.apache.poi.ss.usermodel.Workbook;

import ExcelAPI.ExcelAPI;
import JavaParser.*;
import java.io.File;
import java.util.*;

public class Main {
    private static final String ROOT = System.getProperty("user.dir") + "\\";
    private static final String PROJECT_FILE_PATH = "src\\ProjectFiles\\src";

    public static void main(String[] args) throws Exception {

        DirectoryChecker directoryChecker = new DirectoryChecker();
        HashMap<String, List<String>> javaFilesInDirectories = directoryChecker.getJavaFiles(ROOT, PROJECT_FILE_PATH);
        List<String> javaFileLocationsAsList = new ArrayList<>();

        for(List<String> javaFilesInDirectory : javaFilesInDirectories.values())
            javaFileLocationsAsList.addAll(javaFilesInDirectory);
        Set<String> javaFileLocations = new HashSet<>(javaFileLocationsAsList);


        //List<List<String>> allImports = new ArrayList<>();
        List<String> classNames = new ArrayList<>();

        //Map classes = new LinkedHashMap();
        JsonObject json = Json.object();
        JsonObject classes = Json.object();

        for(String javaFileLocation : javaFileLocations){
            CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + javaFileLocation));

            List<String> className_ = new ArrayList<>();
            List<String> extends_ = new ArrayList<>();
            List<String> implements_ = new ArrayList<>();
            List<String> methodNames = new ArrayList<>();
            List<String> methodCalls = new ArrayList<>();
            List<String> imports_ = new ArrayList<>();
            List<String> packageName = new ArrayList<>();

            VoidVisitor<List<String>> classNamesVisitor = new ClassNamesVisitor();
            classNamesVisitor.visit(cu, className_);
            String className = (className_.size() > 0) ? className_.get(0) : null;
            if(className == null || className.equals("Main"))
                continue;
            //System.out.println("javaFileLocation: " + javaFileLocation);
            String directory = javaFileLocation.substring(0,javaFileLocation.lastIndexOf('\\') + 1);
            //System.out.println("directory: " + directory);
            //System.out.println("\tClass Name: " + className);


            VoidVisitor<List<String>> packageDeclarationVisitor = new PackageDeclarationVisitor();
            packageDeclarationVisitor.visit(cu, packageName);

            String foundClassName = packageName.get(0) + "." + className;
            classNames.add(foundClassName);
            classes.add(foundClassName, Json.object());

            VoidVisitor<List<String>> importDeclarationVisitor = new ImportDeclarationVisitor();
            importDeclarationVisitor.visit(cu, imports_);

            //System.out.println("\t\timportsAsArray: " + imports_);
            JsonArray importsArray = Json.array(imports_.toArray(String[]::new));
            JsonObject classInfos = Json.object();


            VoidVisitor<List<String>> classExtendsVisitor = new ClassExtendsVisitor();
            classExtendsVisitor.visit(cu, extends_);

            String[] extends__ = extends_.toArray(String[]::new);
            if(extends__.length > 0){
                boolean isExtendedClassImported = false;
                for(String imports : imports_){
                    final int lastPackageIndex = imports.lastIndexOf('.') + 1;
                    String importClassName = imports.substring(lastPackageIndex);
                    if(extends__[0].equals(importClassName)){
                        extends__[0] = imports;
                        isExtendedClassImported = true;
                        break;
                    }
                }
                if(!isExtendedClassImported)
                    extends__[0] = directory.replaceAll("\\\\", ".").substring(1) + extends__[0];
            }
            JsonArray extendsArray = Json.array(extends__);


            VoidVisitor<List<String>> classImplementsVisitor = new ClassImplementsVisitor();
            classImplementsVisitor.visit(cu, implements_);
            //System.out.println("\t\timplementsAsArray: " + implements_);
            String[] implements__ = implements_.toArray(String[]::new);
            if(implements__.length > 0){
                int implementsIndex = 0;
                for(String implementsName : implements__){
                    boolean isImplementedClassImported = false;
                    for(String imports : imports_){
                        final int lastPackageIndex = imports.lastIndexOf('.') + 1;
                        String importClassName = imports.substring(lastPackageIndex);
                        if(implementsName.equals(importClassName)){
                            isImplementedClassImported = true;
                            implements__[implementsIndex] = imports;
                            break;
                        }
                    }
                    if(!isImplementedClassImported)
                        implements__[implementsIndex] = directory.replaceAll("\\\\", ".").substring(1) + implements__[implementsIndex];
                    implementsIndex++;
                }
            }
            JsonArray implementsArray = Json.array(implements__);


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


            //System.out.println("");
            //allImports.add(imports_);
        }

        json.add("classes", classes);
        String jsonStr = json.toString(WriterConfig.PRETTY_PRINT);
        //System.out.println(jsonStr);
        //System.exit(1);
        String[] className = classes.names().toArray(new String[0]);
        //System.out.println(className[0]);
        JsonObject aa = json.asObject().get("classes").asObject();
        //System.out.println(aa.get(className[0]));



        Workbook workbook = ExcelAPI.createWorkbook();
        workbook = ExcelAPI.writeDependencies(workbook, json);
        ExcelAPI.saveWorksheet(workbook);


    }
}
