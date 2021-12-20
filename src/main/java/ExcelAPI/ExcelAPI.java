package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.eclipsesource.json.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import static ExcelAPI.DependencyClassifier.*;
import static ExcelAPI.DependencyWriter.*;

public class ExcelAPI {
    public static Workbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public static Workbook writeDependencies(Workbook workbook, JsonObject json){
        JsonObject classes = json.asObject().get("classes").asObject();
        List<List<String>> importDependencies = new ArrayList<>();
        List<List<String>> extendDependencies = new ArrayList<>();
        List<List<String>> implementDependencies = new ArrayList<>();

        List<String> allClasses = new ArrayList<>();

        String[] classNames = classes.names().toArray(String[]::new);
        for(String className : classNames){
            JsonObject className_ = classes.asObject().get(className).asObject();
            System.out.println("Class Name " + className + ": ");
            
            importDependencies.add(importClassifier(className_));
            extendDependencies.add(extendClassifier(className_));
            implementDependencies.add(implementsClassifier(className_));

            allClasses.add(className);

        }

        writeAllDependencies(workbook, allClasses,
                importDependencies,
                extendDependencies,
                implementDependencies);

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
    public static CellStyle setColoredHeaderStyles(Cell cell){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    public static CellStyle setCellStyles(Cell cell, int rowNumber, int columnNumber){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        if(rowNumber == columnNumber){
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
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
