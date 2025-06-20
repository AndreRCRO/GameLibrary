package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.activities.GameDetail;
import com.example.gamelibrary.data.adapters.JuegoAdapter;
import com.example.gamelibrary.data.modelos.Juego;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuscarFragment extends Fragment {

    private EditText et_buscar;
    private RecyclerView recyclerView;
    private JuegoAdapter adapter;

    private final List<Juego> juegos = new ArrayList<>();             // Lista mostrada
    private final List<Juego> juegosOriginales = new ArrayList<>();   // Lista completa

    private final String JUEGOS_URL = "http://10.0.2.2:5001/api/juegos"; // o tu IP local real


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        recyclerView = view.findViewById(R.id.rv_resultados_busqueda);
        et_buscar = view.findViewById(R.id.et_buscar);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new JuegoAdapter(getContext(), juegos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemActionListener((juego, position) -> {
            Intent intent = new Intent(getContext(), GameDetail.class);
            intent.putExtra("juego", juego);
            startActivity(intent);
        });

        // Filtrado en tiempo real
        et_buscar.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarJuegos(s.toString());
            }
        });

        cargarJuegosDesdeAPI();

        return view;
    }

    private void cargarJuegosDesdeAPI() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                JUEGOS_URL,
                null,
                response -> {
                    try {
                        juegos.clear();
                        juegosOriginales.clear();

                        JSONArray juegosArray = response.getJSONArray("data");
                        for (int i = 0; i < juegosArray.length(); i++) {
                            JSONObject obj = juegosArray.getJSONObject(i);
                            Juego juego = new Juego();

                            juego.setId(obj.getInt("id"));
                            juego.setTitulo(obj.getString("titulo"));
                            juego.setImagen_url(obj.getString("imagen_url"));
                            juego.setAnio_lanzamiento(obj.getInt("anio_lanzamiento"));
                            juego.setDesarrolladora(obj.getString("desarrolladora"));
                            juego.setDescripcion(obj.getString("descripcion"));
                            juego.setMetacritic(obj.getInt("metacritic"));

                            JSONArray generosArray = obj.getJSONArray("generos");
                            List<String> generos = new ArrayList<>();
                            for (int j = 0; j < generosArray.length(); j++) {
                                generos.add(generosArray.getString(j));
                            }
                            juego.setGeneros(generos);

                            JSONArray plataformasArray = obj.getJSONArray("plataformas");
                            List<String> plataformas = new ArrayList<>();
                            for (int j = 0; j < plataformasArray.length(); j++) {
                                plataformas.add(plataformasArray.getString(j));
                            }
                            juego.setPlataformas(plataformas);

                            juegos.add(juego);
                            juegosOriginales.add(juego); // <- importante para filtrado
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void filtrarJuegos(String texto) {
        String consulta = texto.toLowerCase().trim();
        juegos.clear();

        if (consulta.isEmpty()) {
            juegos.addAll(juegosOriginales);
        } else {
            for (Juego juego : juegosOriginales) {
                if (juego.getTitulo().toLowerCase().contains(consulta)) {
                    juegos.add(juego);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
