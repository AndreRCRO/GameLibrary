package com.example.gamelibrary.data.modelos;

import com.google.gson.JsonObject;

public class usuario {
    private static usuario instancia;

    private int id;
    private String nombre;
    private String username;
    private JsonObject data; // com.google.gson.JsonObject
    private String ultima_actualizacion;

    // Constructor privado para evitar instanciación externa
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    // Método para limpiar la instancia (por ejemplo al cerrar sesión)
    public void clear() {
        id = 0;
        nombre = null;
        username = null;
        data = null;
        ultima_actualizacion = null;
    }
}
