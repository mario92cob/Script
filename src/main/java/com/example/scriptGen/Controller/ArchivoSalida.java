//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.scriptGen.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ArchivoSalida {
    private final ArrayList<String> contenido = new ArrayList();

    public ArchivoSalida(ArrayList<String> contenido) {
        contenido.forEach((s) -> {
            this.contenido.add(s);
        });
    }

    public boolean guardar(String path) {
        File archivo = new File(path);
        boolean result = true;

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
            Iterator var5 = this.contenido.iterator();

            while(var5.hasNext()) {
                String c = (String)var5.next();
                bw.write(c);
            }

            bw.close();
        } catch (IOException var7) {
            result = false;
            System.err.println("ERROR AL GUARDAR EL ARCHIVO " + path + ": " + var7.getMessage());
        }

        return result;
    }
}
