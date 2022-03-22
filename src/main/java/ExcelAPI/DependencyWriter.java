package ExcelAPI;

import org.apache.poi.hpsf.Array;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public class DependencyWriter {
    private final static String emptyCell = "              ";
    public static HashMap<Integer, String> getDependencyNames(){
        HashMap<Integer, String> dependencyNames = new HashMap<>();
        dependencyNames.put(0, "Dp");
        dependencyNames.put(1, "Ext");
        dependencyNames.put(2, "Impl");

        return dependencyNames;
    };

    public static Workbook writeAllDependencies(Workbook workbook, List<String> allClasses, List<String> cyclicDependencies, HashMap<String, HashMap<String,Integer>> commitStats, List<List<String>>... dependenciesList){
        HashMap<Integer, String> dependencyNames = getDependencyNames();

        Sheet Imports = workbook.createSheet("Dependencies");

        CellStyle tableHeaderStyleOfClassDependencies = workbook.createCellStyle();
        ExcelAPI.getTableHeaderStyle(tableHeaderStyleOfClassDependencies);

        XSSFFont classDependenciesHeaderFont = ((XSSFWorkbook) workbook).createFont();
        ExcelAPI.getTableHeaderFont(classDependenciesHeaderFont);
        tableHeaderStyleOfClassDependencies.setFont(classDependenciesHeaderFont);

        Row firstRowOfClassDependencies = Imports.createRow(0);

        ArrayList<String> allClassesNames = new ArrayList<>(allClasses);

        //A0,A1,A1....AX
        int columnNumber = 1;
        for(int i=0; i<allClassesNames.size(); i++){
            Cell headerCell = firstRowOfClassDependencies.createCell(columnNumber++);

            headerCell.setCellValue(i+1);
            headerCell.setCellStyle(tableHeaderStyleOfClassDependencies);
        }

        ArrayList<String> allClassNames = new ArrayList<>(allClasses);

        List<List<String>> allImportDependencies = dependenciesList[0];
        List<List<String>> allExtendDependencies = dependenciesList[1];
        List<List<String>> allImplementDependencies = dependenciesList[2];
        List<String> allCyclicDependencies = cyclicDependencies;

        /*Set<String> allCyclicDependencies = new HashSet<>();
        for(List<String> cyclicDependencyChain : dependenciesList[3]){
            for(String className : cyclicDependencyChain){
                allCyclicDependencies.add(className);
            }
        }*/

        /*System.out.println("allCyclicDependencies: ");
        System.out.println(allCyclicDependencies);*/
        /*System.out.println("allImportDependencies: ");
        System.out.println(allImportDependencies);
        System.out.println("allExtendDependencies: ");
        System.out.println(allExtendDependencies);*/

        int rowNumber = 1;
        for(int i=0; i<allClassNames.size(); i++){
            String classNameOnRow = allClassNames.get(i);
            List<String> importDependencies = allImportDependencies.get(i);
            List<String> extendDependencies = allExtendDependencies.get(i);
            List<String> implementDependencies = allImplementDependencies.get(i);

            List<String> cyclicTo = ExcelAPI.getCyclicToClasses(classNameOnRow, allCyclicDependencies);

            Row row = Imports.createRow(rowNumber++);
            Cell classNameCell = row.createCell(0);
            classNameCell.setCellValue(i+1 + " " + classNameOnRow);

            CellStyle firstColumnStyle = ExcelAPI.getFirstColumnStyles(classNameCell);
            classNameCell.setCellStyle(firstColumnStyle);
            firstColumnStyle.setFont(classDependenciesHeaderFont);
            Imports.autoSizeColumn(0);

            columnNumber = 1;
            //commitStats

            for (String classNameOnColumn : allClassNames) {
                Cell isDependantOrNotCell = row.createCell(columnNumber);
                CellStyle cellStyleOfClassDependencies = ExcelAPI.getCellStyleOfClassDependencies(isDependantOrNotCell, rowNumber-1, columnNumber);

                final int importDependenciesIndex = importDependencies.indexOf(classNameOnColumn);
                final int extendDependenciesIndex = extendDependencies.indexOf(classNameOnColumn);
                final int implementDependenciesIndex = implementDependencies.indexOf(classNameOnColumn);
                List<Integer> dependencyIndexes = Arrays.asList(importDependenciesIndex,extendDependenciesIndex,implementDependenciesIndex);

                String cellValue = "";
                for(int x=0; x<dependencyIndexes.size(); x++){
                    int index = dependencyIndexes.get(x);
                    if(index != -1)
                        cellValue += dependencyNames.get(x) + ",";
                }

                if(!cellValue.equals(""))
                    cellValue = cellValue.substring(0, cellValue.length()-1);

                if(!classNameOnColumn.equals(classNameOnRow) &&
                        commitStats.get(classNameOnColumn) != null &&
                        commitStats.get(classNameOnColumn).get(classNameOnRow) != null){
                    cellValue = cellValue + "," + commitStats.get(classNameOnColumn).get(classNameOnRow);
                }

                if(cellValue.equals(""))
                    cellValue = emptyCell;
                Imports.autoSizeColumn(columnNumber);
                isDependantOrNotCell.setCellValue(cellValue);
                if(cyclicTo.contains(classNameOnColumn))
                    isDependantOrNotCell.setCellStyle(ExcelAPI.getCyclicCellStyle(isDependantOrNotCell));
                else
                    isDependantOrNotCell.setCellStyle(cellStyleOfClassDependencies);

                Imports.autoSizeColumn(columnNumber++);
            }
        }
        int[] data = new int[allClassNames.size()];
        for(int a=1;a <= allClassNames.size();a++){
            int count =0;
            int rowIndex = 0;
            firstRowOfClassDependencies = Imports.getRow(rowIndex);
            Cell head = firstRowOfClassDependencies.getCell(a);

            for ( rowIndex = 1; rowIndex <= allClassNames.size(); rowIndex++) {
                firstRowOfClassDependencies = Imports.getRow(rowIndex);
                if (firstRowOfClassDependencies != null) {
                    Cell cell = firstRowOfClassDependencies.getCell(a);
                    if (cell.getStringCellValue() != emptyCell && !cell.getStringCellValue().substring(0,1).equals(","))
                        count++;
                }
            }
            data[a-1] = count;
        }
        //Creating Quartiles here
        int pos;
        int temp;
        for (int i = 0; i < data.length; i++)
        {
            pos = i;
            for (int j = i+1; j < data.length; j++)
            {
                if (data[j] < data[pos])                  //find the index of the minimum element
                {
                    pos = j;
                }
            }

            temp = data[pos];            //swap the current element with the minimum element
            data[pos] = data[i];
            data[i] = temp;
        }
        data = new int[]{0, 0, 0, 1, 1, 1, 1, 5, 5, 6};
        double q1;
        double q3;
        double q2;
        double upper_bound;
        if(data.length%2 == 0){
            q1 = (data[(data.length) * 25 / 100] + data[((data.length) * 25 / 100) - 1]) / 2.0;
            q3 = (data[(data.length)*75/100] + data[((data.length)*75/100)-1])/2.0;
            System.out.println(q1);
            q2 = q3-q1;
            upper_bound = q3+(1.5*q2);
        }else{
            q1 = data[(data.length)*25/100];
            q3 = data[(data.length)*75/100];
            q2 = q3-q1;
            upper_bound = q3+(1.5*q2);
        }

        System.out.println(q3);
        System.out.println(upper_bound);
        //End Quartiles
        //Color With Outliers
        for(int a=1;a <= allClassNames.size();a++){
            int count =0;
            int rowIndex = 0;
            firstRowOfClassDependencies = Imports.getRow(rowIndex);
            Cell head = firstRowOfClassDependencies.getCell(a);

            for ( rowIndex = 1; rowIndex <= allClassNames.size(); rowIndex++) {
                firstRowOfClassDependencies = Imports.getRow(rowIndex);
                if (firstRowOfClassDependencies != null) {
                    Cell cell = firstRowOfClassDependencies.getCell(a);
                    if (cell.getStringCellValue() != emptyCell && !cell.getStringCellValue().substring(0,1).equals(","))
                        count++;
                    if(count >= upper_bound){
                        CellStyle firstRowManyDependentStyle = ExcelAPI.getManyDependentHeaderStyle(head, true);
                        CellStyle firstColumnManyDependentStyle = ExcelAPI.getManyDependentHeaderStyle(head, false);
                        workbook.getSheetAt(0).getRow(a).getCell(0).setCellStyle(firstColumnManyDependentStyle);
                        head.setCellStyle(firstRowManyDependentStyle);
                    }
                }
            }
            data[a-1] = count;
        }
        //Color End

        int rightMostColumn = allClassNames.size()+2;
        final int middleRow = workbook.getSheetAt(0).getLastRowNum()/2;

        String[] legend = {"Cyclic Dependency","Unhealthy Inheritance"};


        for(String legendName : legend){
            //System.out.println("legendName: " + legendName);
            Cell legendCell = workbook.getSheetAt(0).getRow(middleRow).createCell(rightMostColumn);
            legendCell.setCellValue(legendName);

            if(legendName.equals("Cyclic Dependency"))
                legendCell.setCellStyle(ExcelAPI.getCyclicCellStyle(legendCell));
            else if(legendName.equals("Unhealthy Inheritance"))
                legendCell.setCellStyle(ExcelAPI.getManyDependentHeaderStyle(legendCell, true));
            Imports.autoSizeColumn(rightMostColumn++);
        }

        return workbook;
    }

}
