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
    private static final String PROJECT_FILE_PATH = "src\\ProjectFiles\\project1";
    //private static final String PROJECT_FILE_PATH = "src\\ProjectFiles\\project2";

    public static void exit_(){
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {

        DirectoryChecker directoryChecker = new DirectoryChecker();
        HashMap<String, List<String>> javaFilesInDirectories = directoryChecker.getJavaFiles(ROOT, PROJECT_FILE_PATH);
        List<String> javaFileLocationsAsList = new ArrayList<>();

        for(List<String> javaFilesInDirectory : javaFilesInDirectories.values())
            javaFileLocationsAsList.addAll(javaFilesInDirectory);
        Set<String> javaFileLocations = new HashSet<>(javaFileLocationsAsList);

        //System.out.println(javaFileLocations);

        JsonObject json = Json.object();
        JsonObject classes = Json.object();

        List<String> classNames = new ArrayList<>();
        for(String javaFileLocation : javaFileLocations){
            CompilationUnit cu = StaticJavaParser.parse(new File(ROOT + PROJECT_FILE_PATH + javaFileLocation));

            List<String> classDataList = new ArrayList<>();
            List<String> extends_ = new ArrayList<>();
            List<String> packageName = new ArrayList<>();
            List<String> imports_ = new ArrayList<>();

            String directory = javaFileLocation.substring(0, javaFileLocation.lastIndexOf('\\') + 1);
            System.out.println("javaFileLocation: " + javaFileLocation + ", directory: " + directory);

            JsonArray importsArray = null;
            {
                VoidVisitor<List<String>> classNamesVisitor = new ClassNamesVisitor();
                classNamesVisitor.visit(cu, classDataList);

                VoidVisitor<List<String>> packageDeclarationVisitor = new PackageDeclarationVisitor();
                packageDeclarationVisitor.visit(cu, packageName);

                VoidVisitor<List<String>> importDeclarationVisitor = new ImportDeclarationVisitor();
                importDeclarationVisitor.visit(cu, imports_);
                importsArray = Json.array(imports_.toArray(String[]::new));
            }

            System.out.println(classDataList);
            System.out.println("");

            for(String classData: classDataList){
                String[] classDataStr = classData.split("\\|");//System.out.println(Arrays.toString(classDataStr));
                JsonObject classInfos = Json.object();

                List<String> implements_ = new ArrayList<>();


                String className = classDataStr[0];
                if(className.equals("Main"))
                    continue;
                className = (packageName.size() > 0) ? packageName.get(0) + "." + className : className;
                classNames.add(className);
                System.out.println("className: " + className);


                String allExtends = classDataStr[1];
                if(allExtends.equals("_"))
                    allExtends = "";
                allExtends = Extracter.extendsExtracter(allExtends, imports_, directory);
                JsonArray extendsArray = (!allExtends.equals("")) ? Json.array(allExtends) : Json.array();
                System.out.println("\tallExtends: " + allExtends);


                String[] allImplementsArray = classDataStr[2].split(",");
                for(String classImplements : allImplementsArray){
                    if(!classImplements.equals("_"))
                        implements_.add(classImplements);
                    System.out.println("\tclassImplements: " + classImplements);
                }
                String[] allImplements = (implements_.size() > 0) ? Extracter.implementsExtracter(implements_, imports_, directory) : new String[]{};
                JsonArray implementsArray = Json.array(allImplements);

                classInfos
                        .add("imports", importsArray)
                        .add("extends", extendsArray)
                        .add("implements", implementsArray);

                System.out.println("endofloop className: " + className);
                if(!className.equals(""))
                    classes.set(className, classInfos);
            }
        }
        json.add("classes", classes);
        String jsonStr = json.toString(WriterConfig.PRETTY_PRINT);
        System.out.println(jsonStr);
        //exit_();

        String[] className = classes.names().toArray(new String[0]);
        //System.out.println(className[0]);

        JsonObject aa = json.asObject().get("classes").asObject();
        //System.out.println(aa.get(className[0]));

        Workbook workbook = ExcelAPI.createWorkbook();
        workbook = ExcelAPI.writeDependencies(workbook, json);
        ExcelAPI.saveWorksheet(workbook);
    }
}
