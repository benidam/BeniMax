package com.abencar.benimax;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MisRutinasActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;

    // 1. El cajón visual donde meteremos los botones
    private LinearLayout contenedorRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_rutinas); // Asegúrate de que el XML se llame así

        // 2. Enlazamos el cajón
        contenedorRutinas = findViewById(R.id.contenedorRutinas);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userID).collection("rutinas")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot documento : task.getResult()){
                            // Extraemos los datos de la caja
                            String nombreRutina = documento.getString("nombre");
                            String idRutina = documento.getId();
                            ArrayList<String> ejercicios = (ArrayList<String>) documento.get("ejercicios");

                            // 3. Fabricamos un botón nuevo para esta rutina
                            Button btnRutina = new Button(MisRutinasActivity.this);
                            btnRutina.setText(nombreRutina);
                            btnRutina.setTextSize(18);
                            // Un poco de margen para que no estén pegados
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 0, 16);
                            btnRutina.setLayoutParams(params);

                            // 4. El puente: ¿Qué pasa al pulsar el botón?
                            btnRutina.setOnClickListener(v -> {
                                if (ejercicios != null && !ejercicios.isEmpty()) {
                                    Intent intent = new Intent(MisRutinasActivity.this, EntrenamientoActivity.class);

                                    // Le mandamos el ID de la rutina para que sepa dónde guardar las series
                                    intent.putExtra("CLAVE_ID_RUTINA", idRutina);

                                    // Para arrancar, le mandamos el PRIMER ejercicio de la lista (posición 0)
                                    intent.putExtra("CLAVE_EJ", ejercicios.get(0));

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MisRutinasActivity.this, "Esta rutina está vacía", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // 5. Inyectamos el botón en la pantalla
                            contenedorRutinas.addView(btnRutina);
                        }

                    } else {
                        Toast.makeText(MisRutinasActivity.this, "Error al cargar tus rutinas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}