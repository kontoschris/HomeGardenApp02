package com.kontoschris.homegardenapp02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class EditPlant extends AppCompatActivity {

    EditText edtTitle;
    EditText edtDescription;
    Button btnCancel, btnSave;
    Drawable drawable;
    Bitmap bitmap1, bitmap2;
    byte[] image;
    ByteArrayOutputStream bytearrayoutputstream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        Intent intent = getIntent();
        edtDescription = findViewById(R.id.edt_Description);
        edtTitle = findViewById(R.id.edt_Title);

        Toast.makeText(this, "" + intent.getStringExtra("title"), Toast.LENGTH_SHORT).show();
        edtTitle.setText(intent.getStringExtra("title"));
        edtDescription.setText(intent.getStringExtra("description"));


        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTitle = edtTitle.getText().toString();
                String sDescription = edtDescription.getText().toString();
                int nIDfromIntent = intent.getIntExtra("id", 1);
//                String sTitle = edtTitle.getText().toString();
//                String sTitle = edtTitle.getText().toString();

                drawable = getResources().getDrawable(R.drawable.defaultplant);
                bitmap1 = ((BitmapDrawable)drawable).getBitmap();
                bytearrayoutputstream = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG,70,bytearrayoutputstream);

                image = bytearrayoutputstream.toByteArray();
                Plant plant = new Plant(sTitle, sDescription,1,1,image);
                plant.setId(nIDfromIntent);
                if (new PlantHandler(EditPlant.this).update(plant)){
                    Toast.makeText(EditPlant.this, "Plant data updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditPlant.this, "Update FAILED!!!!", Toast.LENGTH_SHORT).show();
                }

                onBackPressed(); // τριγκάρω το back press button on Android

            }
        });


    }
}