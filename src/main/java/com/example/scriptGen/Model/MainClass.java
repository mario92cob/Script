package com.example.scriptGen.Model;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.example.scriptGen.Model.JavaPoiUtils;

import java.io.File;

public  class MainClass {
    public static void main(String[] args) {
        // Crear un objeto JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Filtrar para mostrar solo archivos con extensión .xls o .xlsx
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel", "xls", "xlsx");
        fileChooser.setFileFilter(filter);

        // Mostrar el cuadro de diálogo para seleccionar un archivo
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // El usuario seleccionó un archivo
            File excelFile = fileChooser.getSelectedFile();

            // Crea una instancia de JavaPoiUtils
            JavaPoiUtils javaPoiUtils = new JavaPoiUtils();

            // Llama al método readExcelFile para procesar el archivo Excel
            javaPoiUtils.readExcelFile(excelFile);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

}