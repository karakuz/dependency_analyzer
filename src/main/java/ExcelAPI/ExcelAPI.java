package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.eclipsesource.json.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class ExcelAPI {
    public static Workbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public static Workbook writeDependencies(Workbook workbook, JsonObject json) throws Exception {
        System.out.println("");
        JsonObject classes = json.asObject().get("classes").asObject();
        Set<String> allImports = new HashSet<>();
        Set<String> allExtentions = new HashSet<>();
        Set<String> allImplementations = new HashSet<>();
        HashMap<String, List<String>> importDependencies = new HashMap<>();
        HashMap<String, List<String>> extends_ = new HashMap<>();
        HashMap<String, List<String>> implementations = new HashMap<>();

        for(int i=0; i<classes.size(); i++){


            String[] classNames = classes.names().toArray(new String[0]);
            String className = classNames[i];

            JsonObject className_ = classes.asObject().get(className).asObject();
            JsonArray imports = className_.asObject().get("imports").asArray();
            JsonArray extend = className_.asObject().get("extends").asArray();
            JsonArray implement = className_.asObject().get("implements").asArray();
            System.out.println("Import names in " + className + ": ");
            
            List<String> classDependency = new ArrayList<>();
            List<String> classExtend = new ArrayList<>();
            List<String> classImplement = new ArrayList<>();
            for(JsonValue imported : imports){
                String imports_ = imported.asString();
                classDependency.add(imports_);
            }
            for(JsonValue extended : extend){
                if(extended.equals(""))
                    classExtend.add("1");
                else{
                    String extendsStr = extended.asString();
                    classExtend.add(extendsStr);
                }
            }
            for(JsonValue implemented : implement){
                String implements_ = implemented.asString();
                classImplement.add(implements_);
            }

            List<String> classDependencies = new ArrayList<>();
            List<String> classExtentions = new ArrayList<>();
            List<String> classImplementations = new ArrayList<>();

            if(classExtend.size()==0){
                String extension = "";
                System.out.println("extention: " + extension);
                allExtentions.add(extension);
                classExtentions.add(extension);
            }
            else{
                for(int x=0; x<classExtend.size(); x++){
                String extension = classExtend.get(x);

                System.out.println("extention: " + extension);
                allExtentions.add(extension);
                classExtentions.add(extension);
                }
            }
            extends_.put(className,classExtentions);

            for(int x=0; x<classDependency.size(); x++){
                String dependency = classDependency.get(x);

                if(!dependency.startsWith("java") && !dependency.startsWith("javax")){
                    System.out.println("dependency: " + dependency);
                    allImports.add(dependency);
                    classDependencies.add(dependency);
                }
            }
            importDependencies.put(className,classDependencies);

            for(int x=0; x<classImplement.size(); x++){
                String implementing = classImplement.get(x);

                System.out.println("implement: " + implementing);
                allImplementations.add(implementing);
                classImplementations.add(implementing);
            }
            implementations.put(className,classImplementations);

        }
        allExtentions.remove("");
        System.out.println(importDependencies);
        System.out.println(allImports);
        System.out.println(extends_);
        System.out.println(allExtentions);
        System.out.println(implementations);
        System.out.println(allImplementations);


        DependencyWriter.writeClassDependencies(workbook, allImports, importDependencies);
        DependencyWriter.writeExtendDependencies(workbook, allExtentions,  extends_);
        DependencyWriter.writeImplementsDependencies(workbook, allImplementations, implementations);

        return workbook;
    }

    public static CellStyle setTableHeaderStyles(CellStyle tableCellStyle){
        tableCellStyle.setAlignment(HorizontalAlignment.CENTER);
        tableCellStyle.setBorderTop(BorderStyle.MEDIUM);
        tableCellStyle.setBorderRight(BorderStyle.MEDIUM);
        tableCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        tableCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        
        return tableCellStyle;
    }
    
    public static CellStyle setCellStyles(CellStyle cellStyle){
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        
        return cellStyle;
    }

    public static XSSFFont setTableHeaderFont(XSSFFont tableHeaderFont){
        tableHeaderFont.setFontName("Arial");
        tableHeaderFont.setFontHeightInPoints((short) 10);
        tableHeaderFont.setBold(true);

        return tableHeaderFont;
    }
    
    public static void saveWorksheet(Workbook workbook) throws Exception {
        File currDir = new File("output");
        String path = currDir.getAbsolutePath();
        String fileLocation = path + "\\output.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);

        System.out.println("\nTest Worksheet is saved to -> " + fileLocation);
        workbook.close();
    }
}
