package com.kontoschris.homegardenapp02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class PlantDetails extends AppCompatActivity {

    ImageView img;
    TextView txt;
    Button bt_ChangeImage;

    public static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_IMAGE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);


//        FirebaseDatabase.getInstance().getReference("somedata").setValue("test");
//
//        img = findViewById(R.id.imgCircled);
//        txt = findViewById(R.id.plant);
//        bt_ChangeImage = findViewById(R.id.btChangeImage);

        bt_ChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                        String[] perms = {Manifest.permission.CAMERA};
//                        requestPermissions(perms, CAMERA_PERMISSION_CODE);
//                    } else {
//                        takePicture();
//                    }
//
//                } else {
//                    takePicture();
//                }
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message");
//                myRef.setValue("Hello, World!");
            }
        });



        Intent intent = getIntent();
//        //img.setImageResource(intent.getIntExtra("image", R.drawable.f1));
//        txt.setText(intent.getStringExtra("name"));
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
////        if (requestCode == CAMERA_PERMISSION_CODE){
////            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
////                takePicture();
////            } else {
////                Toast.makeText(this, "You have to give permission to Camera", Toast.LENGTH_SHORT).show();
////            }
////
////        }
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == CAMERA_IMAGE_CODE){
////            if (resultCode == RESULT_OK){
////
////                Bitmap takeImage = (Bitmap) data.getExtras().get("data");
////                img.setImageBitmap(takeImage);
////                img.setScaleType(ImageView.ScaleType.FIT_START);
////            }
////        }
//
//    }

//    private void takePicture(){
////        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE);
//
//    }
}