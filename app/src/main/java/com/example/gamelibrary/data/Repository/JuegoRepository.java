package com.example.gamelibrary.data.Repository;

import android.content.Context;

import com.example.gamelibrary.data.AppDatabase;
import com.example.gamelibrary.data.Dao.JuegoDao;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JuegoRepository {
    private final JuegoDao juegoDao;
    private final ExecutorService executorService;
    private final AppDatabase db;

    public JuegoRepository(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.juegoDao = db.juegoDao();
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(Exception e);
    }

    public void getAllJuegos(JuegoRepository.DataCallback<List<Juego>> callback) {
        executorService.execute(() -> {
            try {
                List<Juego> data = juegoDao.getAllJueogs();
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertarJuego(Juego juego, JuegoRepository.DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = juegoDao.insertarJuego(juego);
                callback.onDataLoaded(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteJuego(Juego juego, JuegoRepository.DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                juegoDao.deleteJuego(juego);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
}
