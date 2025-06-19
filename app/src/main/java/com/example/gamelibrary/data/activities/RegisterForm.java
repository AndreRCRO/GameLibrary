package com.example.gamelibrary.data.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamelibrary.R;

public class RegisterForm extends AppCompatActivity {

    private static final String TAG = "RegisterFormActivity";

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


    }
}