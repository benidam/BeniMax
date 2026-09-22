package com.abencar.benimax;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrearRutinaActivity extends AppCompatActivity {

    ArrayList<String> listaEjercicios = new ArrayList<>();

    ArrayList<String> listaEjSeleccionados = new ArrayList<>();

    // Declaramos el cajón vacío a nivel de clase para poder usarlo en cualquier parte
    LinearLayout contenedorEjercicios;
    private EditText etNombreRutina;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private String userID;
    private String nombreRecogido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);
        contenedorEjercicios = findViewById(R.id.contenedorEjercicios);
        db =  FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        etNombreRutina = findViewById(R.id.etNombreRutina);

        configurarBotonAnadir();
        configurarBotonGuardar();


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

                nombreRecogido = etNombreRutina.getText().toString();
                //comprobamos si el nombre esta vacio o sai la lista tde ej seleccionados
                // esta vacio para y le pedimos al usuario que porfavor rellene todos los campos antes de continuar

                if (nombreRecogido.isEmpty() || listaEjSeleccionados.isEmpty()) {
                    Toast.makeText(CrearRutinaActivity.this, "Tienes que rellenar todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> rutina = new HashMap<>(); // contenedor para luego subirla a firebase
                //guardamos el nombre de la rutina
                rutina.put("nombre",nombreRecogido);
                //añado la lista de ej seleccionados por el usuario
                rutina.put("ejercicios",listaEjSeleccionados);
                //guardamos la fecha exacta del servidor
                rutina.put("fecha",com.google.firebase.firestore.FieldValue.serverTimestamp());


                //creamos la ruta exacta donde se va almacenar la rutina que seria ( usuarios->UID->rutinas->rutina , esta última dejamos que firebase le genere un id aleatorio a cada rutina

                db.collection("usuarios").document(userID).collection("rutinas")
                                .add(rutina)
                                        .addOnSuccessListener(documentReference -> {
                                            //si sale bien avisamos y limpiamos la lista para poder añadir una nueva lista a posteriori
                                            Toast.makeText(CrearRutinaActivity.this, "Rutina guardada en tu perfil!",Toast.LENGTH_SHORT).show();

                                            listaEjSeleccionados.clear();
                                            contenedorEjercicios.removeAllViews();
                                            etNombreRutina.setText("");
                                        })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(CrearRutinaActivity.this, " Error al guardar: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                                                });

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