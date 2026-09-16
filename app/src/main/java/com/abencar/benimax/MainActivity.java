package com.abencar.benimax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnCrearRutina = findViewById(R.id.btnCrearRutina);
        Button btnComenzarEntreno = findViewById(R.id.btnComenzarEntreno);


        btnCrearRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CrearRutinaActivity.class);
                startActivity(intent);
            }
        });

        btnComenzarEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "¡A reventar esos hierros!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EntrenamientoActivity.class);
                startActivity(intent);
            }
        });
    }
}