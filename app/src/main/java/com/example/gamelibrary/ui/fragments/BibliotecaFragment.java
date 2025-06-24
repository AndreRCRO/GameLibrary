package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamelibrary.R;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.Repository.JuegoRepository;
import com.example.gamelibrary.data.activities.GameDetail;
import com.example.gamelibrary.data.adapters.BibliotecaAdapter;
import com.example.gamelibrary.data.adapters.JuegoAdapter;
import com.example.gamelibrary.data.adapters.MisJuegosAdapter;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaFragment extends Fragment {

    private JuegoRepository juegoRepository;
    private RecyclerView recyclerView;
    private List<Juego> juegos = new ArrayList<>();
    private MisJuegosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biblioteca, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_juegos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MisJuegosAdapter(getContext(), juegos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemActionListener(new MisJuegosAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Juego juego, int position) {
                Intent intent = new Intent(getContext(), GameDetail.class);
                intent.putExtra("juego", juego);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(Juego juego, int position) {
                juegoRepository.deleteJuego(juego, new JuegoRepository.DataCallback<Void>() {
                    @Override
                    public void onDataLoaded(Void unused) {
                        requireActivity().runOnUiThread(() -> {
                            juegos.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(getContext(), "Juego eliminado", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Error al eliminar juego: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            }
        });

        
        juegoRepository = new JuegoRepository(requireContext());

        cargarJuegos();

        return view;
    }

    private void cargarJuegos() {
        juegoRepository.getAllJuegos(new JuegoRepository.DataCallback<List<Juego>>() {
            @Override
            public void onDataLoaded(List<Juego> data) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        juegos.clear();
                        juegos.addAll(data);
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al cargar juegos: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}