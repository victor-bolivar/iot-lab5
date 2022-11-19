package com.example.lab5;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityInsertar extends AppCompatActivity {


    private final int inicio = 360;
    private final int fin = 1410;


    private ImageView imageView;

    final Calendar myCalendar= Calendar.getInstance();
    EditText inpFecha;
    EditText inpHoraIni;
    EditText inpHoraFin;
    TextView messImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        inpFecha = findViewById(R.id.inpFecha);
        inpHoraIni = findViewById(R.id.intHoraIni);
        inpHoraFin = findViewById(R.id.intHoraFin);
        imageView = findViewById(R.id.imgView);
        messImg = findViewById(R.id.textImg);

        inpHoraIni.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityInsertar.this, (timePicker, hourOfDay, minutes) -> {
                String amPm;
                int hour = hourOfDay;
                if (hourOfDay >= 12) {
                    hour=hourOfDay-12;
                    amPm = " PM";
                } else {
                    amPm = " AM";
                }

                if((hourOfDay*60+minutes)<inicio){
                    Toast.makeText(this, "Solo se agenda desde las 6:00 am", Toast.LENGTH_SHORT).show();

                } else if((hourOfDay*60+minutes)>fin){
                    Toast.makeText(this, "Solo se agenda hasta las 11:30 pm", Toast.LENGTH_SHORT).show();
                }
                else{
                    inpHoraIni.setText(String.format("%02d:%02d", hour, minutes) + amPm);
                }
            }, 0, 0, false);

            timePickerDialog.show();
        });

        inpHoraFin.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityInsertar.this, (timePicker, hourOfDay, minutes) -> {
                String amPm;
                int hour = hourOfDay;
                if (hourOfDay >= 12) {
                    hour=hourOfDay-12;
                    amPm = " PM";
                } else {
                    amPm = " AM";
                }
                if((hourOfDay*60+minutes)<inicio){
                    Toast.makeText(this, "Solo se agenda desde las 6:00 am", Toast.LENGTH_SHORT).show();

                } else if((hourOfDay*60+minutes)>fin){
                    Toast.makeText(this, "Solo se agenda hasta las 11:30 pm", Toast.LENGTH_SHORT).show();
                }
                else{
                    inpHoraFin.setText(String.format("%02d:%02d", hour, minutes) + amPm);
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
        };

        inpFecha.setOnClickListener(view -> {
            DatePickerDialog datepicker = new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datepicker.getDatePicker().setMinDate(new Date().getTime());
            datepicker.show();
        });



    }


    private void updateLabel(){
        String myFormat="MM/dd/yyyy";
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
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

}