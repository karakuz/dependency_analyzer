package ExcelAPI;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public class DependencyWriter {
    public static Workbook writeClassDependencies(Workbook workbook, Set<String> allImports, HashMap<String, List<String>> importDependencies){
        Sheet Imports = workbook.createSheet("ImportDependencies");

        CellStyle tableHeaderStyleOfClassDependencies = workbook.createCellStyle();
        ExcelAPI.setTableHeaderStyles(tableHeaderStyleOfClassDependencies);

        CellStyle cellStyleOfClassDependencies = workbook.createCellStyle();
        ExcelAPI.setCellStyles(cellStyleOfClassDependencies);

        XSSFFont classDependenciesHeaderFont = ((XSSFWorkbook) workbook).createFont();
        ExcelAPI.setTableHeaderFont(classDependenciesHeaderFont);
        tableHeaderStyleOfClassDependencies.setFont(classDependenciesHeaderFont);


        Row firstRowOfClassDependencies = Imports.createRow(0);

        ArrayList<String> allImports_ = new ArrayList<>(allImports);

        int columnNumber = 1;
        for(int i=0; i<allImports_.size(); i++){
            Cell headerCell = firstRowOfClassDependencies.createCell(columnNumber++);
            String dependency = allImports_.get(i);

            headerCell.setCellValue(dependency);
            headerCell.setCellStyle(tableHeaderStyleOfClassDependencies);
            Imports.autoSizeColumn(columnNumber);
        }

        Set<String> classNamesAsKeySet = importDependencies.keySet();
        ArrayList<String> classNames_ = new ArrayList<>(classNamesAsKeySet);

        Collection<List<String>> classDependenciesAsKeySet = importDependencies.values();
        ArrayList<List<String>> classDependencies_ = new ArrayList<>(classDependenciesAsKeySet);


        int rowNumber = 1;
        for(int i=0; i<classNames_.size(); i++){
            String className = classNames_.get(i);
            if(className.contains("Main")) continue;

            List<String> classDependencies = classDependencies_.get(i);

            Row row = Imports.createRow(rowNumber++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyleOfClassDependencies);
            Imports.autoSizeColumn(0);

            columnNumber = 1;
            for (String dependency : allImports_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber);

                if (classDependencies.contains(dependency)) {
                    Imports.autoSizeColumn(columnNumber);
                    isDependantOrNotCell.setCellValue("Dp");
                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
                    cellStyleOfClassDependencies.setFont(font);
                    isDependantOrNotCell.setCellStyle(cellStyleOfClassDependencies);
                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyleOfClassDependencies);
                }
                Imports.autoSizeColumn(columnNumber++);
            }
        }

        return workbook;
    }

    public static Workbook writeExtendDependencies(Workbook workbook, Set<String> allExtends, HashMap<String, List<String>> extends_){
        Sheet Extends = workbook.createSheet("Extend Dependencies");

        CellStyle tableHeaderStyleOfExtendDependencies = workbook.createCellStyle();
        ExcelAPI.setTableHeaderStyles(tableHeaderStyleOfExtendDependencies);

        CellStyle cellStyleOfExtendDependencies = workbook.createCellStyle();
        ExcelAPI.setCellStyles(cellStyleOfExtendDependencies);

        XSSFFont extendDependenciesHeaderFont = ((XSSFWorkbook) workbook).createFont();
        ExcelAPI.setTableHeaderFont(extendDependenciesHeaderFont);
        tableHeaderStyleOfExtendDependencies.setFont(extendDependenciesHeaderFont);


        Row firstRowOfExtendDependencies = Extends.createRow(0);

        ArrayList<String> allExtentions_ = new ArrayList<>(allExtends);

        int columnNumber2 = 1;
        for(int i=0; i<allExtentions_.size(); i++){
            Cell headerCell = firstRowOfExtendDependencies.createCell(columnNumber2++);
            String extention = allExtentions_.get(i);

            headerCell.setCellValue(extention);
            headerCell.setCellStyle(tableHeaderStyleOfExtendDependencies);
            Extends.autoSizeColumn(columnNumber2);
        }

        Set<String> classNamesAsKeySet2 = extends_.keySet();
        ArrayList<String> classNames_2 = new ArrayList<>(classNamesAsKeySet2);

        Collection<List<String>> classExtentionsAsKeySet = extends_.values();
        ArrayList<List<String>> classExtentions_ = new ArrayList<>(classExtentionsAsKeySet);


        int rowNumber2 = 1;
        for(int i = 0; i< classNames_2.size(); i++){
            String className = classNames_2.get(i);
            List<String> classExtentions = classExtentions_.get(i);

            Row row = Extends.createRow(rowNumber2++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyleOfExtendDependencies);
            Extends.autoSizeColumn(0);

            columnNumber2 = 1;
            for (String extend : allExtentions_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber2);

                if (classExtentions.contains(extend)) {
                    Extends.autoSizeColumn(columnNumber2);
                    isDependantOrNotCell.setCellValue("Ext");
                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    cellStyleOfExtendDependencies.setFont(font);
                    isDependantOrNotCell.setCellStyle(cellStyleOfExtendDependencies);

                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyleOfExtendDependencies);
                }
                Extends.autoSizeColumn(columnNumber2++);
            }
        }

        return workbook;
    }

    public static Workbook writeImplementsDependencies(Workbook workbook,Set<String> allImplementations,HashMap<String, List<String>> implementations) throws Exception{
        Sheet Implements = workbook.createSheet("Implement Dependencies");

        CellStyle tableHeaderStyleOfImplementDependencies = workbook.createCellStyle();
        ExcelAPI.setTableHeaderStyles(tableHeaderStyleOfImplementDependencies);

        CellStyle cellStyleOfImplementDependencies = workbook.createCellStyle();
        ExcelAPI.setCellStyles(cellStyleOfImplementDependencies);

        XSSFFont implementsDependenciesHeaderFont = ((XSSFWorkbook) workbook).createFont();
        ExcelAPI.setTableHeaderFont(implementsDependenciesHeaderFont);
        tableHeaderStyleOfImplementDependencies.setFont(implementsDependenciesHeaderFont);

        Row firstRowOfImplementsDependencies = Implements.createRow(0);

        ArrayList<String> allImplements_ = new ArrayList<>(allImplementations);

        int columnNumber3 = 1;
        for(int i = 0; i< allImplements_.size(); i++){
            Cell headerCell = firstRowOfImplementsDependencies.createCell(columnNumber3++);
            String implementation = allImplements_.get(i);

            headerCell.setCellValue(implementation);
            headerCell.setCellStyle(tableHeaderStyleOfImplementDependencies);
            Implements.autoSizeColumn(columnNumber3);
        }

        Set<String> classNamesAsKeySet3 = implementations.keySet();
        ArrayList<String> classNames_3 = new ArrayList<>(classNamesAsKeySet3);

        Collection<List<String>> classImplementationsAsKeySet = implementations.values();
        ArrayList<List<String>> classImplementations_ = new ArrayList<>(classImplementationsAsKeySet);


        int rowNumber3 = 1;
        for(int i = 0; i< classNames_3.size(); i++){
            String className = classNames_3.get(i);

            List<String> classImplementations = classImplementations_.get(i);

            Row row = Implements.createRow(rowNumber3++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyleOfImplementDependencies);
            Implements.autoSizeColumn(0);

            columnNumber3 = 1;
            for (String Implement : allImplements_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber3);

                if (classImplementations.contains(Implement)) {
                    Implements.autoSizeColumn(columnNumber3);
                    //cellStyleOfImplementDependencies.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    //cellStyleOfImplementDependencies.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    isDependantOrNotCell.setCellValue("Impl");
                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
                    cellStyleOfImplementDependencies.setFont(font);
                    isDependantOrNotCell.setCellStyle(cellStyleOfImplementDependencies);

                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyleOfImplementDependencies);
                }
                Implements.autoSizeColumn(columnNumber3++);
            }
        }
        return workbook;
    }

}
