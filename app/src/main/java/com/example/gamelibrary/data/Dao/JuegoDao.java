package com.example.gamelibrary.data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.gamelibrary.data.modelos.Juego;
import com.example.gamelibrary.data.modelos.JuegoConBibliotecas;

import java.util.List;
@Dao
public interface JuegoDao {
    @Query("SELECT * FROM juego ORDER BY id DESC")
    List<Juego> getAllJueogs();

    @Query("SELECT * FROM juego WHERE id = :id")
    Juego getJuegoById(int id);

    @Insert
    long insertarJuego(Juego juego);

    @Delete
    void deleteJuego(Juego juego);

    @Transaction
    @Query("SELECT * FROM juego WHERE id = :juegoId")
    JuegoConBibliotecas getBibliotecasConJuego(int juegoId);
}
