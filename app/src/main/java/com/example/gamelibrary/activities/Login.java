package com.example.gamelibrary.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamelibrary.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Delay before starting animations
        new Handler(Looper.getMainLooper()).postDelayed(this::startAnimations, 300); // 300ms delay

        // Set up login button to navigate to MainActivity
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, LoginForm.class);
            startActivity(intent);
            finish();
        });
    }

    private void startAnimations() {
        try {
            Animator logoAnim = AnimatorInflater.loadAnimator(this, R.animator.fade_in_animation);
            logoAnim.setTarget(findViewById(R.id.main_container));
            logoAnim.start();

            Animator textAnim = AnimatorInflater.loadAnimator(this, R.animator.fade_in_delayed);
            textAnim.setTarget(findViewById(R.id.text_container));
            textAnim.start();

            Animator buttonsAnim = AnimatorInflater.loadAnimator(this, R.animator.fade_in_buttons);
            buttonsAnim.setTarget(findViewById(R.id.buttons_container));
            buttonsAnim.start();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    Animator pulseAnim = AnimatorInflater.loadAnimator(this, R.animator.pulse_animation);
                    pulseAnim.setTarget(findViewById(R.id.pulse_indicator));
                    pulseAnim.start();

                    // 5. Rotation animation for decorations
                    Animator rotationAnim = AnimatorInflater.loadAnimator(this, R.animator.rotation_animation);
                    rotationAnim.setTarget(findViewById(R.id.rotating_decoration));
                    rotationAnim.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}