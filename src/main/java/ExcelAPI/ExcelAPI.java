package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.eclipsesource.json.*;

import java.io.*;
import java.util.*;

import static ExcelAPI.DependencyClassifier.*;
import static ExcelAPI.DependencyWriter.*;

public class ExcelAPI {
    public static Workbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public static Workbook writeDependencies(Workbook workbook, JsonObject json) throws IOException {
        String projectPath = System.getProperty("user.dir");
        Reader reader = new FileReader(projectPath + "\\classInfos.json");
        JsonValue value = Json.parse(reader);

        JsonObject classes = json.asObject().get("classes").asObject();
        JsonValue commits = value.asObject().get("commits");
        commitClassifier(commits);


        List<List<String>> importDependencies = new ArrayList<>();
        List<List<String>> extendDependencies = new ArrayList<>();
        List<List<String>> implementDependencies = new ArrayList<>();

        List<String> allClasses = new ArrayList<>();

        String[] classNames = classes.names().toArray(String[]::new);
        for(String className : classNames){
            JsonObject classDependencies = classes.asObject().get(className).asObject();
            //System.out.println("Class Name " + className + ": ");
            
            importDependencies.add(importClassifier(classDependencies));
            extendDependencies.add(extendClassifier(classDependencies));
            implementDependencies.add(implementsClassifier(classDependencies));

            putCyclicDependencies(className, importDependencies.get(importDependencies.size()-1));

            allClasses.add(className);
        }
        findCyclicDependencies();

        for(int i=0; i<cyclicDependencies.size(); i++){
            String chain = cyclicDependencies.get(i);
            String[] splittedChain = chain.split(" -> ");

            String newChain = "";
            for(int y=0; y<splittedChain.length-1; y++)
                newChain += splittedChain[y] + " -> ";
            newChain = newChain.substring(0, newChain.length()-4);
            cyclicDependencies.set(i, newChain);
        }
        //printCyclicDependencies();


        writeAllDependencies(workbook, allClasses, cyclicDependencies, commitStats,
                importDependencies,
                extendDependencies,
                implementDependencies,
                cyclicDependenciesList);

        return workbook;
    }

    public static List<String> getCyclicToClasses(String className, List<String> allCyclicDependencies){
        List<String> cyclicToClasses = new ArrayList<>();
        for(String cyclicChain : allCyclicDependencies){
            String[] splittedCyclicChain = cyclicChain.split(" -> ");
            if(splittedCyclicChain[0].equals(className))
                for(int i=1; i<splittedCyclicChain.length; i++)
                    cyclicToClasses.add(splittedCyclicChain[i]);
        }
        return cyclicToClasses;
    }

    public static CellStyle getTableHeaderStyle(CellStyle tableCellStyle){
        tableCellStyle.setAlignment(HorizontalAlignment.CENTER);
        tableCellStyle.setBorderTop(BorderStyle.MEDIUM);
        tableCellStyle.setBorderRight(BorderStyle.MEDIUM);
        tableCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        tableCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        
        return tableCellStyle;
    }
    public static CellStyle getManyDependentHeaderStyle(Cell cell, boolean isFirstRow){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();

        cellStyle.setAlignment((isFirstRow) ? HorizontalAlignment.CENTER : HorizontalAlignment.LEFT);

        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    public static CellStyle getCellStyleOfClassDependencies(Cell cell, int rowNumber, int columnNumber){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        if(rowNumber == columnNumber){
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return cellStyle;
    }

    public static CellStyle getFirstColumnStyles(Cell cell){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);

        return cellStyle;
    }

    public static CellStyle getCyclicCellStyle(Cell cell){
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THICK);
        cellStyle.setTopBorderColor(IndexedColors.RED.getIndex());
        cellStyle.setBorderRight(BorderStyle.THICK);
        cellStyle.setRightBorderColor(IndexedColors.RED.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THICK);
        cellStyle.setBottomBorderColor(IndexedColors.RED.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THICK);
        cellStyle.setLeftBorderColor(IndexedColors.RED.getIndex());

        return cellStyle;
    }

    public static XSSFFont getTableHeaderFont(XSSFFont tableHeaderFont){
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

        System.out.println("\nDependency Table is saved to -> " + fileLocation);
        workbook.close();
    }
}
