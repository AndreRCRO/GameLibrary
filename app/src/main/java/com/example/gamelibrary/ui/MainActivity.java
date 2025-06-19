package com.example.gamelibrary.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamelibrary.R;
import com.example.gamelibrary.ui.fragments.BibliotecaFragment;
import com.example.gamelibrary.ui.fragments.BuscarFragment;
import com.example.gamelibrary.ui.fragments.CollectionsFragment;
import com.example.gamelibrary.ui.fragments.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_biblioteca) {
                selectedFragment = new BibliotecaFragment();
            } else if (itemId == R.id.nav_buscar) {
                selectedFragment = new BuscarFragment();
            } else if (itemId == R.id.nav_favoritos) {
                selectedFragment = new CollectionsFragment();
            } else if (itemId == R.id.nav_perfil) {
                selectedFragment = new profileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            } else {
                return false;
            }
        });

        // Load the default fragment (Biblioteca)
        if (savedInstanceState == null) {
            loadFragment(new BibliotecaFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_biblioteca);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}