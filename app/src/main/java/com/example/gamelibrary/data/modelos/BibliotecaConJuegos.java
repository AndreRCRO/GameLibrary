package com.example.gamelibrary.data.modelos;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class BibliotecaConJuegos {
    @Embedded
    public Biblioteca biblioteca;

    @Relation(
            parentColumn = "id",  // columna de Biblioteca
            entityColumn = "id",  // columna de Juego
            associateBy = @Junction(
                    value = JuegoBiblioteca.class,
                    parentColumn = "FK_id_Biblioteca",
                    entityColumn = "FK_id_Juego"
            )
    )
    public List<Juego> juegos;
}
