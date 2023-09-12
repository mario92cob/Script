package com.example.scriptGen.Controller;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

public class TasasVN {
    private static final Integer POS_PLAZO_DD = 0;
    private static final Integer POS_PLAZO_HH = 1;
    private static final Integer POS_TNA = 2;
    private static final Integer POS_TEM = 3;
    private static final Integer POS_PPP = 4;
    private static final Integer POS_CODIGO_PRO = 5;
    private static final JFileChooser fc = new JFileChooser();

    public TasasVN() {
    }

    public ArrayList<Tasa> getTasas(File excelFile) {
        InputStream excelStream = null;
        ArrayList<Tasa> tasas = new ArrayList();

        try {
            excelStream = new FileInputStream(excelFile);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            int rows = hssfSheet.getLastRowNum();

            for (int r = 0; r <= rows; ++r) {
                if (r != 0) {
                    HSSFRow hssfRow = hssfSheet.getRow(r);
                    if (hssfRow == null) {
                        break;
                    }

                    Tasa tasa = new Tasa();

                    tasa.setPlazoDD(formatValue(hssfRow.getCell(POS_PLAZO_DD)));
                    tasa.setPlazoHH(formatValue(hssfRow.getCell(POS_PLAZO_HH)));

                    // Formatear el atributo TNA como número con dos decimales, incluso si es un número entero
                    double tnaValue = cellValueToDouble(hssfRow.getCell(POS_TNA));
                    String tnaString = String.format("%.2f", tnaValue).replace(",", "."); // Modificación aquí
                    tasa.setTna(tnaString);

                    tasa.setTem(formatValue(hssfRow.getCell(POS_TEM)));
                    tasa.setPpp(formatValue(hssfRow.getCell(POS_PPP)));
                    tasa.setCodigoPro(formatValue(hssfRow.getCell(POS_CODIGO_PRO)));

                    tasas.add(tasa);
                }
            }
        } catch (FileNotFoundException var21) {
            System.out.println("The file not exists (No se encontró el fichero): " + var21);
        } catch (IOException var22) {
            System.out.println("Error in file procesing (Error al procesar el fichero): " + var22);
        } finally {
            try {
                excelStream.close();
            } catch (IOException var20) {
                System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + var20);
            }
        }

        return tasas;
    }

    private String formatValue(HSSFCell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            int intValue = (int) numericValue;
            return String.valueOf(intValue);
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                double numericValue = Double.parseDouble(cell.getStringCellValue().replace(",", "."));
                int intValue = (int) numericValue;
                return String.valueOf(intValue);
            } catch (NumberFormatException e) {
                return cell.getStringCellValue();
            }
        }
        return "";
    }

    private double cellValueToDouble(HSSFCell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
            } catch (NumberFormatException e) {
                return 0.0; // Valor predeterminado en caso de error
            }
        }
        return 0.0; // Valor predeterminado en caso de error
    }

    public static void main(String[] args) {
        File workingDirectory = new File("./");
        fc.setCurrentDirectory(workingDirectory);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new XlsxFilter());
        int returnVal = fc.showOpenDialog((Component) null);
        File fileTasas = null;
        if (returnVal == 0) {
            fileTasas = fc.getSelectedFile();
            System.out.println("Opening: " + fileTasas.getName() + ".");
            TasasVN tasasVN = new TasasVN();
            ArrayList<Tasa> tasas = tasasVN.getTasas(fileTasas);
            ArrayList<String> queryTasas = tasasVN.makeQuery(tasas);
            System.out.println("CANTIDAD DE TASAS A ACTUALIZAR: " + queryTasas.size());
            ArchivoSalida salida = new ArchivoSalida(queryTasas);
            salida.guardar(fileTasas.getPath().replace(".xls", "") + "_salida.sql");
        } else {
            System.out.println("Open command cancelled by user.");
        }

    }

    private ArrayList<String> makeQuery(ArrayList<Tasa> lTasas) {
        ArrayList<String> querys = new ArrayList();
        Iterator var3 = lTasas.iterator();

        while (var3.hasNext()) {
            Tasa t = (Tasa) var3.next();
            String query = "UPDATE [dbo].[SFB_SOLVALORESNEGOCIADOS_TASAS] \n    SET [TNA]=" + t.getTna() + " \n    WHERE \n[PlazoDD]=" + t.getPlazoDD() + " AND [PlazoHH]=" + t.getPlazoHH() + " AND [PPP]=" + t.getPpp() + " AND [CodigoPro]=" + t.getCodigoPro() + " \nGO\n\n";
            querys.add(query);
        }

        return querys;
    }

    private static class XlsxFilter extends FileFilter {
        private XlsxFilter() {
        }

        public boolean accept(File file) {
            return file.isDirectory() || file.getAbsolutePath().endsWith(".xls");
        }

        public String getDescription() {
            return "Documento xls (*.xls)";
        }
    }
}
