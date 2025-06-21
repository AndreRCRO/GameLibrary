package com.example.gamelibrary.data.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.usuario;
import com.example.gamelibrary.ui.MainActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("mi_app_prefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (isLoggedIn) {
                // Recuperar datos guardados
                String username = prefs.getString("user_username", null);
                int id = prefs.getInt("user_id", 0);

                if (username != null && id != 0) {
                    // Inicializar singleton con datos
                    usuario user = usuario.getInstance();
                    user.setUsername(username);
                    user.setId(id);

                    // Saltar a MainActivity
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                    return;
                }
            }

            // Si no está logueado o datos inválidos, abrir Login
            startActivity(new Intent(Splash.this, LoginForm.class));
            finish();

        }, 2500);
    }
}
