package com.example.gamelibrary.data.modelos;

import java.util.List;

public class Juego {
    private int id;
    private String titulo;
    private String descripcion;
    private int anio_lanzamiento;
    private String desarrolladora;
    private String imagen_url;
    private int metacritic;
    private List<String> generos;
    private List<String> plataformas;

    // Getters y Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getImagenUrl() { return imagen_url; }
    // Puedes añadir más según necesites

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setAnio_lanzamiento(int anio_lanzamiento) { this.anio_lanzamiento = anio_lanzamiento; }
    public void setDesarrolladora(String desarrolladora) { this.desarrolladora = desarrolladora; }
    public void setImagen_url(String imagen_url) { this.imagen_url = imagen_url; }
    public void setMetacritic(int metacritic) { this.metacritic = metacritic; }
    public void setGeneros(List<String> generos) { this.generos = generos; }
    public void setPlataformas(List<String> plataformas) { this.plataformas = plataformas; }

}