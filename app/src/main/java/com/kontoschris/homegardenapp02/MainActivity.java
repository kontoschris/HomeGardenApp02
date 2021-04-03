package com.kontoschris.homegardenapp02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.Inet4Address;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Plant> plants;
    RecyclerView recyclerView;
    PlantAdapter plantAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.img_Add);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.plant_input, null, false);

                EditText edtTitle = viewInput.findViewById(R.id.edf_Title);
                EditText edtDescription = viewInput.findViewById(R.id.edf_Description);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add Plant")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = edtTitle.getText().toString();
                                String description = edtDescription.getText().toString();

                                Plant plant = new Plant(title, description);

                                boolean isInserted = new PlantHandler(MainActivity.this).create(plant);
                                if (isInserted){
                                    Toast.makeText(MainActivity.this, "Plant Saved", Toast.LENGTH_SHORT).show();
                                    loadPlants();
                                } else {
                                    Toast.makeText(MainActivity.this, "Plant NOT Saved", Toast.LENGTH_SHORT).show();
                                }
                                //dialogInterface.cancel();
                            }
                        }).show();

            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Επεπεξεργασία κατάχωρησης - Swipe καταχωρησης
        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

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
        intent.putExtra("id", plant.getId());

        startActivityForResult(intent, 1); //για να δουμε αν πατήσαμε το back
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { //δηλαδη επιστρέψαμε απο την δραστηρθότητα
            loadPlants();
        }
    }
}