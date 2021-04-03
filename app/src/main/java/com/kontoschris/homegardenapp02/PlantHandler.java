package com.kontoschris.homegardenapp02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;

public class PlantHandler extends DatabaseHelper {

    public PlantHandler(Context context) {
        super(context);
    }

    public boolean create(Plant plant){
        ContentValues values = new ContentValues();
        values.put("title", plant.getTitle());
        values.put("description", plant.getDescription());
        SQLiteDatabase db = this.getReadableDatabase();

        boolean isSuccessfull;
        long nRet;
        nRet = db.insert("Plant", null, values);
        if (nRet >0) {
            isSuccessfull = true;
        } else {
            isSuccessfull = false;
        }
        db.close();
        return isSuccessfull;
    }

    public ArrayList<Plant> readPlants(){
        ArrayList<Plant> plants = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Plant ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null  );

        if (cursor.moveToFirst()) {
            do {
                
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                Plant plant = new Plant(title, description);
                plant.setId(id);
                plants.add(plant);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();

        }
        return plants;
    }

    public Plant readOnePlant(int id) {
        Plant plant = null;
        String sqlQuery = "SELECT * FROM Plant WHERE id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            int plantid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            plant = new Plant(title, description);
            plant.setId(plantid);
        }
        cursor.close();
        db.close();

        return plant;
    }

    public boolean update(Plant plant){
        long nRet;
        boolean isSuccess;
        ContentValues values = new ContentValues();
        values.put("title", plant.getTitle());
        values.put("description", plant.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        nRet = db.update("Plant", values,"id='"+plant.getId()+"'", null );
        if ( nRet > 0) {
            isSuccess  =true;
        } else {
            isSuccess = false;
        }
        db.close();
        return isSuccess;
    }

    public boolean delete(int id){
        boolean isDeleted;
        long nRet;
        SQLiteDatabase db = this.getWritableDatabase();
        nRet = db.delete("Plant","id='"+id+"'", null );
        if (nRet > 0){
            isDeleted = true;
        } else {
            isDeleted = false;
        }
        db.close();
        return isDeleted;

    }

}
