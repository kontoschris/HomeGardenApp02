//MainActivity --> activity_main.xml
//Κωδικας κύριας οθόνης της εφαρμογής

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
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

//Imports for GPS
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tvLatAndLon; //text view GPS Data
    private TextView tvWeather;
    private EditText edtCity;
    ImageButton imgbtAdd; //add button
    ImageButton imgbtRefresh; //add button
    Button btGetGPS;
    Button btGetweather;
    ArrayList<Plant> plants; //array με plants
    RecyclerView recyclerView; //Recycler με λιστα φυτων
    PlantAdapter plantAdapter; //Adapter (διαχειριστής) Φυτών

    //θερμογρασία και υγρασία τρέχουσας πόλης
    int globalTemperature;
    int globalHumidity;


    // initializing FusedLocationProviderClient object (για την αναζήτη της θέσης με χρήση δικτύου και GPS
    // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44; //GPS permissions

    //Object κληση στο OpenWeather API
    WeatherAPIcall weatherAPIcall;

    // Create tou Activity //////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //πέρνουμε και συδέουμε τα αντικείμενα του GUI
        tvLatAndLon = findViewById(R.id.txtGPSdata);
        tvWeather = findViewById(R.id.txtWeather);
        btGetGPS = findViewById(R.id.btGetGPS);
        btGetweather = findViewById(R.id.btGetWeather);
        edtCity = findViewById(R.id.edtCity);

        //check GPS permsisions  kai get GPS Data
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


        //Φτιάχνω RetroFit url το οποίο θα κάνει το http CALL στο OpenWeather
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherServerInfo.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //και δημιουργουμε το αντικείμενο του CALL
        weatherAPIcall = retrofit.create(WeatherAPIcall.class);



        //Button for getting gps (manual from the UI) ----------------------------------------------
        btGetGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Getting GPS", Toast.LENGTH_SHORT).show();

                getLastLocation(); //Get Location
            }
        });//---------------------------------------------------------------------------------------


        //Get Weather button code ------------------------------------------------------------------
        btGetweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCity;
                sCity = edtCity.getText().toString();
                Toast.makeText(MainActivity.this, "Getting Weather Data for City: " + sCity, Toast.LENGTH_SHORT).show();
                getWeather(sCity); //συνάρτηση που βρίσκει τον καιρό

            }
        }); //--------------------------------------------------------------------------------------


        //get Button ADD
        imgbtAdd = findViewById(R.id.img_Add);

        //κώδικας για προσθήκη φυτού .... Add Plant click coding -----------------------------------
        imgbtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                                MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                Toast.makeText(MainActivity.this, "Starting ADD new plant screen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PlantDetails2.class);

                //περναω στην φόρμα τις τρέχουσες τιμές θερμοκρασίας και υγρασίας
                intent.putExtra("globaltemp", globalTemperature);
                intent.putExtra("globalhum", globalHumidity);

                //ξεκινάω την φόρμα PlantDetails2 gia tin prosthiki toy plant
                startActivityForResult(intent, 1);
            }
        });//---------------------------------------------------------------------------------------



        //get Button Refresh
        imgbtRefresh = findViewById(R.id.img_Refresh);

        //κώδικας για το refresh της λίστας των φυτών -------- -----------------------------------
        imgbtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                //αν δεν έχουμε πατήσει πρώτα το κουμπί του καιρού τότε ενημέρωσε χρήστη
                if (globalTemperature > 0) {
                    Toast.makeText(MainActivity.this, "Checking which plants need water", Toast.LENGTH_SHORT).show();
                    loadPlants(); //φόρτωσε την λίστα με τα φυτα....
                } else {
                    Toast.makeText(MainActivity.this, "You have to press button <GET WEATHER> first!", Toast.LENGTH_LONG).show();
                }

            }
        });//---------------------------------------------------------------------------------------


        //Εδώ ορίζουμε τον RecyclerView Που περιέχει τα Plants
        //Recycler για τα Plants https://developer.android.com/guide/topics/ui/layout/recyclerview?gclid=CjwKCAjw07qDBhBxEiwA6pPbHuTgu3RaY5T6L6RpDWeTteq8f_23Hm7d_Kg2inIk7lbu-PaNkVezHBoCVfcQAvD_BwE&gclsrc=aw.ds
        recyclerView = findViewById(R.id.recyclerOfPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        //Code for Swipe & Move tou Recycler
        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            //Το Move δεν υλοποιήθηκε
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //αν το κάνουμε Swipe δεξια ή αριστερά τότε διαγράφει το Plant
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int plantId = plants.get(viewHolder.getAdapterPosition()).getId();
                new PlantHandler(MainActivity.this).delete(plantId);
                //διαγραφή από το Arrat
                plants.remove(viewHolder.getAdapterPosition());
                //ενημέρωση το Plant Adapter οτι διαγράφηκε
                plantAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        //Initialize touch helper & προσθήκη στο recycler
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadPlants();//κάθε φόρα που φορτώνει το recycler --> refresh plants
    }


    //ΔΗμιουργία Array Με Plants κατά το φόρτωμά τους
    public ArrayList<Plant> readPlants (){//////////////////////////////////////////////////////////
        ArrayList<Plant> plants = new PlantHandler(this).readPlants();
        Toast.makeText(MainActivity.this, "Loading Plants", Toast.LENGTH_SHORT).show();
        return plants;
    }


    //Κώδικας που φορτώνει απο τα Plants απο το Array Στον Recycler
    public void loadPlants(){ //////////////////////////////////////////////////////////////////////

        plants = readPlants();

        plantAdapter = new PlantAdapter(plants,globalTemperature,this, new PlantAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                int nPosition = plants.get(position).getId();
                Toast.makeText(MainActivity.this, "Loading Plants...."+nPosition, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(plantAdapter); //βάζει το Recycler ton adapter toy Plant
    }


    @Override //οταν επιστρεψουμε απο το PlantDetails Activity τότε φόρτωσε τα Plants παλι
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //////
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { //δηλαδη επιστρέψαμε απο την δραστηρθότητα
            loadPlants();
        }
    }


    //Κώδικας που βρίσκει τον καιρό για την πόλη sCity
    private void getWeather(String sCity){
        //Αντικείμενο του CALL
        Call<WeatherData> myCall = weatherAPIcall.getWeatherData(sCity, WeatherServerInfo.API_KEY);
        myCall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                WeatherData weatherData = response.body();
                String sTemp, sTempFeels, sHum;

                Toast.makeText(MainActivity.this, "Weather is retrieved.", Toast.LENGTH_SHORT).show();

                //Βρίσκω την θερμοκρασία και υγρασία και την βαζω σε μεταβλητές για να την περάσω στην φόρμα PlantDetails2
                globalTemperature = (int) (weatherData.getWeatherDataMain().getGetTemperature() - 273.15); //μετατροπή σε C και σε ακέραιο
                globalHumidity = (int) (weatherData.getWeatherDataMain().getGetHumidity() - 0);  //μετρατροπή σε ακέραιο

                //Επίσης όπω τα παραπάνω αλλα τωρα τα κανω String για να τα εμφανίζω στο GUI (textview)
                sTemp = "" + (weatherData.getWeatherDataMain().getGetTemperature() - 273.15);
                sTemp = sTemp.substring(0, 2);
                sTempFeels = "" +  (weatherData.getWeatherDataMain().getGetFeelsLike() - 273.15);
                sTempFeels = sTempFeels.substring(0,2);
                sHum = "" + weatherData.getWeatherDataMain().getGetHumidity();
                sHum = sHum.substring(0,2);

                //τα εμφανίζω.....
                tvWeather.setText("Current Temperature: " + sTemp +  " C (Feels like: " + sTempFeels + " C) - Humidity: " + sHum +
                        "% - City: " + sCity);
            }

            @Override //αν αποτυχει το CALL .... μηνυμα στον Χρήστη
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail getting Weather Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // GPS Code from website: https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
    // περισσότερα το Word doc της εργασίας .............
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last location from FusedLocationClient object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            //Set Location Data to Text View
                            tvLatAndLon.setText("Lat: " + location.getLatitude() + " <> Long: " + location.getLongitude());
                            Toast.makeText(MainActivity.this, "Found GPS location", Toast.LENGTH_SHORT).show();
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

        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest  on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            //Set Location Data to Text View
            tvLatAndLon.setText("Lat: " + mLastLocation.getLatitude() + " <> Long: " + mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        // If we want background location on Android 10.0 and higher, use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check if location is enabled
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
///////////////////////////////////////////////////////////////////////// GPS CODE /////////////////

}