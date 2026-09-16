package com.abencar.benimax;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CrearRutinaActivity extends AppCompatActivity {

    String[] listaEjercicios = {
            "Press Banca",
            "Sentadilla",
            "Dominadas",
            "Desafiar a la ratona",
            "Curl de Bíceps",
            "Press Militar"
    };

    // Declaramos el cajón vacío a nivel de clase para poder usarlo en cualquier parte
    LinearLayout contenedorEjercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        // 1. Encontramos nuestro cajón vacío en el XML
        contenedorEjercicios = findViewById(R.id.contenedorEjercicios);

        configurarBotonAnadir();
        configurarBotonGuardar();


    }

    private void configurarBotonGuardar() {
        Button btnGuardarEjercicio = findViewById(R.id.btnGuardarRutina);
        btnGuardarEjercicio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(CrearRutinaActivity.this, "RUTINA GUARDADA CON ÉXITO, A REVENTAR TODAS LAS PERRAS", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void configurarBotonAnadir() {
        Button btnAnadirEjercicio = findViewById(R.id.btnAnadirEjercicio);
        btnAnadirEjercicio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mostrarDialogoEjercicios();
            }
        });
    }

    private void mostrarDialogoEjercicios() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un ejercicio");

        builder.setItems(listaEjercicios, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ejercicioSeleccionado = listaEjercicios[which];

                // 2. LA MAGIA: Creamos un texto nuevo desde cero en Java
                TextView nuevoTextoEjercicio = new TextView(CrearRutinaActivity.this);
                nuevoTextoEjercicio.setText("• " + ejercicioSeleccionado);
                nuevoTextoEjercicio.setTextSize(18);
                nuevoTextoEjercicio.setPadding(0, 16, 0, 16); // Le damos un poco de aire

                // Lo metemos dentro del cajón
                contenedorEjercicios.addView(nuevoTextoEjercicio);
            }
        });

        builder.show();
    }
}