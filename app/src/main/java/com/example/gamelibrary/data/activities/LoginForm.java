package com.example.gamelibrary.data.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginForm extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btn_create_account;
    private MaterialCheckBox cb_remember;

    RequestQueue requestQueue;
    final String LOGIN_URL = "http://10.0.2.2:5001/api/usuarios/login";
    //sharpreference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_form);

        edtUsername = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btn_create_account = findViewById(R.id.btn_create_account);
        cb_remember = findViewById(R.id.cb_remember);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(view -> loginUser());
        btn_create_account.setOnClickListener(view -> {
            Intent intent = new Intent(LoginForm.this, RegisterForm.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("contrasena", password); // <-- asegúrate que tu API lo llama "contrasena"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                requestBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject outerData = response.getJSONObject("data");
                            JSONObject backupData = outerData.getJSONObject("data");

                            // Obtener instancia Singleton
                            usuario user = usuario.getInstance();

                            // Setear datos del usuario
                            user.setUsername(outerData.getString("username"));
                            user.setId(outerData.optInt("id", 0));

                            if(cb_remember.isChecked()) {
                                SharedPreferences prefs = getSharedPreferences("mi_app_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                try {
                                    editor.putBoolean("is_logged_in", true);
                                    editor.putString("user_username", outerData.getString("username"));
                                    editor.putInt("user_id", outerData.getInt("id"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.apply();
                            }

                            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
                            com.google.gson.JsonObject gsonData = parser.parse(backupData.toString()).getAsJsonObject();
                            user.setData(gsonData);

                            Toast.makeText(this, "Bienvenido, " + user.getUsername(), Toast.LENGTH_LONG).show();
                            user.restaurarBackupDesdeData(this);
                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
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
