package com.example.gamelibrary.data.Repository;

import android.content.Context;

import com.example.gamelibrary.data.AppDatabase;
import com.example.gamelibrary.data.Dao.BibliotecaDao;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.BibliotecaConJuegos;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BibliotecaRepository {
    private final BibliotecaDao bibliotecaDao;
    private final ExecutorService executorService;
    private final AppDatabase db;

    public BibliotecaRepository(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.bibliotecaDao = db.bibliotecaDao();
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(Exception e);
    }

    public void getAllBibliotecas(DataCallback<List<Biblioteca>> callback) {
        executorService.execute(() -> {
            try {
                List<Biblioteca> data = bibliotecaDao.getAllBibliotecas();
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getAllBibliotecasConConteo(DataCallback<List<Biblioteca>> callback) {
        executorService.execute(() -> {
            try {
                List<Biblioteca> data = bibliotecaDao.getAllBibliotecas();
                for (Biblioteca b : data) {
                    int count = bibliotecaDao.contarJuegosEnBiblioteca(b.getId());
                    b.setElementos(count);
                }
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertarBiblioteca(Biblioteca biblioteca, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = bibliotecaDao.insertarBiblioteca(biblioteca);
                callback.onDataLoaded(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getBibliotecaConJuegos(int bibliotecaId, DataCallback<BibliotecaConJuegos> callback) {
        executorService.execute(() -> {
            try {
                BibliotecaConJuegos result = bibliotecaDao.getJuegosPorBiblioteca(bibliotecaId);
                callback.onDataLoaded(result);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
    public void updateBiblioteca(Biblioteca biblioteca, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                bibliotecaDao.updateBiblioteca(biblioteca);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteBiblioteca(Biblioteca biblioteca, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                bibliotecaDao.deleteBiblioteca(biblioteca);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
}
