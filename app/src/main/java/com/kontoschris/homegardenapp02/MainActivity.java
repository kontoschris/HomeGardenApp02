package com.kontoschris.homegardenapp02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    TextView tvLatAndLon;
    ImageButton imgbtAdd; //Κουμπί ADD
    ImageButton imgbtOptinons; //Κουμπί Options
    Button btGetGPS;
    ArrayList<Plant> plants; //array με φυτα
    RecyclerView recyclerView; //Recycler με λιστα φυτων
    PlantAdapter plantAdapter; //Adapter (διαχειριστής) Φυτών
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;
    // Initializing other items
    // from layout file
    int PERMISSION_ID = 44;

    //Στο Create του Activity......
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatAndLon = findViewById(R.id.txt_Gps);
        btGetGPS = findViewById(R.id.bt_getGPS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


        btGetGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Getting GPS", Toast.LENGTH_SHORT).show();


//                latitudeTextView = findViewById(R.id.latTextView);
//                longitTextView = findViewById(R.id.lonTextView);



                // method to get the location
                getLastLocation();
            }
        });


        //Τοποθετω το κουμπί Add κάτω δεξιά...
        imgbtAdd = findViewById(R.id.img_Add);

        //Κώδικας για Click στο κουπί ADD...
        imgbtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.plant_input, null, false);

                EditText edtTitle = viewInput.findViewById(R.id.edf_Title);
                EditText edtDescription = viewInput.findViewById(R.id.edf_Description);

                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PlantDetails2.class);

                startActivityForResult(intent, 1);





            }
        });


        //Βρίσκω τον Recycler με τα φυτα...
        recyclerView = findViewById(R.id.recyclerOfPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        //Κώδικα Swipe & Move του Recycler
        //Επεπεξεργασία κατάχωρησης - Swipe καταχωρησης
        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            //Move
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //Swipe
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int plantId = plants.get(viewHolder.getAdapterPosition()).getId();
                new PlantHandler(MainActivity.this).delete(plantId);
                //remove from array
                plants.remove(viewHolder.getAdapterPosition());
                //notify for the removal
                plantAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        //Initialize touch helper & Attach to the recycler
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadPlants();
    }

    public ArrayList<Plant> readPlants (){
        ArrayList<Plant> plants = new PlantHandler(this).readPlants();
        return plants;
    }

    public void loadPlants(){
        plants = readPlants();

        plantAdapter = new PlantAdapter(plants, this, new PlantAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                int nPosition = plants.get(position).getId();
                Toast.makeText(MainActivity.this, ""+nPosition, Toast.LENGTH_SHORT).show();
                editPlant(nPosition, view );
            }
        });
        recyclerView.setAdapter(plantAdapter);
    }

    private void editPlant(int plantId, View view){
        PlantHandler plantHandler = new PlantHandler(this  );

        Plant plant = plantHandler.readOnePlant(plantId);

        Intent intent = new Intent(this, EditPlant.class);

        intent.putExtra("title", plant.getTitle());
        intent.putExtra("description", plant.getDescription());
        //intent.putExtra("img", plant.getImage());
        intent.putExtra("id", plant.getId());

        startActivityForResult(intent, 1); //για να δουμε αν πατήσαμε το back
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //loadPlants();
        if (requestCode == 1) { //δηλαδη επιστρέψαμε απο την δραστηρθότητα
            loadPlants();
        }
    }

    //////////////////////////////
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            tvLatAndLon.setText("Lat: " + location.getLatitude() + " / Long: " + location.getLongitude());
//                            latitudeTextView.setText(location.getLatitude() + "");
//                            longitTextView.setText(location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            tvLatAndLon.setText("Lat: " + mLastLocation.getLatitude() + " / Long: " + mLastLocation.getLongitude());
//            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


}