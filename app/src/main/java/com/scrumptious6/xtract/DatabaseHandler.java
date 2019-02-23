package com.scrumptious6.xtract;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Inventory.db";
    private static final String DATABASE_TABLE = "Inventory_Table";
    private static final String DATABASE_TEMP_TABLE = "Scanlist_Table";

    //Columns for Inventory Table
    private static final String MATERIAL_NUM = "MATERIAL_NUM";
    private static final String MATERIAL_PLANT = "MATERIAL_PLANT";
    private static final String STORAGE_BIN = "STORAGE_BIN";
    private static final String MATERIAL_ATP = "MATERIAL_ATP";
    private static final String SAFETY_STOCK = "SAFETY_STOCK";

    //Column for Scanlist Table
    private static final String ID = "ID";
    private static final String BARCODE = "BARCODE";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        ///The Scanlist table is created in the inventory database.
        String CREATE_SCANLIST_TABLE = "CREATE TABLE " + DATABASE_TEMP_TABLE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BARCODE + " TEXT" + ")";
        db.execSQL(CREATE_SCANLIST_TABLE);

        ///The Inventory table is created in the inventory database
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + MATERIAL_NUM + " VARCHAR(25)," + MATERIAL_PLANT + " VARCHAR(25),"
                + STORAGE_BIN + " VARCHAR(25)," + MATERIAL_ATP + " INTEGER,"
                + SAFETY_STOCK + " INTEGER"
                + ")";

        db.execSQL(CREATE_ITEMS_TABLE);

        //String INSERT = "INSERT INTO " + DATABASE_TEMP_TABLE + "(" + BARCODE + ")" + " VALUES"
        //        + "(" + "purple" +")";
        //db.execSQL("INSERT INTO " + DATABASE_TEMP_TABLE + "(" + BARCODE + ")" + " VALUES"
        //        + "(" + "purple" +")");

        //db.execSQL("INSERT INTO " + DATABASE_TEMP_TABLE+ "(ID,BARCODE) VALUES (0, 'purple')");
        //db.execSQL("INSERT INTO " + DATABASE_TABLE+ "(MATERIAL_NUM,MATERIAL_PLANT,STORAGE_BIN,MATERIAL_ATP,SAFETY_STOCK) " +
         //       "VALUES ('517','S095',null,1,0)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TEMP_TABLE);
        onCreate(db);
    }
    //To insert Scanned Items into the table///
    public boolean insertScannedItem(String name){
        SQLiteDatabase idb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(ID, 1);
        contentValues.put(BARCODE, name);
        long result = idb.insert(DATABASE_TEMP_TABLE, null, contentValues);
        return result != -1; //if result = -1 data absent insert
    }
    ///To view the Scanlist items///
    public Cursor viewScanList(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * from "+ DATABASE_TEMP_TABLE;
        Cursor  cursor = db.rawQuery(query, null);
        return cursor;
    }
    ///To view the Database items///
    public Cursor viewDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * from "+ DATABASE_TABLE;
        Cursor  cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void clearDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DATABASE_TABLE, null, null);
    }

    public boolean importDatabase(StringBuilder statement) throws IOException {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL(statement.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
