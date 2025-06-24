package com.example.gamelibrary.data.modelos;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class JuegoConBibliotecas {
    @Embedded
    public Juego juego;

    @Relation(
            parentColumn = "id", // id de Juego
            entityColumn = "id", // id de Biblioteca
            associateBy = @Junction(
                    value = JuegoBiblioteca.class,
                    parentColumn = "FK_id_Juego",
                    entityColumn = "FK_id_Biblioteca"
            )
    )
    public List<Biblioteca> bibliotecas;
}

