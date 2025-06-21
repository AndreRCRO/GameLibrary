package com.example.gamelibrary.data.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.Juego;

public class GameDetail extends AppCompatActivity {
    private ImageView iv_game_image;
    private TextView tv_game_year, tv_game_platforms, tv_metacritic_score, tv_game_description;
    private ImageButton btn_back, btn_favorite;
    private boolean isFavorite = false;

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

        Juego juego = (Juego) getIntent().getSerializableExtra("juego");

        Glide.with(this)
                .load(juego.getImagenUrl())
                .into(iv_game_image);
        tv_game_year.setText(String.valueOf(juego.getAnio_lanzamiento()));
        tv_game_platforms.setText(String.join(", ", juego.getPlataformas()));
        tv_metacritic_score.setText(String.valueOf(juego.getMetacritic()));
        tv_game_description.setText(juego.getDescripcion());

        btn_back.setOnClickListener(view -> finish());

        btn_favorite.setOnClickListener(v -> toggleFavorite());
    }

    //Metodo para poeder cambiar el estado del favorito (corazon)
    private void toggleFavorite() {
        isFavorite = !isFavorite;

        if (isFavorite) {
            // Corazon relleno
            btn_favorite.setImageResource(R.drawable.ic_favorite);
            btn_favorite.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_light));
        } else {
            // marco vacio del cora
            btn_favorite.setImageResource(R.drawable.ic_favorite_border);
            btn_favorite.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }

        //Animacion que podria dar problemas pero se peude cambiar xd
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
}