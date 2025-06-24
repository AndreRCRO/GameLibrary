package com.example.gamelibrary.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "juego-biblioteca",
        primaryKeys = {"FK_id_Biblioteca", "FK_id_Juego"},
        foreignKeys = {
                @ForeignKey(
                        entity = Juego.class,
                        parentColumns = "id",
                        childColumns = "FK_id_Juego",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Biblioteca.class,
                        parentColumns = "id",
                        childColumns = "FK_id_Biblioteca",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class JuegoBiblioteca {
    @ColumnInfo(name = "FK_id_Biblioteca")
    private int FK_id_Biblioteca;

    @ColumnInfo(name = "FK_id_Juego")
    private int FK_id_Juego;

    public JuegoBiblioteca(int FK_id_Biblioteca, int FK_id_Juego) {
        this.FK_id_Biblioteca = FK_id_Biblioteca;
        this.FK_id_Juego = FK_id_Juego;
    }

    // Getters y Setters
    public int getFK_id_Biblioteca() {
        return FK_id_Biblioteca;
    }

    public void setFK_id_Biblioteca(int FK_id_Biblioteca) {this.FK_id_Biblioteca = FK_id_Biblioteca;}

    public int getFK_id_Juego() {
        return FK_id_Juego;
    }

    public void setFK_id_Juego(int FK_id_Juego) {
        this.FK_id_Juego = FK_id_Juego;
    }
}
