package com.kontoschris.homegardenapp02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText edtPwd;
    EditText edtUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPwd = findViewById(R.id.edtPwd);
        edtUsr = findViewById(R.id.edtUsr);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message2");

        //myRef.setValue("Hello, World!");
    }

    public void openMainFormActivity(View view){
        Intent intent = new Intent(LoginActivity.this, MainActivity .class);

        String sUsr = edtUsr.getText().toString();
        //intent.putExtra("username", edtUsr.getText().toString());
        //intent.putExtra("password", edtPwd.getText().toString());
        startActivity(intent);


    }




}