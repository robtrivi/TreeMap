/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.edd.treemap;

import com.edd.treemap.modelo.Tree;
import com.edd.treemap.modelo.TreeMap;
import com.edd.treemap.modelo.Archivo;
import com.edd.treemap.utils.Utils;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author robes
 */
public class HomeController implements Initializable {

    @FXML
    private Button btnPath;
    @FXML
    private TextField ruta;
    @FXML
    private Button btnExplorador;
    @FXML
    private Label lbDirectory;
    @FXML
    private StackPane stackContainer;
    @FXML
    private CheckBox checkFiltro;
    @FXML
    private VBox vbLabel;

    private Stage stagePopUp = new Stage();

    private HashMap<String, Color> colors = new HashMap();

    private HashMap<Integer, String> node_color = new HashMap();

    private Tree<Archivo> directory;

    private Deque<Tree<Archivo>> dequeDirectory = new LinkedList();

    private TreeMap tm;

    private Rectangle temp_r;
    @FXML
    private Label lbMasInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colors.put("pdf", Color.rgb(255, 113, 113));
        colors.put("docx", Color.rgb(66, 115, 255));
        colors.put("xlsx", Color.rgb(113, 255, 124));
        colors.put("pptx", Color.rgb(255, 190, 113));
        colors.put("jpeg", Color.rgb(201, 255, 113));
        colors.put("jpg", Color.rgb(255, 238, 113));
        colors.put("png", Color.rgb(150, 130, 160));
        colors.put("txt", Color.rgb(103, 194, 195));
    }

    @FXML
    private void selectPath() {
        DirectoryChooser fc = new DirectoryChooser();
        File selectedFile = fc.showDialog(null);
        if (selectedFile != null) {
            checkFiltro.setDisable(false);
            checkFiltro.setSelected(false);
            lbMasInfo.setVisible(true);
            colors.put("", Color.rgb(250, 250, 114));
            tm = new TreeMap(selectedFile);
            vbLabel.setVisible(true);
            directory = tm.getTree();
            Archivo principal = directory.getRoot().getContent();
            lbDirectory.setText(String.format("%s - %s", principal.getName(), Utils.obtenerConversion(principal.getSize())));
            ruta.setText(selectedFile.getPath());
            node_color.clear();
            renderTreemap();
        }
    }

    private void renderTreemap() {
        btnExplorador.setDisable(false);
        stackContainer.getChildren().clear();
        if (directory != null) {
            ArrayList<String> arr = new ArrayList<>();
            createTreemap(directory, vbLabel.getWidth(), 550, arr);
            stackContainer.setOnMouseExited(e -> {
                stackContainer.getChildren().clear();
                createTreemap(directory, vbLabel.getWidth(), 550, arr);
            });
            checkFiltro.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    stackContainer.setOnMouseMoved(e -> {
                        Utils.changeColor(temp_r, stackContainer);
                    });
                } else {
                    stackContainer.setOnMouseMoved(null);
                    stackContainer.setOnMouseExited(null);
                }
            });
        }
    }

    private void createTreemap(Tree<Archivo> directorio, double x, double y, double width, double height, boolean rotate, ArrayList<String> permitidas) {

        stackContainer.setAlignment(Pos.TOP_LEFT);
        Rectangle rect = new Rectangle(width, height);
        String extension = directorio.getRoot().getContent().getExtension();
        Color c;
        if (permitidas != null && !permitidas.isEmpty() && !permitidas.contains(extension)) {
            c = Color.web("#e0e0e0");
        } else {
            c = colors.getOrDefault(extension, Utils.generateRandomColor());
        }
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
        colors.put(extension, c);
        rect.setStyle("-fx-fill: " + hex + "; -fx-stroke: white;");
        node_color.put(stackContainer.getChildren().size(), "-fx-fill: " + hex + "; -fx-stroke: white; -fx-font-size: 15");

        rect.setTranslateX(x);
        rect.setTranslateY(y);

        double initialX = x;
        double initialY = y;

        LinkedList<Tree<Archivo>> children = directorio.getRoot().getChildren();
        int numChildren = children.size();

        if (children.isEmpty()) {

            rect.setOnMouseClicked(eh -> {
                Scene sc = popup(directorio.getRoot().getContent());
                stagePopUp.setScene(sc);
                if (!stagePopUp.isShowing()) {
                    stagePopUp.show();
                }

            });

            rect.setOnMouseEntered(eh -> {
                temp_r = rect;
            });
            rect.setOnMouseExited(eh -> {
                for (int i : node_color.keySet()) {
                    stackContainer.getChildren().get(i).setStyle(node_color.get(i));
                }
            });
        }

        stackContainer.getChildren().add(rect);

        if (!rotate) {
            for (int i = 0; i < numChildren; i++) {
                Tree<Archivo> child = children.get(i);
                double childWidth = (width * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                createTreemap(child, x, y, childWidth, height, true, permitidas);
                x += childWidth;
            }

        } else {
            for (int i = 0; i < numChildren; i++) {
                Tree<Archivo> child = children.get(i);
                double childHeight = (height * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                createTreemap(child, x, y, width, childHeight, false, permitidas);
                y += childHeight;
            }
        }
    }

    public void createTreemap(Tree<Archivo> directorio, double width, double height, ArrayList<String> permitidas) {
        createTreemap(directorio, 0, 0, width, height, false, permitidas);
    }

    @FXML
    private void explorar() {
        Stage stage = new Stage();
        stage.setTitle("TreeView");
        VBox root = new VBox();
        root.setStyle("-fx-background-color: WHITE");
        root.setAlignment(Pos.CENTER);
        ScrollPane contenido = new ScrollPane();
        contenido.setFitToWidth(true);
        VBox exploBox = new VBox();
        exploBox.setStyle("-fx-background-color: WHITE");
        exploBox.setPadding(new Insets(5, 5, 5, 5));
        Button back = new Button("Volver");
        back.setDisable(true);
        back.setOnMouseClicked(mc -> {
            dequeDirectory.pop();
            if (!dequeDirectory.isEmpty()) {
                searchPath(dequeDirectory.pop(), back, exploBox);
            }
        });
        searchPath(directory, back, exploBox);
        contenido.setContent(exploBox);
        root.getChildren().addAll(back, contenido);
        root.setSpacing(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(root, 500, 400);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void searchPath(Tree<Archivo> path, Button back, VBox explorador) {
        if (path != directory) {
            back.setDisable(false);
        }else{
            back.setDisable(true);
        }
        explorador.getChildren().clear();
        dequeDirectory.push(path);
        for (Tree<Archivo> file : path.getRoot().getChildren()) {
            Label nombrearchivo = new Label(file.getRoot().getContent().getName());
            nombrearchivo.setFont(new Font("System", 14));
            ImageView imgview = Utils.getImage(file.getRoot().getContent().getExtension() + ".png", 20, 20);
            HBox archivoBox = new HBox(imgview, nombrearchivo);
            if (file.getRoot().getContent().isDirectory()) {
                archivoBox.setOnMouseClicked(mc -> {
                    searchPath(file, back, explorador);
                });
            }
            archivoBox.setSpacing(5);
            archivoBox.setPadding(new Insets(3, 3, 3, 3));
            explorador.getChildren().add(archivoBox);
        }
    }

    private Scene popup(Archivo a) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: WHITE");
        root.setSpacing(20);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);
        Label lbarchivo = new Label("Nombre: " + a.getName());
        lbarchivo.setFont(Font.font("System", 14));
        lbarchivo.setWrapText(true);
        String s = Utils.obtenerConversion(a.getSize());
        Label lbpeso = new Label("Peso: " + s);
        lbpeso.setFont(Font.font("System", 14));
        lbpeso.setWrapText(true);
        ImageView imgview = Utils.getImage(a.getExtension() + ".png", 40, 40);
        root.getChildren().addAll(lbarchivo, lbpeso, imgview);
        Scene scene = new Scene(root);
        return scene;
    }
}
