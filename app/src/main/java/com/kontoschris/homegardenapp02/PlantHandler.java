//Η βασική κλάσση που διαχειρίζεται το Plant object όσο αφορά τις λειτοτργίες με την Βαση δεδομένων
//Λειτουργίες CRUD
package com.kontoschris.homegardenapp02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;

public class PlantHandler extends DatabaseHelper {

    public PlantHandler(Context context) {
        super(context);
    }

    public boolean create(Plant plant){
        //ετοιμάζουμε και πέρνουμε τις τιμές για αποήκευση και τοις βάζουμε στο Values Object
        ContentValues values = new ContentValues();
        values.put("title", plant.getTitle());
        values.put("description", plant.getDescription());
        values.put("temperature", plant.getTemperature());
        values.put("humidity", plant.getHumidity());
        values.put("img", plant.getImage());

        SQLiteDatabase db = this.getReadableDatabase(); //πέρνουμε το αντικείμενο της SQL lite

        boolean isSuccessfull;
        long nRet;

        nRet = db.insert("Plant", null, values); //εισαγωγή στην βάση του Plant Object
        if (nRet >0) {
            isSuccessfull = true;
        } else {
            isSuccessfull = false;
        }
        db.close();
        return isSuccessfull;
    }

    //Λειτουργία RETRIEVE  - Retrieve Plant Objects σε ARRAY
    public ArrayList<Plant> readPlants(){
        ArrayList<Plant> plants = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Plant ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null  );

        if (cursor.moveToFirst()) {
            do {
                //διαβάζουμε τα δεδομένα του κάθε πεδίου του πίνακα Plants
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                int temperature = cursor.getInt(cursor.getColumnIndex("temperature"));
                int humidity = cursor.getInt(cursor.getColumnIndex("humidity"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("img"));

                //για κάθε εγγραφή του πίνακα δημιουργούμε ένα αντικείμενο Plant
                Plant plant = new Plant(title, description,temperature ,humidity, image);
                plant.setId(id);
                plants.add(plant);
            }while (cursor.moveToNext()); //στην επόμενη εγγραφή

            //τα κλείνουμε...
            cursor.close();
            db.close();

        }
        return plants;
    }

    //Λειτουργία RETRIEVE - φορτώμνουμε απο την SQL lite ένα συγκεκριμένο plant Βαση του ID του
    public Plant readOnePlant(int id) {
        Plant plant = null;
        String sqlQuery = "SELECT * FROM Plant WHERE id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            int plantid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            int temperature = cursor.getInt(cursor.getColumnIndex("temperature"));
            int humidity = cursor.getInt(cursor.getColumnIndex("humidity"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("img"));

            plant = new Plant(title, description,1,1,null);
            plant.setId(plantid);
        }
        cursor.close();
        db.close();

        return plant;
    }

    //Λειτουργία UPDATE του Plant... (δεν υλοποιήθηκε στο GUI όμως)....???
    public boolean update(Plant plant){
        long nRet;
        boolean isSuccess;
        ContentValues values = new ContentValues();
        values.put("title", plant.getTitle());
        values.put("description", plant.getDescription());
        values.put("temperature", plant.getTemperature());
        values.put("humidity", plant.getHumidity());

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

    //λειτουργία DELETE plant
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
