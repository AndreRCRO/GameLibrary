package com.example.gamelibrary.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gamelibrary.data.Dao.BibliotecaDao;
import com.example.gamelibrary.data.Dao.JuegoBibliotecaDao;
import com.example.gamelibrary.data.Dao.JuegoDao;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;
import com.example.gamelibrary.data.modelos.JuegoBiblioteca;

@Database(
        entities = {Juego.class, Biblioteca.class, JuegoBiblioteca.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "juegos_db";

    private static AppDatabase instance;

    // DAOs expuestos por la base de datos
    public abstract JuegoDao juegoDao();
    public abstract BibliotecaDao bibliotecaDao();
    public abstract JuegoBibliotecaDao juegobibliotecaDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
