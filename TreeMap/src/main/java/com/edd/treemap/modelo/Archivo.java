package com.edd.treemap.modelo;

import com.edd.treemap.utils.Utils;

public class Archivo {
    private String name;
    private long size;
    private boolean directory;
    private String extension;

    public Archivo(String name, String path, long size) {
        this.name = name;
        this.extension = Utils.getExtension(path);
        this.size = size;
        this.directory = false;
    }

    
    public String getExtension() {
        return extension;
    }

    public void setExtension(String path) {
        this.extension = path;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    
    
    @Override
    public String toString() {
        return "Archivo{" + "name=" + name + ", size=" + size + '}';
    }
    
    
}
