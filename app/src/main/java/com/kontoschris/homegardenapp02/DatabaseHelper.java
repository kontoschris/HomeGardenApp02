//Κλάσση για την δημιουργία ή διαγραφή της βάσης
//Γίνεται χρήση της SQL lite του κινητού
package com.kontoschris.homegardenapp02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final  int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "plants";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }


    @Override //δημιουργία του πίνακα
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE Plant (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, " +
                "description TEXT, temperature INT, humidity INT, img BLOB)";
        db.execSQL(sqlQuery);
    }

    @Override //αν κάνουμε κάποια αλλαγη στον πίνακα τότε τον διαγράφουμε πρώτα πριν γινει πάλει create
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS Plant";
        db.execSQL(sqlQuery);
        onCreate(db);

    }
}
