package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamelibrary.R;
import com.example.gamelibrary.data.activities.Login;
import com.example.gamelibrary.data.modelos.usuario;
import com.google.android.material.checkbox.MaterialCheckBox;

public class profileFragment extends Fragment {

    private TextView tv_username;
    private Button btn_desconectar;
    usuario user = usuario.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        tv_username = view.findViewById(R.id.tv_username);
        btn_desconectar = view.findViewById(R.id.btn_desconectar);

        tv_username.setText(user.getUsername()); // Mostrar usuario

        btn_desconectar.setOnClickListener(v -> {
            // Limpiar singleton
            user.clear();

            // Borrar SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("mi_app_prefs", requireContext().MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Crear intent para Login (no MainActivity)
            Intent intent = new Intent(requireContext(), Login.class);

            // Limpiar pila de actividades para que no se pueda volver atrás
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        });

        return view;
    }
}
