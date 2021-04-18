//Προσαρμογέας για την κλάσση plant την οποία θα εφανίζεται στον recyclerView
package com.kontoschris.homegardenapp02;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantHolder> {

    ArrayList<Plant> plants; //array me ta plants
    int globalTemp; //η άρχική θερμογρασία που έχει η τρέχουσα πολη
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;

    //Properties toy Adapter
    public PlantAdapter(ArrayList<Plant> arrayList, int temp, Context context, ItemClicked itemClicked){
        plants = arrayList;
        this.globalTemp = temp;
        this.context = context;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override //δημιουργία του adapter
    public PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.plant_holder, parent, false);//οριζσουμε το layout
        this.parent = parent;
        return new PlantHolder(view);
    }

    @Override //δένουμε τον adapter με το αντίστοιχο layout το οποίο θα  εμφανίζει το κάθε plant
    public void onBindViewHolder(@NonNull PlantHolder holder, int position) {
        holder.title.setText(plants.get(position).getTitle()); ///titlos
        holder.description.setText(plants.get(position).getDescription());//perigrafh
        int temp = plants.get(position).getTemperature();//θερμοκρσαία
        int hum = plants.get(position).getHumidity();//υγρασία


        //αν η θερμογρασία του φυτού είναι μεγαλύτερη απο την θερμογρασια της περιοχής (πόλης) τότε εμφάνισε μήνυμα
        // ότι χρειάζεται πότισμα
        if (temp > globalTemp && globalTemp > 0) {
            holder.temperature.setText(plants.get(position).getTemperature() + " (WATER PLEASE)"); //χρειαζεται πότισμα
        } else {
        holder.temperature.setText("" + plants.get(position).getTemperature());
        }


        holder.humidity.setText("" + plants.get(position).getHumidity());

        //βάζουμε την εικόνα στο layout
        byte image[] = plants.get(position).getImage();
        holder.imgPlant.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    //Η κλασση που περιέχει τα δεδομένα του Plant Και η οποία τα συνδέεται με τον adaprter για να εμφανιστούν
    class PlantHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;
        TextView temperature;
        TextView humidity;
        ImageView imgEdit;
        ImageView imgPlant;
        public PlantHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_plant_name);
            description = itemView.findViewById(R.id.txt_plant_description);
            temperature = itemView.findViewById(R.id.txt_Temp);
            humidity = itemView.findViewById(R.id.txt_Hum);
            imgPlant = itemView.findViewById(R.id.imgPlant);
        }
    }

    interface ItemClicked{
        void onClick(int position, View view);
    }
}
