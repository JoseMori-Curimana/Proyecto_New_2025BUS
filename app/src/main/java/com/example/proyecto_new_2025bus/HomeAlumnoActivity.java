package com.example.proyecto_new_2025bus;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeAlumnoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationAlumno;
    private String dniAlumno;
    private String nombreAlumno;
    private String codigo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_alumno);
        createNotificationChannel();

        // Insets para ajustar la UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAlumno), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener datos del intent
        dniAlumno = getIntent().getStringExtra("dni-usuario");
        nombreAlumno = getIntent().getStringExtra("nombre_usuario");
        codigo = getIntent().getStringExtra("codigo");

        if (dniAlumno == null) {
            dniAlumno = "";
            Toast.makeText(this, "DNI no recibido", Toast.LENGTH_SHORT).show();
        }

        // Notificaciones desde Firestore
        db.collection("notificacion")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Firestore", "Escucha fallida", error);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                DocumentSnapshot doc = dc.getDocument();
                                String tipo = doc.getString("tipo");
                                String mensaje = doc.getString("mensaje");

                                mostrarNotificacion(tipo, mensaje);
                            }
                        }
                    }
                });

        // Bottom Navigation
        bottomNavigationAlumno = findViewById(R.id.bottomNavigationAlumno);
        bottomNavigationAlumno.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_rutas) {
                // Aquí puedes poner startActivity si tienes VerRutasActivity
                return true;
            } else if (id == R.id.nav_ubicacion) {
                startActivity(new Intent(this, estudiante.class));
                return true;
            } else if (id == R.id.nav_asientos) {
                Intent intent = new Intent(this, AsistenciaAlumnoActivity.class);
                intent.putExtra("dni-alumno", dniAlumno);
                intent.putExtra("nombre-alumno", nombreAlumno);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_perfil_alumno) {
                Intent intent = new Intent(this, PerfilAlumnoActivity.class);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_notificaciones) {
                startActivity(new Intent(this, Notificacion.class));
                return true;
            }

            return false;
        });
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void mostrarNotificacion(String titulo, String contenido) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mi_canal_id")
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mi_canal_id",
                    "Canal de Notificaciones",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Muestra notificaciones sobre buses");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
