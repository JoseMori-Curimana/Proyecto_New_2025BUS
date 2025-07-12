package com.example.proyecto_new_2025bus;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoferAsignacionAdapter extends RecyclerView.Adapter<ChoferAsignacionAdapter.ViewHolder> {

    private final List<chofer> choferList;
    private final Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String[] rutas = {"Callería", "Yarinacocha"};
    private final String[] turnos = {"Mañana", "Tarde", "Noche"};
    private final String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};

    public ChoferAsignacionAdapter(List<chofer> choferList, Context context) {
        this.choferList = choferList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chofer_asignacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        chofer chofer = choferList.get(position);

        holder.tvNombre.setText("Nombre: " + chofer.getNombre());
        holder.tvDni.setText("DNI: " + chofer.getDni());
        holder.tvTelefono.setText("Teléfono: " + chofer.getTelefono());

        ArrayAdapter<String> adapterRuta = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, rutas);
        adapterRuta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spRuta.setAdapter(adapterRuta);

        ArrayAdapter<String> adapterTurno = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, turnos);
        adapterTurno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spTurno.setAdapter(adapterTurno);

        ArrayAdapter<String> adapterDia = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dias);
        adapterDia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spDia.setAdapter(adapterDia);

        holder.btnAsignar.setOnClickListener(v -> {
            String ruta = holder.spRuta.getSelectedItem().toString();
            String turno = holder.spTurno.getSelectedItem().toString();
            String dia = holder.spDia.getSelectedItem().toString();

            // Validación previa
            db.collection("asignaciones")
                    .whereEqualTo("choferId", chofer.getId())
                    .whereEqualTo("turno", turno)
                    .whereEqualTo("dia", dia)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        if (!snapshot.isEmpty()) {
                            Toast.makeText(context, "Este chofer ya tiene una asignación ese día en el mismo turno", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> data = new HashMap<>();
                            data.put("choferId", chofer.getId());
                            data.put("nombreChofer", chofer.getNombre());
                            data.put("ruta", ruta);
                            data.put("turno", turno);
                            data.put("dia", dia);

                            db.collection("asignaciones")
                                    .add(data)
                                    .addOnSuccessListener(doc -> Toast.makeText(context, "Asignación registrada", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error al asignar", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al validar asignación", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return choferList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDni, tvTelefono;
        Spinner spRuta, spTurno, spDia;
        Button btnAsignar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreChofer);
            tvDni = itemView.findViewById(R.id.tvDniChofer);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoChofer);
            btnAsignar = itemView.findViewById(R.id.btnAsignarRuta);
        }

    }
}
