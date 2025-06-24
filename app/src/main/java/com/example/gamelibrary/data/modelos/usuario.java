package com.example.gamelibrary.data.modelos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.Repository.JuegoRepository;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class usuario {
    private static usuario instancia;

    private int id;
    private String username;
    private JsonObject data;
    private String ultima_actualizacion;

    private usuario() {
    }

    // Método para obtener la instancia única
    public static synchronized usuario getInstance() {
        if (instancia == null) {
            instancia = new usuario();
        }
        return instancia;
    }

    // Setters y getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getUltimaActualizacion() {
        return ultima_actualizacion;
    }

    public void setUltimaActualizacion(String ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }
    public void clear() {
        id = 0;
        username = null;
        data = null;
        ultima_actualizacion = null;
    }

    public void restaurarBackupDesdeData(Context context) {
        Toast.makeText(context, "Restauracion en proceso", Toast.LENGTH_LONG).show();

        JuegoRepository juegoRepo = new JuegoRepository(context);
        BibliotecaRepository bibliotecaRepo = new BibliotecaRepository(context);

        try {
            List<Integer> juegosIds = new ArrayList<>();
            for (com.google.gson.JsonElement idElem : data.getAsJsonArray("juegos")) {
                juegosIds.add(idElem.getAsInt());
            }

            // Pedir info completa a API para los juegos
            JsonObject payload = new JsonObject();
            com.google.gson.JsonArray arrayIds = new com.google.gson.JsonArray();

            for (Integer idJuego : juegosIds) arrayIds.add(idJuego);
            payload.add("ids", arrayIds);

            String url = "http://10.0.2.2:5001/api/juegos/listaJuegos";

            // Usar Volley para petición POST
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    new JSONObject(payload.toString()),
                    response -> {
                        try {
                            com.google.gson.JsonArray juegosArray = com.google.gson.JsonParser
                                    .parseString(response.get("data").toString())
                                    .getAsJsonArray();
                            for (com.google.gson.JsonElement juegoElem : juegosArray) {
                                com.google.gson.JsonObject juegoObj = juegoElem.getAsJsonObject();
                                Juego juego = new Juego();
                                juego.setId(juegoObj.get("id").getAsInt());
                                juego.setTitulo(juegoObj.get("titulo").getAsString());
                                juego.setDescripcion(juegoObj.get("descripcion").getAsString());
                                juego.setAnio_lanzamiento(juegoObj.get("anio_lanzamiento").getAsInt());
                                juego.setImagen_url(juegoObj.get("imagen_url").getAsString());
                                juego.setDesarrolladora(juegoObj.get("desarrolladora").getAsString());
                                juego.setMetacritic(juegoObj.get("metacritic").getAsInt());

                                List<String> generos = new ArrayList<>();
                                if (juegoObj.has("generos") && juegoObj.get("generos").isJsonArray()) {
                                    for (com.google.gson.JsonElement generoElem : juegoObj.getAsJsonArray("generos")) {
                                        generos.add(generoElem.getAsString());
                                    }
                                }
                                juego.setGeneros(generos);

                                List<String> plataformas = new ArrayList<>();
                                if (juegoObj.has("plataformas") && juegoObj.get("plataformas").isJsonArray()) {
                                    for (com.google.gson.JsonElement plataformaElem : juegoObj.getAsJsonArray("plataformas")) {
                                        plataformas.add(plataformaElem.getAsString());
                                    }
                                }
                                juego.setPlataformas(plataformas);

                                juegoRepo.insertarJuego(juego, new JuegoRepository.DataCallback<Long>() {
                                    @Override
                                    public void onDataLoaded(Long id) { /* éxito */ }
                                    @Override
                                    public void onError(Exception e) { /* manejar error */ }
                                });
                            }

                            com.google.gson.JsonArray bibliotecasArray = data.getAsJsonArray("bibliotecas");
                            for (com.google.gson.JsonElement biblioElem : bibliotecasArray) {
                                com.google.gson.JsonObject biblioObj = biblioElem.getAsJsonObject();
                                int biblioId = biblioObj.get("id").getAsInt();
                                String nombre = biblioObj.get("nombre").getAsString();

                                Biblioteca biblio = new Biblioteca();
                                biblio.setId(biblioId);
                                biblio.setTitulo(nombre);

                                bibliotecaRepo.insertarBiblioteca(biblio, new BibliotecaRepository.DataCallback<Long>() {
                                    @Override
                                    public void onDataLoaded(Long id) {
                                        com.google.gson.JsonArray juegosArray = biblioObj.getAsJsonArray("juegos");
                                        for (com.google.gson.JsonElement juegoElem : juegosArray) {
                                            int juegoId = juegoElem.getAsInt();
                                            juegoRepo.insertarRelacionJuegoBiblioteca(biblioId, juegoId , new JuegoRepository.DataCallback<Void>() {
                                                @Override
                                                public void onDataLoaded(Void result) {
                                                    Log.e("RelacionExito", "Error al insertar relación: " );
                                                }
                                                @Override
                                                public void onError(Exception e) {
                                                    Log.e("RelacionError", "Error al insertar relación: " + e.getMessage(), e);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("BibliotecaError", "Error al insertar relación: " + e.getMessage(), e);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error al procesar respuesta", Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(context, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            );
            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error preparando restauración: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
