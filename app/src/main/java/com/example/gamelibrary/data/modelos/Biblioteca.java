package com.example.gamelibrary.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "biblioteca")
public class Biblioteca implements Serializable {
    @PrimaryKey
    private int id;
    private String titulo;
}
