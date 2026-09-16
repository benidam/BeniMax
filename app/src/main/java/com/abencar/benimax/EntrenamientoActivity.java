package com.abencar.benimax;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EntrenamientoActivity extends AppCompatActivity {

    private EditText etPeso, etReps, etRir;
    private TextView tvEjercicioActual;
    private int numeroSerie = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);
        tvEjercicioActual= findViewById(R.id.tvEjercicioActual);
        etPeso = findViewById(R.id.etPeso);
        etReps = findViewById(R.id.etReps);
        etRir = findViewById(R.id.etRir);
        Button btnGuardarSerie = findViewById(R.id.btnGuardarSerie);

        btnGuardarSerie.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                guardarSerie();
            }
        });
    }

    private void guardarSerie() {
        String pesoStr = etPeso.getText().toString();
        String repStr = etReps.getText().toString();
        String rirStr = etRir.getText().toString();

        if ( pesoStr.isEmpty() || repStr.isEmpty() || rirStr.isEmpty()){
            Toast.makeText(this, "Rellena todos los datos para guardar la serie , que no se te olvide o vas a estar estancado de por vida", Toast.LENGTH_SHORT).show();
            return;
        }

        String mensaje = "Serie " + numeroSerie + " guardada: " + pesoStr + "kg x " + rirStr;
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

        numeroSerie++;

        etReps.setText("");
        etRir.setText("");

        etReps.requestFocus();
    }
}