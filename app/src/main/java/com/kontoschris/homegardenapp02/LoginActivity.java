//LoginActivity --> activity_login.xml
// Οθόνη σύνδεσης
package com.kontoschris.homegardenapp02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    EditText edtPwd;
    EditText edtUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPwd = findViewById(R.id.edtPwd);
        edtUsr = findViewById(R.id.edtUsr);
    }

    public void openMainFormActivity(View view){
        //ετοιμάζω την σύνδεση με την κύρια οθόνη MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity .class);

        String sUsr = edtUsr.getText().toString();
        String sPwd = edtPwd.getText().toString();

        //ελέγχω αν το usr & pwd = admin
        if (sUsr.equals("admin") && sPwd.equals("admin")) {
            intent.putExtra("username", edtUsr.getText().toString());
            intent.putExtra("password", edtPwd.getText().toString());

            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            edtUsr.setText("");
            edtPwd.setText("");
        }
    }
}