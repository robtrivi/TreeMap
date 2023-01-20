/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edd.treemap.utils;

import com.edd.treemap.App;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author robes
 */
public class Utils {

    public static Color generateRandomColor() {
        int[] rgb = new int[3];
        Random rd = new Random();
        int pos = rd.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == pos) {
                rgb[i] = 250;
            } else {
                rgb[i] = rd.nextInt(250);
            }
        }

        Color c = Color.rgb(rgb[0], rgb[1], rgb[2]);
        return c;
    }

    public static void changeColor(Rectangle r, StackPane explorador) {
        if (r == null) {
            return;
        }
        for (Node childs : explorador.getChildren()) {
            if (!childs.getStyle().equals(r.getStyle())) {
                childs.setStyle("-fx-fill: #e0e0e0; -fx-stroke: white; -fx-font-size: 15");
            }
        }

    }

    public static ImageView getImage(String ruta, int height, int width) {
        try ( FileInputStream input = new FileInputStream(App.rutaImagen + ruta)) {
            ImageView imgview = new ImageView();
            Image img = new Image(input);
            imgview.setImage(img);
            imgview.setFitHeight(height);
            imgview.setFitWidth(width);
            return imgview;
        } catch (IOException ex) {

        }
        return getImage("unknownfile.png", height, width);
    }

    public static String obtenerConversion(double s) {
        String sizeS = "";
        double size = 0;
        if (s > 1073741824) {
            sizeS = "GB";
            size = s / 1073741824;
        } else if (s > 1048576) {
            sizeS = "MB";
            size = s / 1048576;
        } else {
            sizeS = "KB";
            size = s / 1024;
        }
        return String.format("%.2f%s", size, sizeS);
    }

    public static String getExtension(String path) {
        String extension = "";

        int i = path.lastIndexOf('.');
        int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (i > p) {
            extension = path.substring(i + 1);
        }

        if (extension.isEmpty()) {
            return "carpeta";
        }

        return extension;
    }

}
