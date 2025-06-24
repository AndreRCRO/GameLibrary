package com.example.gamelibrary.data.activities;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.Repository.BibliotecaRepository;
import com.example.gamelibrary.data.Repository.JuegoRepository;
import com.example.gamelibrary.data.adapters.MisBibliotecasAdapter;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;
import com.example.gamelibrary.data.modelos.JuegoConBibliotecas;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;


public class GameDetail extends AppCompatActivity {
    private ImageView iv_game_image;
    private TextView tv_game_year, tv_game_platforms, tv_metacritic_score, tv_game_description;
    private ImageButton btn_back, btn_favorite, btn_add;
    private FlexboxLayout flexbox_genres;
    private JuegoRepository juegoRepository;
    private boolean agregado = false;
    private Juego juego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        iv_game_image = findViewById(R.id.iv_game_image);
        tv_game_year = findViewById(R.id.tv_game_year);
        tv_game_platforms = findViewById(R.id.tv_game_platforms);
        tv_metacritic_score = findViewById(R.id.tv_metacritic_score);
        tv_game_description = findViewById(R.id.tv_game_description);
        btn_back = findViewById(R.id.btn_back);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_add = findViewById(R.id.btn_add);
        flexbox_genres = findViewById(R.id.flexbox_genres);

        juegoRepository = new JuegoRepository(this);

        juego = (Juego) getIntent().getSerializableExtra("juego");
        verificarJuego();
        Glide.with(this)
                .load(juego.getImagenUrl())
                .into(iv_game_image);
        tv_game_year.setText(String.valueOf(juego.getAnio_lanzamiento()));
        tv_game_platforms.setText(String.join(", ", juego.getPlataformas()));

        for (String genero : juego.getGeneros()) {
            TextView textView = new TextView(this);
            textView.setText(genero);
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            textView.setTextSize(12);
            textView.setBackgroundResource(R.drawable.genre_chip_background);
            textView.setPadding(20, 10, 20, 10);

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            textView.setLayoutParams(params);

            flexbox_genres.addView(textView);
        }

        tv_metacritic_score.setText(String.valueOf(juego.getMetacritic()));
        tv_game_description.setText(juego.getDescripcion());

        btn_back.setOnClickListener(view -> finish());

        btn_favorite.setOnClickListener(v -> toggleFavorite());

        btn_add.setOnClickListener(v -> showCollectionsDialog(juego));
    }

    private void toggleFavorite() {
        if (agregado)
        {
            juegoRepository.deleteJuego(juego, new JuegoRepository.DataCallback<Void>() {
                @Override
                public void onDataLoaded(Void data) {
                    runOnUiThread(() -> {
                        agregado = false;
                        Toast.makeText(GameDetail.this, "Juego eliminado de favoritos" , Toast.LENGTH_SHORT).show();
                        btn_favorite.setImageResource(R.drawable.ic_favorite_border);
                        btn_favorite.setColorFilter(ContextCompat.getColor(GameDetail.this, android.R.color.white));
                    });
                }
                @Override
                public void onError(Exception e) {

                }
            });
        }
        else
        {
            juegoRepository.insertarJuego(juego, new JuegoRepository.DataCallback<Long>() {
                @Override
                public void onDataLoaded(Long id) {
                    runOnUiThread(() -> {
                        agregado = true;
                        Toast.makeText(GameDetail.this, "Juego agregado a favoritos con ID: " + id, Toast.LENGTH_SHORT).show();
                        btn_favorite.setImageResource(R.drawable.ic_favorite);
                        btn_favorite.setColorFilter(ContextCompat.getColor(GameDetail.this, android.R.color.holo_red_light));
                    });
                }
                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(GameDetail.this, "Error al agregar favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
        btn_favorite.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(100)
                .withEndAction(() -> btn_favorite.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start())
                .start();
    }

    private void showCollectionsDialog(Juego juego) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_collections, null);
        dialogView.setTag(juego);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();


        ImageView iv_close_modal = dialogView.findViewById(R.id.iv_close_modal);
        RecyclerView recyclerView = dialogView.findViewById(R.id.rv_colecciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JuegoRepository juegoRepo = new JuegoRepository(this);
        BibliotecaRepository bibliotecaRepo = new BibliotecaRepository(this);

        List<Biblioteca> bibliotecas = new ArrayList<>();
        List<Integer> idsAsociados = new ArrayList<>();
        MisBibliotecasAdapter adapter = new MisBibliotecasAdapter(this, bibliotecas, idsAsociados);
        recyclerView.setAdapter(adapter);
        iv_close_modal.setOnClickListener(v -> dialog.dismiss());
        adapter.setOnBibliotecaCheckedChangeListener((biblioteca, isChecked) -> {
            if (isChecked) {
                juegoRepository.insertarRelacionJuegoBiblioteca(biblioteca.getId(), juego.getId(), new JuegoRepository.DataCallback<Void>() {
                    @Override
                    public void onDataLoaded(Void data) {
                        runOnUiThread(() -> {
                            Toast.makeText(GameDetail.this, "Se ha agregado el juego a la biblioteca", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        juegoRepo.insertarJuego(juego, new JuegoRepository.DataCallback<Long>() {
                            @Override
                            public void onDataLoaded(Long id) {
                                juegoRepository.insertarRelacionJuegoBiblioteca(biblioteca.getId(), juego.getId(), new JuegoRepository.DataCallback<Void>() {
                                    @Override
                                    public void onDataLoaded(Void data) {
                                        runOnUiThread(() -> {
                                            agregado = true;
                                            Toast.makeText(GameDetail.this, "Juego insertado y agregado a la biblioteca", Toast.LENGTH_SHORT).show();
                                            btn_favorite.setImageResource(R.drawable.ic_favorite);
                                            btn_favorite.setColorFilter(ContextCompat.getColor(GameDetail.this, android.R.color.holo_red_light));
                                        });
                                    }

                                    @Override
                                    public void onError(Exception e2) {
                                        runOnUiThread(() -> {
                                            Toast.makeText(GameDetail.this, "Error al agregar juego a biblioteca incluso después de insertar el juego", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                });
                            }
                            @Override
                            public void onError(Exception e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(GameDetail.this, "Error al agregar favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    }
                });
            } else {
                juegoRepository.eliminarRelacionJuegoBiblioteca(biblioteca.getId(), juego.getId() , new JuegoRepository.DataCallback<Void>() {
                    @Override
                    public void onDataLoaded(Void data) {
                        runOnUiThread(() -> {
                            Toast.makeText(GameDetail.this, "Se ha eliminado el juego a la biblioteca", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(GameDetail.this, "Error al eliminar juego de biblioteca", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });

        bibliotecaRepo.getAllBibliotecasConConteo(new BibliotecaRepository.DataCallback<List<Biblioteca>>() {
            @Override
            public void onDataLoaded(List<Biblioteca> data) {
                runOnUiThread(() -> {
                    bibliotecas.clear();
                    bibliotecas.addAll(data);
                    adapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onError(Exception e) { }
        });

        juegoRepo.getJuegosConBiblioteca(juego.getId(), new JuegoRepository.DataCallback<JuegoConBibliotecas>() {
            @Override
            public void onDataLoaded(JuegoConBibliotecas data) {
                for (Biblioteca b : data.bibliotecas) {
                    idsAsociados.add(b.getId());
                }
                runOnUiThread(adapter::notifyDataSetChanged);
            }

            @Override
            public void onError(Exception e) { }
        });

        dialog.show();
        dialog.getWindow().setLayout(
                (int)(getResources().getDisplayMetrics().widthPixels * 0.90),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }


    private void verificarJuego() {
        juegoRepository.getJuegoById(juego.getId(), new JuegoRepository.DataCallback<Juego>() {
            @Override
            public void onDataLoaded(Juego data) {
                runOnUiThread(() -> {
                    if (data != null) {
                        agregado = true;
                        btn_favorite.setImageResource(R.drawable.ic_favorite);
                        btn_favorite.setColorFilter(ContextCompat.getColor(GameDetail.this, android.R.color.holo_red_light));
                    } else {
                        agregado = false;
                        btn_favorite.setImageResource(R.drawable.ic_favorite_border);
                        btn_favorite.setColorFilter(ContextCompat.getColor(GameDetail.this, android.R.color.white));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(GameDetail.this, "Error al verificar favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}