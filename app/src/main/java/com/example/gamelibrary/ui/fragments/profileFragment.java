package com.example.gamelibrary.ui.fragments;

import android.content.Intent;
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

public class profileFragment extends Fragment {

    TextView tv_username;
    Button btn_desconectar;
    usuario user = usuario.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        tv_username = view.findViewById(R.id.tv_username);
        btn_desconectar = view.findViewById(R.id.btn_desconectar);

        tv_username.setText(user.getUsername()); // Mostrar usuario

        btn_desconectar.setOnClickListener(v -> {
            user.clear(); // Limpiar singleton

            // Crear intent para MainActivity
            Intent intent = new Intent(requireContext(), Login.class);

            // Limpiar la pila de actividades y comenzar de nuevo
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        });

        return view;
    }
}
