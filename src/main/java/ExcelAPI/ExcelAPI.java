package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.eclipsesource.json.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class ExcelAPI {
    public void writeClassDependencies(Workbook workbook, JsonObject json) throws Exception {
        System.out.println("");
        JsonObject classes = json.asObject().get("classes").asObject();
        Set<String> allDependencies = new HashSet<>();
        Set<String> allExtentions = new HashSet<>();
        Set<String> allImplementations = new HashSet<>();
        HashMap<String, List<String>> dependencies = new HashMap<>();
        HashMap<String, List<String>> extentions = new HashMap<>();
        HashMap<String, List<String>> implementations = new HashMap<>();

        for(int i=0; i<classes.size(); i++){
            String[] classNames = classes.names().toArray(new String[0]);
            String className = classNames[i];

            JsonObject className_ = classes.asObject().get(className).asObject();
            JsonArray imports = className_.asObject().get("imports").asArray();
            JsonArray extend = className_.asObject().get("extends").asArray();
            JsonArray implement = className_.asObject().get("implements").asArray();
            System.out.println("Import names in " + className + ": ");
            List<String> classDependency = new ArrayList<>() ;
            List<String> classExtend = new ArrayList<>() ;
            List<String> classImplement = new ArrayList<>() ;
            for(JsonValue imported : imports){
                String imports_ = imported.asString();
                classDependency.add(imports_);
            }
            for(JsonValue extended : extend){
                if(extended.equals("")){classExtend.add("1");}else{
                String extends_ = extended.asString();
                classExtend.add(extends_);}
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
            }else{for(int x=0; x<classExtend.size(); x++){
                String extension = classExtend.get(x);

                System.out.println("extention: " + extension);
                allExtentions.add(extension);
                classExtentions.add(extension);

            }}
            extentions.put(className,classExtentions);

            for(int x=0; x<classDependency.size(); x++){
                String dependency = classDependency.get(x);

                if(!dependency.startsWith("java") && !dependency.startsWith("javax")){
                    System.out.println("dependency: " + dependency);
                    allDependencies.add(dependency);
                    classDependencies.add(dependency);
                }
            }
            dependencies.put(className,classDependencies);

            for(int x=0; x<classImplement.size(); x++){
                    String implementing = classImplement.get(x);

                    System.out.println("implement: " + implementing);
                    allImplementations.add(implementing);
                    classImplementations.add(implementing);

            }
            implementations.put(className,classImplementations);

        }
        allExtentions.remove("");
        System.out.println(dependencies);
        System.out.println(allDependencies);
        System.out.println(extentions);
        System.out.println(allExtentions);
        System.out.println(implementations);
        System.out.println(allImplementations);



        Sheet Imports = workbook.createSheet("ImportDependencies");

        CellStyle tableHeaderStyle = workbook.createCellStyle();
        tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        tableHeaderStyle.setBorderTop(BorderStyle.MEDIUM);
        tableHeaderStyle.setBorderRight(BorderStyle.MEDIUM);
        tableHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        tableHeaderStyle.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);

        tableHeaderStyle.setFont(font);


        Row firstRow = Imports.createRow(0);



        ArrayList<String> allDependencies_ = new ArrayList<>(allDependencies);

        int columnNumber = 1;
        for(int i=0; i<allDependencies_.size(); i++){
            Cell headerCell = firstRow.createCell(columnNumber++);
            String dependency = allDependencies_.get(i);

            headerCell.setCellValue(dependency);
            headerCell.setCellStyle(tableHeaderStyle);
            Imports.autoSizeColumn(columnNumber);
        }

        Set<String> classNamesAsKeySet = dependencies.keySet();
        ArrayList<String> classNames_ = new ArrayList<>(classNamesAsKeySet);

        Collection<List<String>> classDependenciesAsKeySet = dependencies.values();
        ArrayList<List<String>> classDependencies_ = new ArrayList<>(classDependenciesAsKeySet);


        int rowNumber = 1;
        for(int i=0; i<classNames_.size(); i++){
            String className = classNames_.get(i);
            List<String> classDependencies = classDependencies_.get(i);

            Row row = Imports.createRow(rowNumber++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyle);
            Imports.autoSizeColumn(0);

            columnNumber = 1;
            for (String dependency : allDependencies_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber);

                if (classDependencies.contains(dependency)) {
                    Imports.autoSizeColumn(columnNumber);
                    isDependantOrNotCell.setCellValue("X");
                    isDependantOrNotCell.setCellStyle(cellStyle);
                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyle);
                }
                Imports.autoSizeColumn(columnNumber++);
            }
        }


        Sheet Extends = workbook.createSheet("ExtendDependencies");

        CellStyle tableHeaderStyle2 = workbook.createCellStyle();
        tableHeaderStyle2.setAlignment(HorizontalAlignment.CENTER);
        tableHeaderStyle2.setBorderTop(BorderStyle.MEDIUM);
        tableHeaderStyle2.setBorderRight(BorderStyle.MEDIUM);
        tableHeaderStyle2.setBorderBottom(BorderStyle.MEDIUM);
        tableHeaderStyle2.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setBorderTop(BorderStyle.MEDIUM);
        cellStyle2.setBorderRight(BorderStyle.MEDIUM);
        cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle2.setBorderLeft(BorderStyle.MEDIUM);

        XSSFFont font2 = ((XSSFWorkbook) workbook).createFont();
        font2.setFontName("Arial");
        font2.setFontHeightInPoints((short) 10);
        font2.setBold(true);

        tableHeaderStyle2.setFont(font2);


        Row firstRow2 = Extends.createRow(0);

        ArrayList<String> allExtentions_ = new ArrayList<>(allExtentions);

        int columnNumber2 = 1;
        for(int i=0; i<allExtentions_.size(); i++){
            Cell headerCell = firstRow2.createCell(columnNumber2++);
            String extention = allExtentions_.get(i);

            headerCell.setCellValue(extention);
            headerCell.setCellStyle(tableHeaderStyle2);
            Extends.autoSizeColumn(columnNumber2);
        }

        Set<String> classNamesAsKeySet2 = extentions.keySet();
        ArrayList<String> classNames_2 = new ArrayList<>(classNamesAsKeySet2);

        Collection<List<String>> classExtentionsAsKeySet = extentions.values();
        ArrayList<List<String>> classExtentions_ = new ArrayList<>(classExtentionsAsKeySet);


        int rowNumber2 = 1;
        for(int i = 0; i< classNames_2.size(); i++){
            String className = classNames_2.get(i);
            List<String> classExtentions = classExtentions_.get(i);

            Row row = Extends.createRow(rowNumber2++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyle2);
            Extends.autoSizeColumn(0);

            columnNumber2 = 1;
            for (String extend : allExtentions_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber2);

                if (classExtentions.contains(extend)) {
                    Extends.autoSizeColumn(columnNumber2);
                    isDependantOrNotCell.setCellValue("X");
                    isDependantOrNotCell.setCellStyle(cellStyle2);
                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyle2);
                }
                Extends.autoSizeColumn(columnNumber2++);
            }
        }

        Sheet Implements = workbook.createSheet("ImplementDependencies");

        CellStyle tableHeaderStyle3 = workbook.createCellStyle();
        tableHeaderStyle3.setAlignment(HorizontalAlignment.CENTER);
        tableHeaderStyle3.setBorderTop(BorderStyle.MEDIUM);
        tableHeaderStyle3.setBorderRight(BorderStyle.MEDIUM);
        tableHeaderStyle3.setBorderBottom(BorderStyle.MEDIUM);
        tableHeaderStyle3.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);
        cellStyle3.setBorderTop(BorderStyle.MEDIUM);
        cellStyle3.setBorderRight(BorderStyle.MEDIUM);
        cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle3.setBorderLeft(BorderStyle.MEDIUM);

        XSSFFont font3 = ((XSSFWorkbook) workbook).createFont();
        font3.setFontName("Arial");
        font3.setFontHeightInPoints((short) 10);
        font3.setBold(true);

        tableHeaderStyle3.setFont(font3);

        Row firstRow3 = Implements.createRow(0);

        ArrayList<String> allImplements_ = new ArrayList<>(allImplementations);

        int columnNumber3 = 1;
        for(int i = 0; i< allImplements_.size(); i++){
            Cell headerCell = firstRow3.createCell(columnNumber3++);
            String implementation = allImplements_.get(i);

            headerCell.setCellValue(implementation);
            headerCell.setCellStyle(tableHeaderStyle3);
            Implements.autoSizeColumn(columnNumber3);
        }

        Set<String> classNamesAsKeySet3 = implementations.keySet();
        ArrayList<String> classNames_3 = new ArrayList<>(classNamesAsKeySet3);

        Collection<List<String>> classImplementationsAsKeySet = implementations.values();
        ArrayList<List<String>> classImplementations_ = new ArrayList<>(classImplementationsAsKeySet);


        int rowNumber3 = 1;
        for(int i = 0; i< classNames_3.size(); i++){
            String className = classNames_3.get(i);
            if(className.contains("Main")) continue;

            List<String> classImplementations = classImplementations_.get(i);

            Row row = Implements.createRow(rowNumber3++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyle3);
            Implements.autoSizeColumn(0);

            columnNumber3 = 1;
            for (String Implement : allImplements_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber3);

                if (classImplementations.contains(Implement)) {
                    Implements.autoSizeColumn(columnNumber3);
                    isDependantOrNotCell.setCellValue("X");
                    isDependantOrNotCell.setCellStyle(cellStyle3);
                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyle3);
                }
                Implements.autoSizeColumn(columnNumber3++);
            }
        }

        saveWorksheet(workbook);
    }

    public Workbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public Workbook testWorksheet(){
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Row header = sheet.createRow(0);//ROW 1

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        Row row2 = sheet.createRow(1);//ROW 2
        Cell cell_A2 = row2.createCell(0);
        cell_A2.setCellValue("Emre Karakuz");
        cell_A2.setCellStyle(cellStyle);

        cell_A2 = row2.createCell(1);
        cell_A2.setCellValue(23);
        cell_A2.setCellStyle(cellStyle);

        Row row3 = sheet.createRow(2);//ROW 3
        Cell cell_A3 = row3.createCell(0);
        cell_A3.setCellValue("Emir Gülçür");
        cell_A3.setCellStyle(cellStyle);

        cell_A3 = row3.createCell(1);
        cell_A3.setCellValue(22);
        cell_A3.setCellStyle(cellStyle);

        return workbook;
    }
    public void saveWorksheet(Workbook workbook) throws Exception {
        File currDir = new File("output");
        String path = currDir.getAbsolutePath();
        String fileLocation = path + "\\output.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);

        System.out.println("\nTest Worksheet is saved to -> " + fileLocation);
        workbook.close();
    }
}
