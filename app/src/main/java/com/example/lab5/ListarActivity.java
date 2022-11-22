package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lab5.Entity.Actividad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    ArrayList<Actividad> listarActividades = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        mAuth= FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.Recycler);

        ListaAdapter adapter = new ListaAdapter();
        adapter.setContenido(ListarActivity.this);
        firebaseDatabase = firebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("actividades/" + mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    Actividad actividad = children.getValue(Actividad.class);
                    listarActividades.add(actividad);
                    adapter.setListarActividades(listarActividades);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListarActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void IrImagen(View view){
        Intent intent = new Intent(this, Imagen_Activity.class);
        startActivity(intent);
    }




}