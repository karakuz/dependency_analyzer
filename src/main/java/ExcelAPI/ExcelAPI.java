package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class ExcelAPI {
    public void writeClassDependencies(Workbook workbook, List<List<String>> allImports, List<String> classNames) throws Exception {
        System.out.println("");

        Set<String> allDependencies = new HashSet<>();
        HashMap<String, List<String>> dependencies = new HashMap<>();

        for(int i=0; i<allImports.size(); i++){
            String className = classNames.get(i);
            System.out.println("Import names in " + className + ": ");
            List<String> classDependency = allImports.get(i);

            List<String> classDependencies = new ArrayList<>();
            for(int x=0; x<classDependency.size(); x++){
                String dependency = classDependency.get(x);

                if(!dependency.startsWith("java") && !dependency.startsWith("javax")){
                    System.out.println("dependency: " + dependency);
                    allDependencies.add(dependency);
                    classDependencies.add(dependency);
                }
            }
            dependencies.put(className,classDependencies);
        }
        System.out.println(dependencies);
        System.out.println(allDependencies);


        Sheet sheet = workbook.createSheet("ClassDependencies");

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


        Row firstRow = sheet.createRow(0);

        ArrayList<String> allDependencies_ = new ArrayList<>(allDependencies);

        int columnNumber = 1;
        for(int i=0; i<allDependencies_.size(); i++){
            Cell headerCell = firstRow.createCell(columnNumber++);
            String dependency = allDependencies_.get(i);

            headerCell.setCellValue(dependency);
            headerCell.setCellStyle(tableHeaderStyle);
            sheet.autoSizeColumn(columnNumber);
        }

        Set<String> classNamesAsKeySet = dependencies.keySet();
        ArrayList<String> classNames_ = new ArrayList<>(classNamesAsKeySet);

        Collection<List<String>> classDependenciesAsKeySet = dependencies.values();
        ArrayList<List<String>> classDependencies_ = new ArrayList<>(classDependenciesAsKeySet);


        int rowNumber = 1;
        for(int i=0; i<classNames_.size(); i++){
            String className = classNames_.get(i);
            List<String> classDependencies = classDependencies_.get(i);

            Row row = sheet.createRow(rowNumber++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(className);
            classNameCell.setCellStyle(tableHeaderStyle);
            sheet.autoSizeColumn(0);

            columnNumber = 1;
            for (String dependency : allDependencies_) {
                Cell isDependantOrNotCell = row.createCell(columnNumber);

                if (classDependencies.contains(dependency)) {
                    sheet.autoSizeColumn(columnNumber);
                    isDependantOrNotCell.setCellValue("X");
                    isDependantOrNotCell.setCellStyle(cellStyle);
                } else {
                    isDependantOrNotCell.setCellValue("");
                    isDependantOrNotCell.setCellStyle(cellStyle);
                }
                sheet.autoSizeColumn(columnNumber++);
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
