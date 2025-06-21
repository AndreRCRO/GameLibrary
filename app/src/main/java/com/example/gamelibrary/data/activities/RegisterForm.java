package com.example.gamelibrary.data.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.usuario;
import com.example.gamelibrary.ui.MainActivity;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterForm extends AppCompatActivity {

    private static final String TAG = "RegisterFormActivity";
    private Button btn_back_to_login, btn_register;
    private EditText et_username, et_password, et_confirm_password;
    private RequestQueue requestQueue;
    private final String LOGIN_URL = "http://10.0.2.2:5001/api/usuarios"; // Para emulador

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        View mainView = findViewById(R.id.register_container);

        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, windowInsets) -> {
                Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
                return WindowInsetsCompat.CONSUMED;
            });
        }
        btn_back_to_login = findViewById(R.id.btn_back_to_login);
        btn_register = findViewById(R.id.btn_register);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        requestQueue = Volley.newRequestQueue(this);

        btn_register.setOnClickListener(view -> registerUser());
        btn_back_to_login.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginForm.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = et_username.getText().toString().trim();
        if (!et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = et_password.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || et_confirm_password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("contrasena", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                requestBody,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");

                        usuario user = usuario.getInstance();

                        user.setUsername(data.getString("username"));
                        user.setId(data.optInt("id", 0));

                        SharedPreferences prefs = getSharedPreferences("mi_app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        try {
                            editor.putBoolean("is_logged_in", true);
                            editor.putString("user_username", data.getString("username"));
                            editor.putInt("user_id", data.getInt("id"));
                            // Si tienes token, guardalo aquí también
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editor.apply();

                        // Convertir JSONObject a JsonObject de Gson
                        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
                        com.google.gson.JsonObject gsonData = parser.parse(data.toString()).getAsJsonObject();
                        user.setData(gsonData);

                        Toast.makeText(this, "Bienvenido, " + user.getUsername(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al leer respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(request);
    }
}