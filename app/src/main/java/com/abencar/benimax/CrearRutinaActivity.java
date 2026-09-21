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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CrearRutinaActivity extends AppCompatActivity {

    ArrayList<String> listaEjercicios = new ArrayList<>();

    ArrayList<String> listaEjSeleccionados = new ArrayList<>();

    // Declaramos el cajón vacío a nivel de clase para poder usarlo en cualquier parte
    LinearLayout contenedorEjercicios;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);
        contenedorEjercicios = findViewById(R.id.contenedorEjercicios);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        configurarBotonAnadir();
        configurarBotonGuardar();

        db =  FirebaseFirestore.getInstance();

        db.collection("catalogo_ejercicios")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot documentos : task.getResult()){
                            String nombreEjercicio = documentos.getString("nombre");
                            listaEjercicios.add(nombreEjercicio);
                        }

                    }else {
                        Toast.makeText(CrearRutinaActivity.this,"Error al cargar el catálogo", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void configurarBotonGuardar() {
        Button btnGuardarEjercicio = findViewById(R.id.btnGuardarRutina);
        btnGuardarEjercicio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){






                Toast.makeText(CrearRutinaActivity.this, "RUTINA GUARDADA CON ÉXITO, A DARLE DURO", Toast.LENGTH_SHORT).show();
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

        //converti el arrayl a array normal para sacarlo por pantalla

        String[] arrayDialogo = listaEjercicios.toArray(new String[0]);

        builder.setItems(arrayDialogo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ejercicioSeleccionado = listaEjercicios.get(which);
                listaEjSeleccionados.add(ejercicioSeleccionado);

                TextView nuevoTextoEjercicio = new TextView(CrearRutinaActivity.this);
                nuevoTextoEjercicio.setText("• " + ejercicioSeleccionado);
                nuevoTextoEjercicio.setTextSize(18);
                nuevoTextoEjercicio.setPadding(0, 16, 0, 16);
                contenedorEjercicios.addView(nuevoTextoEjercicio);
            }
        });

        builder.show();
    }
}