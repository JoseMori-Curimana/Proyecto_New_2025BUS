package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilAlumnoActivity extends AppCompatActivity {

    private TextView tvNombre, tvDni, tvCodigo, tvTelefono, tvCorreo;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_alumno);  // usa tu layout

        tvNombre = findViewById(R.id.tvNombreAlumno);
        tvDni = findViewById(R.id.tvDniAlumno);
        tvCodigo = findViewById(R.id.tvCodigoAlumno);
        tvTelefono = findViewById(R.id.tvTelefonoAlumno);
        tvCorreo = findViewById(R.id.tvCorreoAlumno);

        db = FirebaseFirestore.getInstance();

        String codigo = getIntent().getStringExtra("codigo");

        if (codigo != null && !codigo.isEmpty()) {
            cargarDatosAlumno(codigo);
        } else {
            Toast.makeText(this, "No se recibió el código del alumno", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarDatosAlumno(String codigo) {
        db.collection("usuarios")
                .whereEqualTo("codigo", codigo)
                .whereEqualTo("rol", "alumno")
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);

                        tvNombre.setText("" + doc.getString("nombre"));
                        tvDni.setText("" + doc.getString("dni"));
                        tvCodigo.setText("" + doc.getString("codigo"));
                        tvTelefono.setText("" + doc.getString("telefono"));
                        tvCorreo.setText("" + doc.getString("correo"));
                    } else {
                        Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
