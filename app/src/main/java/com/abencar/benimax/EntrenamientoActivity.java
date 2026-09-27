package com.abencar.benimax;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EntrenamientoActivity extends AppCompatActivity {

    private EditText etPeso, etReps, etRir;
    private TextView tvEjercicioActual, tvTemporizador;
    private int numeroSerie = 1;
    private CountDownTimer temporizadorDescanso;

    private FirebaseFirestore db;
    private long tiempoRestanteMilisegundos = 0;

    private String nombreEjercicioActual;

    private FirebaseAuth mAuth;

    private String userID;

    private String idRutinaActual;

    private ArrayList<String> listaEjerciciosRutina;
    private int indiceEjercicioActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);

        tvEjercicioActual = findViewById(R.id.tvEjercicioActual);
        tvTemporizador = findViewById(R.id.tvTemporizador);
        etPeso = findViewById(R.id.etPeso);
        etReps = findViewById(R.id.etReps);
        etRir = findViewById(R.id.etRir);
        Button btnGuardarSerie = findViewById(R.id.btnGuardarSerie);
        Button btnMasTiempo = findViewById(R.id.btnMasTiempo);
        Button btnSiguienteEjercicio = findViewById(R.id.btnSiguienteEjercicio);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        Intent intenRecibido = this.getIntent();
        idRutinaActual = intenRecibido.getStringExtra("CLAVE_ID_RUTINA");
        listaEjerciciosRutina = intenRecibido.getStringArrayListExtra("CLAVE_LISTA_EJ");

            // Comprobamos que la lista ha llegado bien y no está vacía
        if (listaEjerciciosRutina != null && !listaEjerciciosRutina.isEmpty()) {
            nombreEjercicioActual = listaEjerciciosRutina.get(indiceEjercicioActual);
            tvEjercicioActual.setText(nombreEjercicioActual);
        }

        btnGuardarSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSerie();
            }
        });

        btnMasTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masTiempo();
            }
        });

        btnSiguienteEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteEjercicio();
            }
        });
    }

    private void masTiempo() {
        if (temporizadorDescanso != null) {
            temporizadorDescanso.cancel();
        }

        tiempoRestanteMilisegundos = tiempoRestanteMilisegundos + 30000;
        iniciarDescanso();
    }


    private void guardarSerie () {
            Map<String,Object> serie = new HashMap<>();
            String pesoStr = etPeso.getText().toString().replace(",", ".");
            String repsStr = etReps.getText().toString();
            String rirStr = etRir.getText().toString();

            if (pesoStr.isEmpty() || repsStr.isEmpty() || rirStr.isEmpty()) {
                Toast.makeText(this, "Rellena todos los datos para guardar la serie", Toast.LENGTH_SHORT).show();
                return;
            }

            String mensaje = "Serie " + numeroSerie + " guardada: " + pesoStr + "kg x " + repsStr + " (RIR " + rirStr + ")";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            serie.put("ejercicio", nombreEjercicioActual);
            serie.put("Serie", numeroSerie);
            serie.put("peso", Double.parseDouble(pesoStr));
            serie.put("reps", Integer.parseInt(repsStr));
            serie.put("rir", Integer.parseInt(rirStr));

            db.collection("usuarios").document(userID).collection("rutinas").document(idRutinaActual).collection("series").add(serie)
                    .addOnSuccessListener(documentReference -> {

                numeroSerie++;
                etReps.setText("");
                etRir.setText("");
                etReps.requestFocus();

                // Iniciamos el descanso justo después de guardar la serie
                tiempoRestanteMilisegundos=120000; // VOLVEMOS A RESTABLECER EL CONTADOR A 2 MINS , ANTES DE EMPEZAR UNA SERIE NUEVA
                iniciarDescanso();

            }).addOnFailureListener(e -> {
                Toast.makeText(EntrenamientoActivity.this,"Error de conexión: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }

        private void iniciarDescanso () {
            if (temporizadorDescanso != null) {
                temporizadorDescanso.cancel();
            }

            tvTemporizador.setVisibility(View.VISIBLE);

            temporizadorDescanso = new CountDownTimer(tiempoRestanteMilisegundos, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    tiempoRestanteMilisegundos = millisUntilFinished;

                    int minutos = (int) (millisUntilFinished / 1000) / 60;
                    int segundos = (int) (millisUntilFinished / 1000) % 60;

                    String tiempoFormateado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
                    tvTemporizador.setText(tiempoFormateado);
                }

                @Override
                public void onFinish() {
                    tvTemporizador.setText("¡A DARLE CAÑA!");
                    Toast.makeText(EntrenamientoActivity.this, "¡Fin del descanso! Toca faenar.", Toast.LENGTH_LONG).show();
                }
            }.start();
        }

    private void siguienteEjercicio() {
        indiceEjercicioActual++; // Sumamos 1 para pasar a la siguiente posición

        // Comprobamos si aún quedan ejercicios en la lista
        if (indiceEjercicioActual < listaEjerciciosRutina.size()) {
            // Actualizamos el nombre en la variable y en la pantalla
            nombreEjercicioActual = listaEjerciciosRutina.get(indiceEjercicioActual);
            tvEjercicioActual.setText(nombreEjercicioActual);

            // Reiniciamos la serie a 1 y limpiamos los campos
            numeroSerie = 1;
            etPeso.setText("");
            etReps.setText("");
            etRir.setText("");

            Toast.makeText(this, "Pasamos a: " + nombreEjercicioActual, Toast.LENGTH_SHORT).show();
        } else {
            // Ya no hay más ejercicios en el array
            Toast.makeText(this, "¡Rutina Completada! Eres una bestia.", Toast.LENGTH_LONG).show();
            finish(); // Cierra la pantalla de entrenamiento y vuelve a Mis Rutinas
        }
    }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            if (temporizadorDescanso != null) {
                temporizadorDescanso.cancel();
            }
        }
    }
