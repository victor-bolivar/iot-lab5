package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class ListarActivity extends AppCompatActivity {

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    StorageReference storageReference = storage.getReference();
    StorageReference fileRef = storageReference.child("files/uid");
    //private DatabaseReference mDatabase;
    public void listarAcrchivos(View view){
        fileRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int cantidadElementos = listResult.getItems().size();
                Log.d("infoApp", String.valueOf(cantidadElementos));
                for (StorageReference item : listResult.getItems()){
                    Log.d("InfoAppArchivo",item.getName());
                }
            }
        });
    }


}