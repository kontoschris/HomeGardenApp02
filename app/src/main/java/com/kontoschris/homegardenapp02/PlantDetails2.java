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


    EditText edtTitle;
    EditText edtDescription;
    EditText edtTemp;
    EditText edtHum;

    Button btnCancel, btnSave, btnChangeImg;
    ImageView img;
    byte byteImage[];
    Bitmap imageFromCamera = null;

    public static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_IMAGE_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details2);

        //Intent intent = getIntent();
        edtDescription = findViewById(R.id.edtDescription);
        edtTitle = findViewById(R.id.edtTitle);
        edtTemp = findViewById(R.id.edtTemp);
        edtHum = findViewById(R.id.edtHum);
        img = findViewById(R.id.imgCaptured);
        //Toast.makeText(this, "" + intent.getStringExtra("title"), Toast.LENGTH_SHORT).show();
        //edtTitle.setText(intent.getStringExtra("title"));
        //edtDescription.setText(intent.getStringExtra("description"));

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImg = findViewById(R.id.btnChangeImage);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();
                String temperature =  edtTemp.getText().toString();
                String humidity =  edtHum.getText().toString();

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


                if (title.trim().equals("") ||  description.trim().equals("")) {
                    Toast.makeText(PlantDetails2.this, "You have to add Title and Description of the Plant. Plant not saved!!!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (imageFromCamera == null){
                        Toast.makeText(PlantDetails2.this, "You have to select an image from camera. Plant not saved!!!", Toast.LENGTH_LONG).show();
                    } else
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageFromCamera.compress(Bitmap.CompressFormat.PNG, 0, stream);
                        byteImage = stream.toByteArray();


                        Plant plant = new Plant(title, description, nTemperature, nHumidity, byteImage);

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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        String[] perms = {Manifest.permission.CAMERA};
                        requestPermissions(perms, CAMERA_PERMISSION_CODE);
                    } else {
                        takePicture();
                    }

                } else {
                    takePicture();
                }
            }
        });


    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_CODE){
            if (resultCode == RESULT_OK){

                //Bitmap takeImage = (Bitmap) data.getExtras().get("data");
                imageFromCamera = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(imageFromCamera);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

    }
    private void takePicture(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE);

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}