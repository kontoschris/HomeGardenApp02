//PlantDetails2
//Η φόρμα η οποία διαχειρίζεται τα Properties του Plant Και υλοποιεί τις λειτουργίες
//CRUD Καθώς και την λήψη φωτογραφίας

package com.kontoschris.homegardenapp02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class PlantDetails2 extends AppCompatActivity {
    //δηλώνω κουμπικά & edit boxes του GUI
    EditText edtTitle;
    EditText edtDescription;
    EditText edtTemp;
    EditText edtHum;

    Button btnCancel, btnSave, btnChangeImg;
    ImageView img;
    byte byteImage[];
    Bitmap imageFromCamera = null;

    //Camera permission codes
    public static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_IMAGE_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details2);

        //περνουμε τα δεδομένα απο το MainActivity
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        int globalTemp = extras.getInt("globaltemp");
        int globalHum = extras.getInt("globalhum");


        //συνδέουμε τα αντικειμενα του GUI Με τις μεταβλητες
        edtDescription = findViewById(R.id.edtDescription);
        edtTitle = findViewById(R.id.edtTitle);
        edtTemp = findViewById(R.id.edtTemp);
        edtHum = findViewById(R.id.edtHum);
        img = findViewById(R.id.imgCaptured);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImg = findViewById(R.id.btnChangeImage);

        //βάζουμε την θερμογρασία και την υγρασία της πόλης στα αντίστοιχα πεδία του plant
        edtTemp.setText(""+globalTemp);
        edtHum.setText(""+globalHum);


        //Ενέργειες στο click του SAVE (κατά την αποθήκευση)
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();
                String temperature =  edtTemp.getText().toString();
                String humidity =  edtHum.getText().toString();

                //φτιάχνουμε την θερμοκρασία και την υγρασία
                int nTemperature;
                int nHumidity;
                if (temperature.trim().equals("")) {
                    nTemperature = 0;
                } else {
                    nTemperature = Integer.parseInt(temperature);
                }
                if (humidity.trim().equals("")) {
                    nHumidity = 0;
                } else {
                    nHumidity = Integer.parseInt(humidity);
                }

                //αν δεν καταχωρήσει ο χρήστης τιτλο, θερμοκρασία ή και δεν τραβήκει φωτογραφία τότε δεν αποθηκέυεται το φυτό
                if (title.trim().equals("") ||  description.trim().equals("")) {
                    Toast.makeText(PlantDetails2.this, "You have to add Title and Description of the Plant. Plant not saved!!!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (imageFromCamera == null){
                        Toast.makeText(PlantDetails2.this, "You have to select an image from camera. Plant not saved!!!", Toast.LENGTH_LONG).show();
                    } else
                    {
                        //αν όλα είναι οκ... τοτε δημιουργείται το Plant object
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageFromCamera.compress(Bitmap.CompressFormat.PNG, 0, stream);
                        byteImage = stream.toByteArray();
                        Plant plant = new Plant(title, description, nTemperature, nHumidity, byteImage);

                        //δημιουργείται νεος handler για το Object προκειμένου να αποθηκευτεί στην βάση
                        boolean isInserted = new PlantHandler(PlantDetails2.this).create(plant);
                        if (isInserted){
                            Toast.makeText(PlantDetails2.this, "Plant Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PlantDetails2.this, "Plant NOT Saved", Toast.LENGTH_SHORT).show();
                        }
                        onBackPressed(); // τριγκάρω το back press button on Android
                    }
                }
            }
        });

        //κώδικας για το Cancel click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Κλωδικας για την λήψη φωτογραφίας
        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ελέγχω το SDK version του κινητού να είναι μεγαλύτερη του Marshmallow προκειμένου να ζητήσω δικαιώματα γιατι
                //δεν δίνονται μόνο κατά το Manifest
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //M is for Marshmallow!  Applications targeting this or a later release will get these new changes in behavior.
                    //έλεγχος δικαιωμάτων κάμερας
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        String[] perms = {Manifest.permission.CAMERA};
                        requestPermissions(perms, CAMERA_PERMISSION_CODE);
                    } else {
                        //αν έχω πάρει δικαιώματα απο τον χρήστη τράβα φωτο
                        takePicture();
                    }
                } else {
                    takePicture();
                }
            }
        });
    }

    //Κατά την ζήτηση διακιωμάτων κάμερας απο τον χρήστη
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            } else {
                Toast.makeText(this, "You have to give permission to Camera", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override //στην περίπτωση που επιστεέψουμε απο την διαδικασία λήψης φωτογραφίας
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //αν το resultcode = 1
        if (requestCode == CAMERA_IMAGE_CODE){
            //και έχει γίνει με επιτυχία η λήψη φωτογραφίας....
            if (resultCode == RESULT_OK){
                //έχουμε παρει και τοποθετήσει την εικόνα από την κάμερα και την τοποθετούμε
                imageFromCamera = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(imageFromCamera);
                img.setScaleType(ImageView.ScaleType.FIT_XY); //φτιάχνουμε το scale της φωτογραφίας
            }
        }

    }

    //βασικές εντολές λήψης φωτογραφίας
    private void takePicture(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE);

    }

    //μετατροπή Bitmap σε ByteArray (χρειάζεται για την αποθήκευση τη φωτογραφίας
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}