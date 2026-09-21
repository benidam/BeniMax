package com.abencar.benimax;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etContra;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etCorreo = findViewById(R.id.etCorreo);
        etContra = findViewById(R.id.etContra);
        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        Button btnLogearse = findViewById(R.id.btnValidarlog);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String correo = etCorreo.getText().toString();
                String contra = etContra.getText().toString();

                if(correo.isEmpty() || contra.isEmpty() ) {
                    Toast.makeText(LoginActivity.this,"Rellene los datos primero",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Éxito: el usuario ya se ha creado en la base de datos
                                Toast.makeText(LoginActivity.this, "¡Usuario registrado en BeniMax!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Fallo: la contraseña es muy corta, el correo está mal escrito, etc.
                                Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        btnLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString();
                String contra = etContra.getText().toString();

                if(correo.isEmpty() || contra.isEmpty() ) {
                    Toast.makeText(LoginActivity.this,"Rellene los datos primero",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(LoginActivity.this,task -> {

                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "¡Usuario logeado en BeniMax!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"¡No hay ninguna cuenta con esas crendciales, revisa la contraseña o registrate primero!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}