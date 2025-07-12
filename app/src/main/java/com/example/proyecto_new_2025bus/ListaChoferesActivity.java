package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaChoferesActivity extends AppCompatActivity {

    private RecyclerView recyclerChoferes;
    private ChoferAsignacionAdapter adapter;
    private final List<chofer> listaChoferes = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_choferes);

        recyclerChoferes = findViewById(R.id.recyclerChoferes);
        recyclerChoferes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChoferAsignacionAdapter(listaChoferes, this);
        recyclerChoferes.setAdapter(adapter);

        cargarChoferesDesdeFirestore();
    }

    private void cargarChoferesDesdeFirestore() {
        db.collection("usuarios")
                .whereEqualTo("rol", "chofer")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaChoferes.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String id = doc.getId();
                        String nombre = doc.getString("nombre");
                        String dni = doc.getString("dni");
                        String telefono = doc.getString("telefono");

                        chofer chofer = new chofer(id, nombre, dni, telefono);
                        listaChoferes.add(chofer);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar choferes", Toast.LENGTH_SHORT).show());
    }
}

