/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edd.treemap.modelo;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author robes
 */
public class TreeMap {

    private String nameDirectory;
    private Tree<Archivo> tree;
    
    public TreeMap(File file) {
        this.tree = crearDirectorio(file);
        this.nameDirectory = file.getName();
    }

    public String getNameDirectory() {
        return nameDirectory;
    }

    public void setNameDirectory(String nameDirectory) {
        this.nameDirectory = nameDirectory;
    }

    public Tree<Archivo> getTree() {
        return tree;
    }

    public void setTree(Tree<Archivo> tree) {
        this.tree = tree;
    }
    
    private Tree<Archivo> crearDirectorio(File file) {

        Tree<Archivo> tree_directory = new Tree(new Archivo(file.getName(), file.getPath(), file.length()));
        LinkedList<Tree<Archivo>> hijos = new LinkedList<>();

        if (file.isDirectory()) {
            tree_directory.getRoot().getContent().setDirectory(true);
            long totalSize = 0;
            for (File f : file.listFiles()) {
                try {
                    Tree<Archivo> temp = crearDirectorio(f);
                    hijos.add(temp);
                    totalSize += temp.getRoot().getContent().getSize();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            tree_directory.getRoot().getContent().setSize(totalSize);
        }
        tree_directory.getRoot().setChildren(hijos);
        return tree_directory;
    }

}
