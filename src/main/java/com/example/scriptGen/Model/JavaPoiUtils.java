//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.scriptGen.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class JavaPoiUtils {
    public JavaPoiUtils() {
    }

    public void readExcelFile(File excelFile) {
        InputStream excelStream = null;

        try {
            excelStream = new FileInputStream(excelFile);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            int rows = xssfSheet.getLastRowNum();
            boolean cols = false;

            for(int r = 0; r <= rows; ++r) {
                XSSFRow xssfRow = xssfSheet.getRow(r);
                if (xssfRow == null) {
                    break;
                }

                System.out.print("Row: " + r + " -> ");

                for(int c = 0; c < xssfRow.getLastCellNum(); ++c) {
                    String cellValue = xssfRow.getCell(c) == null ? "" : (xssfRow.getCell(c).getCellType() == CellType.STRING ? xssfRow.getCell(c).getStringCellValue() : (xssfRow.getCell(c).getCellType() == CellType.NUMERIC ? "" + xssfRow.getCell(c).getNumericCellValue() : (xssfRow.getCell(c).getCellType() == CellType.BOOLEAN ? "" + xssfRow.getCell(c).getBooleanCellValue() : (xssfRow.getCell(c).getCellType() == CellType.BLANK ? "BLANK" : (xssfRow.getCell(c).getCellType() == CellType.FORMULA ? "FORMULA" : (xssfRow.getCell(c).getCellType() == CellType.ERROR ? "ERROR" : ""))))));
                    System.out.print("[Column " + c + ": " + cellValue + "] ");
                }

                System.out.println();
            }
        } catch (FileNotFoundException var22) {
            System.out.println("The file not exists (No se encontró el fichero): " + var22);
        } catch (IOException var23) {
            System.out.println("Error in file procesing (Error al procesar el fichero): " + var23);
        } finally {
            try {
                excelStream.close();
            } catch (IOException var21) {
                System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + var21);
            }

        }

    }
}


