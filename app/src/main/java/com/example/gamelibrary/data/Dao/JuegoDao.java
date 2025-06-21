package com.example.gamelibrary.data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gamelibrary.data.modelos.Juego;

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
}
