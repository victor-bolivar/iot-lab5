package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // to hide title bar (la vaina que esta arriba que dice el nombre de la app "PrestoPucp")
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // SE VERIFICA QUE SI ESTA LOGUEADO, SE CIERRA ESTA ACTIVIDAD
        if (mAuth.getCurrentUser() != null){
            Log.d("msg / usuario logeado", mAuth.getCurrentUser().getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity( intent );
        }

    }

    public void iniciarSesion(View view){

        EditText editText_email = findViewById(R.id.login_editTextTextEmailAddress);
        EditText editText_password = findViewById(R.id.login_editTextTextPassword);

        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        // se verifica que no hayan valores vacios
        if (    email.isEmpty() ||
                password.isEmpty() ) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // sino, se autentica al usuario
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // se regresa a la actrividad principal
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity( intent );

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d( "msg / InicioSesion", String.valueOf(task.getException()));

                        }
                    }
                });

    }
}