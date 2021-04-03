package com.kontoschris.homegardenapp02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPlant extends AppCompatActivity {

    EditText edtTitle;
    EditText edtDescription;
    Button btnCancel, btnSave;


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
                Plant plant = new Plant(sTitle, sDescription);
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