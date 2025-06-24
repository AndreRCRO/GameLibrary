package com.example.gamelibrary.data.modelos;

import java.util.List;

public class BackupData {
    public List<Integer> juegos;
    public List<BibliotecaBackup> bibliotecas;

    public static class BibliotecaBackup {
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

        public List<Integer> getJuegos() {
            return juegos;
        }

        public void setJuegos(List<Integer> juegos) {
            this.juegos = juegos;
        }

        public int id;
        public String nombre;
        public List<Integer> juegos;
        public BibliotecaBackup(int id, String nombre, List<Integer> juegos) {
            this.id = id;
            this.nombre = nombre;
            this.juegos = juegos;
        }
    }
}

