package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab5.Entity.Actividad;
import com.example.lab5.Entity.DateSaver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActualizarActivity extends AppCompatActivity{

    ActivityInsertar insertar = new ActivityInsertar();

    EditText inpTitulo, inpDescrip, inpFecha, inpInicio, inpFin;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        Intent intent = getIntent();
        String data = intent.getStringExtra("id_list");

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        inpTitulo = findViewById(R.id.editTextTitulo);
        inpDescrip = findViewById(R.id.editTextDescripcion);
        inpFecha = findViewById(R.id.editTextFecha);
        inpInicio = findViewById(R.id.editTextInicio);
        inpFin = findViewById(R.id.editTextFin);


        ref.child("actividades/"+mAuth.getUid()+data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Actividad actividad = snapshot.getValue(Actividad.class);
                inpTitulo.setText(actividad.getTitulo().toString());
                inpDescrip.setText(actividad.getDescripcion().toString());
                inpFecha.setText(actividad.getFecha().toString());
                inpInicio.setText(actividad.getHoraInicio().toString());
                inpFin.setText(actividad.getHoraFin().toString());
                Log.d("msg","Actidad : " );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActualizarActivity.this, "Error al validar data", Toast.LENGTH_SHORT).show();
            }
        });


        



    }


    public void actualizarData(View v){


        insertar.subir(v);

    }





}