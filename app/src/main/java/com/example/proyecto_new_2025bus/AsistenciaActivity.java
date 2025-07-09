package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AsistenciaActivity extends AppCompatActivity {
    Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

//        btnConfirmar = findViewById(R.id.btnConfirmarAsistencia);
//        btnConfirmar.setOnClickListener(v -> {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//
//            Map<String, Object> asistencia = new HashMap<>();
//            asistencia.put("usuarioId", userId);
//            asistencia.put("fecha", fecha);
//            asistencia.put("busId", "bus001");
//
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("asistencias");
//            ref.push().setValue(asistencia).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(this, "Asistencia confirmada", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Error al confirmar", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
    }
}
