package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.AppDatabase;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.Repository.JuegoRepository;
import com.example.gamelibrary.data.activities.Login;
import com.example.gamelibrary.data.modelos.BackupData;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.BibliotecaConJuegos;
import com.example.gamelibrary.data.modelos.Juego;
import com.example.gamelibrary.data.modelos.usuario;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profileFragment extends Fragment {

    private TextView tv_username;
    private Button btn_desconectar, btn_hacer_backup;
    usuario user = usuario.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        tv_username = view.findViewById(R.id.tv_username);
        btn_desconectar = view.findViewById(R.id.btn_desconectar);
        btn_hacer_backup = view.findViewById(R.id.btn_hacer_backup);

        tv_username.setText(user.getUsername());

        btn_desconectar.setOnClickListener(v -> finalizarSesion());

        btn_hacer_backup.setOnClickListener(v -> generarBackup());

        return view;
    }
    private void generarBackup() {
        BibliotecaRepository bibliotecaRepo = new BibliotecaRepository(requireContext());
        JuegoRepository juegoRepo = new JuegoRepository(requireContext());

        juegoRepo.getAllJuegos(new JuegoRepository.DataCallback<List<Juego>>() {
            @Override
            public void onDataLoaded(List<Juego> juegos) {
                List<Integer> idsJuegos = new ArrayList<>();
                for (Juego juego : juegos) {
                    idsJuegos.add(juego.getId());
                }

                bibliotecaRepo.getAllBibliotecasConConteo(new BibliotecaRepository.DataCallback<List<Biblioteca>>() {
                    @Override
                    public void onDataLoaded(List<Biblioteca> bibliotecas) {
                        List<BackupData.BibliotecaBackup> listaBibliotecas = new ArrayList<>();

                        final int[] pending = {bibliotecas.size()};
                        for (Biblioteca b : bibliotecas) {
                            bibliotecaRepo.getBibliotecaConJuegos(b.getId(), new BibliotecaRepository.DataCallback<BibliotecaConJuegos>() {
                                @Override
                                public void onDataLoaded(BibliotecaConJuegos data) {
                                    List<Integer> juegosIds = new ArrayList<>();
                                    for (Juego j : data.juegos) {
                                        juegosIds.add(j.getId());
                                    }
                                    listaBibliotecas.add(new BackupData.BibliotecaBackup(b.getId(), b.getTitulo(), juegosIds));

                                    if (--pending[0] == 0) {
                                        enviarBackupAlServidor(idsJuegos, listaBibliotecas);
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    if (--pending[0] == 0) {
                                        enviarBackupAlServidor(idsJuegos, listaBibliotecas);
                                    }
                                }
                            });
                        }
                        if (bibliotecas.isEmpty()) {
                            enviarBackupAlServidor(idsJuegos, listaBibliotecas);
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void enviarBackupAlServidor(List<Integer> juegos, List<BackupData.BibliotecaBackup> bibliotecas) {
        try {
            JSONObject jsonBackup = new JSONObject();
            JSONArray juegosArray = new JSONArray();
            JSONArray bibliotecasArray = new JSONArray();

            for (Integer juegoId : juegos) {
                juegosArray.put(juegoId);
            }
            for (BackupData.BibliotecaBackup b : bibliotecas) {
                JSONObject objBiblio = new JSONObject();
                objBiblio.put("id", b.getId());
                objBiblio.put("nombre", b.getNombre());

                JSONArray juegosIdsBiblio = new JSONArray();
                for (Integer idJuego : b.getJuegos()) {
                    juegosIdsBiblio.put(idJuego);
                }
                objBiblio.put("juegos", juegosIdsBiblio);

                bibliotecasArray.put(objBiblio);
            }
            JSONObject dataObject = new JSONObject();
            dataObject.put("juegos", juegosArray);
            dataObject.put("bibliotecas", bibliotecasArray);

            JSONObject finalJson = new JSONObject();
            finalJson.put("data", dataObject);

            int userId = user.getId();

            String url = "http://10.0.2.2:5001/api/usuarios/" + userId;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    finalJson,  // enviamos el JSON con la envoltura "data"
                    response -> {
                        Toast.makeText(requireContext(), "Backup enviado correctamente", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(requireContext(), "Error al enviar backup: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("BackupError", error.toString());
                    }
            ) {
                @Override
                public java.util.Map<String, String> getHeaders() {
                    java.util.Map<String, String> headers = new java.util.HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            Volley.newRequestQueue(requireContext()).add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al preparar backup: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void finalizarSesion() {
        limpiarDatosBD();

        user.clear();

        SharedPreferences prefs = requireContext().getSharedPreferences("mi_app_prefs", requireContext().MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(requireContext(), Login.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private void limpiarDatosBD() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            db.clearAllData();
        }).start();
    }
}
