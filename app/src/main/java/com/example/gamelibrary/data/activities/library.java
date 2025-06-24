package com.example.gamelibrary.data.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamelibrary.R;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.Repository.JuegoRepository;
import com.example.gamelibrary.data.adapters.JuegoAdapter;
import com.example.gamelibrary.data.adapters.MisBibliotecasAdapter;
import com.example.gamelibrary.data.adapters.MisJuegosAdapter;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.BibliotecaConJuegos;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.ArrayList;
import java.util.List;

public class library extends AppCompatActivity {

    private TextView nombreBiblioteca;
    private RecyclerView recyclerView;
    private Biblioteca biblioteca;
    private List<Juego> juegos = new ArrayList<>();
    private MisJuegosAdapter adapter;
    private JuegoRepository juegoRepository;
    private BibliotecaRepository bibliotecaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombreBiblioteca = findViewById(R.id.tv_header_principal);

        recyclerView = findViewById(R.id.recycler_view_elementos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        biblioteca = (Biblioteca) getIntent().getSerializableExtra("biblioteca");

        nombreBiblioteca.setText(biblioteca.getTitulo());

        adapter = new MisJuegosAdapter(this, juegos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemActionListener(new MisJuegosAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Juego juego, int position) {
                Intent intent = new Intent(library.this, GameDetail.class);
                intent.putExtra("juego", juego);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(Juego juego, int position) {
                juegoRepository.deleteJuego(juego, new JuegoRepository.DataCallback<Void>() {
                    @Override
                    public void onDataLoaded(Void unused) {
                        runOnUiThread(() -> {
                            juegos.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(library.this, "Juego eliminado", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() ->
                            Toast.makeText(library.this, "Error al eliminar juego: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            }
        });

        juegoRepository = new JuegoRepository(this);
        bibliotecaRepository = new BibliotecaRepository(this);

        cargarJuegos(biblioteca.getId());
    }

    private void cargarJuegos(int bibliotecaId) {
        bibliotecaRepository.getBibliotecaConJuegos(bibliotecaId, new BibliotecaRepository.DataCallback<BibliotecaConJuegos>() {
            @Override
            public void onDataLoaded(BibliotecaConJuegos data) {
                runOnUiThread(() -> {
                    juegos.clear();
                    juegos.addAll(data.juegos);
                    adapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(library.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}