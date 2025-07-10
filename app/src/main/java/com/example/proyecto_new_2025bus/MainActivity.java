package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_new_2025bus.R;
import com.example.proyecto_new_2025bus.chofer;
import com.example.proyecto_new_2025bus.estudiante;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChofer = findViewById(R.id.btnChofer);
        Button btnEstudiante = findViewById(R.id.btnEstudiante);

        btnChofer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, chofer.class);
            startActivity(intent);
        });

        btnEstudiante.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, estudiante.class);
            startActivity(intent);
        });
    }
}
