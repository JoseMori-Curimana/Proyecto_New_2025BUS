package com.example.rutas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class chofer extends AppCompatActivity implements OnMapReadyCallback {

    private Spinner spinnerRuta, spinnerViaje;
    private Button btnIniciar;
    private TextView tvEstado;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean enviandoUbicacion = false;
    private DatabaseReference refFirebase;
    private final int REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer);

        spinnerRuta = findViewById(R.id.spinnerRuta);
        spinnerViaje = findViewById(R.id.spinnerViaje);
        btnIniciar = findViewById(R.id.btnIniciar);
        tvEstado = findViewById(R.id.tvEstado);

        ArrayAdapter<String> rutasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Yarinacocha", "San Fernando"});
        rutasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuta.setAdapter(rutasAdapter);

        ArrayAdapter<String> viajesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"1er viaje", "2do viaje", "3er viaje", "4to viaje"});
        viajesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerViaje.setAdapter(viajesAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapChofer);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        refFirebase = FirebaseDatabase.getInstance().getReference("rutas");

        btnIniciar.setOnClickListener(v -> {
            enviandoUbicacion = !enviandoUbicacion;
            if (enviandoUbicacion) {
                btnIniciar.setText("FINALIZAR RUTA");
                iniciarEnvioUbicacion();
                tvEstado.setText("En ruta desde las " + obtenerHoraActual());
            } else {
                btnIniciar.setText("INICIAR RUTA");
                detenerUbicacion();
                tvEstado.setText("Ruta detenida");
            }
        });
    }

    private void iniciarEnvioUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        LocationRequest request = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000 // cada 5 segundos
        ).setMinUpdateIntervalMillis(3000).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    String ruta = spinnerRuta.getSelectedItem().toString().toLowerCase().replace(" ", "_");
                    String viaje = spinnerViaje.getSelectedItem().toString().toLowerCase().replace(" ", "_");
                    DatabaseReference rutaRef = refFirebase.child(ruta).child(viaje);

                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("lat", lat);
                    datos.put("lng", lng);
                    datos.put("ultimo_update", ServerValue.TIMESTAMP);
                    datos.put("hora", obtenerHoraActual());
                    datos.put("activo", true);
                    datos.put("nombre", "Chofer Demo");

                    rutaRef.setValue(datos);

                    if (mMap != null) {
                        LatLng coordenadas = new LatLng(lat, lng);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(coordenadas).title("Tu posición"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 15));
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(request, locationCallback, getMainLooper());
    }

    private void detenerUbicacion() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

        String ruta = spinnerRuta.getSelectedItem().toString().toLowerCase().replace(" ", "_");
        String viaje = spinnerViaje.getSelectedItem().toString().toLowerCase().replace(" ", "_");
        refFirebase.child(ruta).child(viaje).child("activo").setValue(false);
    }

    private String obtenerHoraActual() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pucallpa = new LatLng(-8.3791, -74.5539); // Vista inicial
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pucallpa, 14));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == REQUEST_LOCATION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarEnvioUbicacion();
        }
    }
}
