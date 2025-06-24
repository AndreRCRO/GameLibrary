package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamelibrary.R;
import com.example.gamelibrary.data.AppDatabase;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.activities.GameDetail;
import com.example.gamelibrary.data.activities.library;
import com.example.gamelibrary.data.adapters.BibliotecaAdapter;
import com.example.gamelibrary.data.modelos.Biblioteca;

import java.util.ArrayList;
import java.util.List;

public class CollectionsFragment extends Fragment {
    private BibliotecaRepository bibliotecaRepository;
    private RecyclerView recyclerView;
    private List<Biblioteca> listaBibliotecas = new ArrayList<>();
    private Button iv_add_collection;
    private BibliotecaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        iv_add_collection = view.findViewById(R.id.iv_add_collection);
        recyclerView = view.findViewById(R.id.recycler_view_colecciones);
        adapter = new BibliotecaAdapter(getContext(), listaBibliotecas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        bibliotecaRepository = new BibliotecaRepository(requireContext());

        iv_add_collection.setOnClickListener(v -> mostrarDialogoAgregarBiblioteca());

        adapter.setOnItemActionListener((biblioteca, position) -> {
            Intent intent = new Intent(getContext(), library.class);
            intent.putExtra("biblioteca", biblioteca);
            startActivity(intent);
        });
        cargarBibliotecas();

        return view;
    }

    private void mostrarDialogoAgregarBiblioteca() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Nueva Biblioteca");

        // Crear el input
        final android.widget.EditText input = new android.widget.EditText(getContext());
        input.setHint("Nombre de la biblioteca");
        input.setPadding(40, 30, 40, 30);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (!nombre.isEmpty()) {
                crearBiblioteca(nombre); // 🔹 Aquí debes crear la biblioteca
            } else {
                android.widget.Toast.makeText(getContext(), "Debe ingresar un nombre", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void crearBiblioteca(String nombre) {
        Biblioteca nuevaBiblioteca = new Biblioteca();
        nuevaBiblioteca.setTitulo(nombre);

        bibliotecaRepository.insertarBiblioteca(nuevaBiblioteca, new BibliotecaRepository.DataCallback<Long>() {
            @Override
            public void onDataLoaded(Long id) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Biblioteca creada con ID: " + id, Toast.LENGTH_SHORT).show();
                    cargarBibliotecas();
                });
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error al crear biblioteca: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void cargarBibliotecas() {
        bibliotecaRepository.getAllBibliotecasConConteo(new BibliotecaRepository.DataCallback<List<Biblioteca>>() {
            @Override
            public void onDataLoaded(List<Biblioteca> data) {
                requireActivity().runOnUiThread(() -> {
                    listaBibliotecas.clear();
                    listaBibliotecas.addAll(data);
                    adapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al cargar bibliotecas: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
