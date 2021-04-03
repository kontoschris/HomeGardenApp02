package com.kontoschris.homegardenapp02;

import android.content.Context;
import android.content.Intent;
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

    ArrayList<Plant> plants;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;
    public PlantAdapter(ArrayList<Plant> arrayList, Context context, ItemClicked itemClicked){
        plants = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override
    public PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.plant_holder, parent, false);
        this.parent = parent;
        return new PlantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantHolder holder, int position) {
        holder.title.setText(plants.get(position).getTitle());
        holder.description.setText(plants.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    class PlantHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;
        ImageView imgEdit;
        public PlantHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_plant_name);
            description = itemView.findViewById(R.id.txt_plant_description);
            imgEdit = itemView.findViewById(R.id.img_edit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (description.getMaxLines() ==1 ){
                        description.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        description.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onClick(getAdapterPosition(), itemView);
                }
            });

        }
    }

    interface ItemClicked{
        void onClick(int position, View view);
    }
}
