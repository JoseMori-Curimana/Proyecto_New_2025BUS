package com.example.proyecto_new_2025bus;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Asistencia> historial;
    AsistenciaAdapter adapter;
    DatabaseReference ref;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.recyclerHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historial = new ArrayList<>();
        adapter = new AsistenciaAdapter(historial);
        recyclerView.setAdapter(adapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("asistencias");

        ref.orderByChild("usuarioId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historial.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Asistencia asistencia = ds.getValue(Asistencia.class);
                    historial.add(asistencia);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistorialActivity.this, "Error al cargar historial", Toast.LENGTH_SHORT).show();
            }
        });
    }
}