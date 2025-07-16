package com.example.proyecto_new_2025bus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Notificacion extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificacionAdapter adapter;
    private List<NotificacionModel> listaNotificaciones = new ArrayList<>();
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        recyclerView = findViewById(R.id.recyclerViewNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificacionAdapter(listaNotificaciones);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        db.collection("notificacion")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaNotificaciones.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        NotificacionModel noti = doc.toObject(NotificacionModel.class);
                        listaNotificaciones.add(noti);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
                });
    }
}