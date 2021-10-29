package ExcelAPI;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelAPI {

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
