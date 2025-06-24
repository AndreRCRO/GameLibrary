package com.example.gamelibrary.data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gamelibrary.data.modelos.JuegoBiblioteca;

@Dao
public interface JuegoBibliotecaDao {
    @Insert
    void insertarJuegoBiblioteca(JuegoBiblioteca juegoBiblioteca);

    @Delete
    void eliminarJuegoBiblioteca(JuegoBiblioteca juegoBiblioteca);

    @Query("DELETE FROM `juego-biblioteca`")
    void deleteAll();
}
