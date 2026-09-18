package com.abencar.benimax;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class EntrenamientoActivity extends AppCompatActivity {

    private EditText etPeso, etReps, etRir;
    private TextView tvEjercicioActual, tvTemporizador;
    private int numeroSerie = 1;
    private CountDownTimer temporizadorDescanso;

    private long tiempoRestanteMilisegundos = 0;

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
    }

    private void masTiempo() {
        if (temporizadorDescanso != null) {
            temporizadorDescanso.cancel();
        }

        tiempoRestanteMilisegundos = tiempoRestanteMilisegundos + 30000;
        iniciarDescanso();
    }


    private void guardarSerie () {
            String pesoStr = etPeso.getText().toString();
            String repsStr = etReps.getText().toString();
            String rirStr = etRir.getText().toString();

            if (pesoStr.isEmpty() || repsStr.isEmpty() || rirStr.isEmpty()) {
                Toast.makeText(this, "Rellena todos los datos para guardar la serie", Toast.LENGTH_SHORT).show();
                return;
            }

            String mensaje = "Serie " + numeroSerie + " guardada: " + pesoStr + "kg x " + repsStr + " (RIR " + rirStr + ")";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            numeroSerie++;
            etReps.setText("");
            etRir.setText("");
            etReps.requestFocus();

            // Iniciamos el descanso justo después de guardar la serie
            tiempoRestanteMilisegundos=120000; // VOLVEMOS A RESTABLECER EL CONTADOR A 2 MINS , ANTES DE EMPEZAR UNA SERIE NUEVA
            iniciarDescanso();
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
                    tvTemporizador.setText("¡A DALE CAÑA!");
                    Toast.makeText(EntrenamientoActivity.this, "¡Fin del descanso! Toca faenar.", Toast.LENGTH_LONG).show();
                }
            }.start();
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            if (temporizadorDescanso != null) {
                temporizadorDescanso.cancel();
            }
        }
    }
