package com.example.gamelibrary.data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.BibliotecaConJuegos;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.List;
@Dao
public interface BibliotecaDao {
    @Query("SELECT * FROM biblioteca ORDER BY id DESC")
    List<Biblioteca> getAllBibliotecas();

    @Update
    void updateBiblioteca(Biblioteca biblioteca);

    @Delete
    void deleteBiblioteca(Biblioteca biblioteca);

    @Insert
    long insertarBiblioteca(Biblioteca biblioteca);

    @Transaction
    @Query("SELECT * FROM biblioteca WHERE id = :bibliotecaId")
    BibliotecaConJuegos getJuegosPorBiblioteca(int bibliotecaId);

}
