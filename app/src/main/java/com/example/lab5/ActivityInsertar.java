package com.example.lab5;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityInsertar extends AppCompatActivity {


    private final int inicio = 360,fin = 1410;
    public int iniInput = 0, finInput = 0;
    private boolean imageSelected=false;

    private ImageView imageView;

    private Calendar myCalendar= Calendar.getInstance();
    EditText inpFecha,inpHoraIni,inpHoraFin, inpTitulo, inpDescp;

    Uri imageUri;
    TextView messImg;
    Button messDisp;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private List<DateSaver> listaFechas = new ArrayList<>();
    private List<Button> labelDisponibilidad = new ArrayList<>();

    String idImageActividad;
    boolean disponible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Intent intent = getIntent();
        String data = intent.getStringExtra("id_list");

        if (data!= null) {
            ref.child("actividades/"+mAuth.getUid()+data).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Actividad actividad = snapshot.getValue(Actividad.class);
                    inpTitulo.setText(actividad.getTitulo().toString());
                    inpDescp.setText(actividad.getDescripcion().toString());
                    inpFecha.setText(actividad.getFecha().toString());
                    inpHoraIni.setText(actividad.getHoraInicio().toString());
                    inpHoraFin.setText(actividad.getHoraFin().toString());
                    Log.d("msg","se obtuvo la data" );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ActivityInsertar.this, "Error al validar data", Toast.LENGTH_SHORT).show();
                }
            });
        }


        labelDisponibilidad.add(findViewById(R.id.dispLabel));
        labelDisponibilidad.add(findViewById(R.id.dispLabelerror));
        labelDisponibilidad.add(findViewById(R.id.dispLabeldisp));

        ref.child("actividades/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idImageActividad = String.valueOf(snapshot.getChildrenCount()+1);
                for(DataSnapshot children : snapshot.getChildren()){
                    Actividad act = children.getValue(Actividad.class);
                    if(act!=null) {
                        listaFechas.add(new DateSaver(act.getTitulo(),act.getFecha(), act.getHoraInicio(), act.getHoraFin()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityInsertar.this, "Error al validar data", Toast.LENGTH_SHORT).show();
            }
        });

        iniInput =0 ; finInput =0;

        inpFecha = findViewById(R.id.inpFecha);
        inpHoraIni = findViewById(R.id.intHoraIni);
        inpHoraFin = findViewById(R.id.intHoraFin);
        imageView = findViewById(R.id.imgView);
        inpTitulo = findViewById(R.id.intTitulo);
        inpDescp = findViewById(R.id.intDesc);

        messImg = findViewById(R.id.textImg);

        messDisp = findViewById(R.id.dispLabel);



        inpHoraIni.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityInsertar.this, (timePicker, hourOfDay, minutes) -> {
                String amPm;
                int hour = hourOfDay;
                int minutesTime=hourOfDay*60+minutes;

                if(finInput!=0 && minutesTime>finInput){
                    Toast.makeText(this, "Conflicto con las horas", Toast.LENGTH_SHORT).show();

                } else if(minutesTime<inicio){
                    Toast.makeText(this, "Solo se agenda desde las 6:00 am", Toast.LENGTH_SHORT).show();

                } else if(minutesTime>fin){
                    Toast.makeText(this, "Solo se agenda hasta las 11:30 pm", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (hourOfDay >= 12) {
                        hour=hourOfDay-12;
                        amPm = " PM";
                    } else {
                        amPm = " AM";
                    }
                    iniInput = minutesTime;
                    inpHoraIni.setText(String.format("%02d:%02d", hour, minutes) + amPm);
                    evaluarDisponibilidad();
                }
            }, 0, 0, false);

            timePickerDialog.show();
        });

        inpHoraFin.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityInsertar.this, (timePicker, hourOfDay, minutes) -> {
                String amPm;
                int hour = hourOfDay;
                int minutesTime=hourOfDay*60+minutes;

                if(iniInput!=0 && minutesTime<iniInput){
                    Toast.makeText(this, "Conflicto con las horas", Toast.LENGTH_SHORT).show();
                }
                else if((minutesTime)<inicio){
                    Toast.makeText(this, "Solo se agenda desde las 6:00 am", Toast.LENGTH_SHORT).show();

                } else if((minutesTime)>fin){
                    Toast.makeText(this, "Solo se agenda hasta las 11:30 pm", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (hourOfDay >= 12) {
                        hour=hourOfDay-12;
                        amPm = " PM";
                    } else {
                        amPm = " AM";
                    }

                    finInput = minutesTime;
                    inpHoraFin.setText(String.format("%02d:%02d", hour, minutes) + amPm);
                    evaluarDisponibilidad();
                }

            }, 0, 0, false);

            timePickerDialog.show();
        });

        //Date picker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
            evaluarDisponibilidad();
        };

        inpFecha.setOnClickListener(view -> {
            DatePickerDialog datepicker = new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datepicker.getDatePicker().setMinDate(new Date().getTime());
            datepicker.show();
        });



    }



    public void evaluarDisponibilidad(){
        String fecha = inpFecha.getText().toString();
        String horaInicio = inpHoraIni.getText().toString();
        String horaFin = inpHoraFin.getText().toString();

        if(fecha.isEmpty() || horaFin.isEmpty() || horaInicio.isEmpty()){
            disponible = false;
            return;
        }

        List<String> listaConflictos = new ArrayList<>();


        Calendar calTemp = Calendar.getInstance();

        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        String currentDate = dateFormat.format(calTemp.getTime());

        if(currentDate.equals(fecha)){
            int tempMinutes =  calTemp.get(Calendar.HOUR_OF_DAY)*60 + calTemp.get(Calendar.MINUTE);
            int start = DateSaver.minutesInDay(horaInicio);
            if(start<tempMinutes){
                disponible = false;

                labelDisponibilidad.get(0).setVisibility(View.INVISIBLE);
                labelDisponibilidad.get(1).setVisibility(View.VISIBLE);
                labelDisponibilidad.get(2).setVisibility(View.INVISIBLE);

                Toast.makeText(this, "No se puede agendar el pasado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for(DateSaver i : listaFechas){
            if(i.getFecha().equals(fecha) && i.conflict(horaInicio, horaFin)){
                disponible = false;
                listaConflictos.add(i.getTitulo());

                labelDisponibilidad.get(0).setVisibility(View.INVISIBLE);
                labelDisponibilidad.get(1).setVisibility(View.VISIBLE);
                labelDisponibilidad.get(2).setVisibility(View.INVISIBLE);
            }
        }


        if(!listaConflictos.isEmpty()){
            StringBuilder conflictos= new StringBuilder();
            for(String i: listaConflictos){
                conflictos.append(i+"\n");
            }


            // Create the object of AlertDialog Builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the message show for the Alert time
            builder.setMessage(conflictos.toString());

            // Set Alert Title
            builder.setTitle("Se encontró conflictos con las siguientes actividades");

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("De acuerdo", (dialog, which) -> {
                // When the user click yes button then app will close
                dialog.cancel();

            });

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("", (dialog, which) -> {
                // If user click no then dialog box is canceled.
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
            return;
        }



        disponible = true;
        labelDisponibilidad.get(0).setVisibility(View.INVISIBLE);
        labelDisponibilidad.get(1).setVisibility(View.INVISIBLE);
        labelDisponibilidad.get(2).setVisibility(View.VISIBLE);

    }


    public void subir(View view){
        String titulo = inpTitulo.getText().toString().trim();
        String descripcion = inpDescp.getText().toString().trim();
        String fecha = inpFecha.getText().toString().trim();
        String hInicio = inpHoraIni.getText().toString().trim();
        String hFin = inpHoraFin.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hInicio.isEmpty() || hFin.isEmpty() || !imageSelected ) {
            Toast.makeText(this, "Llene todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!disponible){
            Toast.makeText(this, "No hay disponibilidad", Toast.LENGTH_SHORT).show();
            return;
        }

        Actividad actividad = new Actividad(
                titulo, descripcion, fecha,
                hInicio, hFin);

        actividad.setImageName(idImageActividad);

        ref.child("actividades/"+mAuth.getUid()+"/").push().setValue(actividad).addOnSuccessListener(aVoid ->{
                    //Toast.makeText(this, "Exitoso", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e ->{
                    //Toast.makeText(this, "Fallo", Toast.LENGTH_SHORT).show();
                });


        Toast.makeText(this, "Subiendo", Toast.LENGTH_SHORT).show();
        storageReference.child("img/"+mAuth.getUid()+"/"+actividad.getImageName()).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(this, "Exitoso", Toast.LENGTH_SHORT).show();
                    finish();

                }).addOnFailureListener( e ->{
                    Toast.makeText(this, "Fallo", Toast.LENGTH_SHORT).show();

                });


    }


    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        inpFecha.setText(dateFormat.format(myCalendar.getTime()));
    }


    public void subirImagen(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> galleryLauncher
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                            imageView.setImageBitmap(
                                    selectedImageBitmap);

                            messImg.setVisibility(View.GONE);
                            imageUri = selectedImageUri;
                            imageSelected= true;
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

}