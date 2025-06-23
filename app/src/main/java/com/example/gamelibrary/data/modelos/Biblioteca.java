package com.example.gamelibrary.data.modelos;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "biblioteca")
public class Biblioteca implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    @Ignore
    private int elementos;


    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    @Ignore
    public int getElementos() {return elementos;}
    @Ignore
    public void setElementos(int elementos) {this.elementos = elementos;}
}
