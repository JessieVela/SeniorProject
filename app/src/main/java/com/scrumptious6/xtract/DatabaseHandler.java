package com.scrumptious6.xtract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Inventory.db";
    public static final String DATABASE_TABLE = "Inventory_Table";
    public static final String DATABASE_TEMP_TABLE = "Scanlist_Table";

    // Columns for Inventory Table
    private static final String MATERIAL_NUM = "MATERIAL_NUM";
    private static final String MATERIAL_PLANT = "MATERIAL_PLANT";
    private static final String STORAGE_BIN = "STORAGE_BIN";
    private static final String MATERIAL_ATP = "MATERIAL_ATP";
    private static final String SAFETY_STOCK = "SAFETY_STOCK";

    // Column for Scanlist Table
    private static final String BARCODE = "BARCODE";
    private static final String SCANLIST_ITEM_ATP = "SCANLIST_ITEM_ATP";
    private static final String SCANLIST_ITEM_STORAGE_BIN = "SCANLIST_ITEM_STORAGE_BIN";
    private static final String SCANLIST_ITEM_PLANT = "SCANLIST_ITEM_PLANT";
    private static final String SCANLIST_SAFETY_STOCK = "SCANLIST_SAFETY_STOCK";

    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SCANLIST_TABLE = "CREATE TABLE " + DATABASE_TEMP_TABLE + "("
                + BARCODE + " TEXT PRIMARY KEY,"
                + SCANLIST_ITEM_ATP + " INTEGER,"
                + SCANLIST_ITEM_STORAGE_BIN + " TEXT,"
                + SCANLIST_ITEM_PLANT + " TEXT,"
                + SCANLIST_SAFETY_STOCK + " TEXT"
                + ")";
        db.execSQL(CREATE_SCANLIST_TABLE);

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + MATERIAL_NUM + " TEXT PRIMARY KEY,"
                + MATERIAL_PLANT + " TEXT,"
                + STORAGE_BIN + " TEXT,"
                + MATERIAL_ATP + " INTEGER,"
                + SAFETY_STOCK + " INTEGER"
                + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TEMP_TABLE);
        onCreate(db);
    }
    //To insert Scanned Items into the table
    public boolean insertScannedItem(String barcode, String atp, String bin){
        SQLiteDatabase idb = this.getWritableDatabase();
        String value = "0";
        String plant = "S095";
        ContentValues contentValues = new ContentValues();
        contentValues.put(BARCODE, barcode);
        contentValues.put(SCANLIST_ITEM_ATP, atp);
        contentValues.put(SCANLIST_ITEM_STORAGE_BIN, bin);
        contentValues.put(SCANLIST_ITEM_PLANT, plant);
        contentValues.put(SCANLIST_SAFETY_STOCK, value);
        long result = idb.insert(DATABASE_TEMP_TABLE, null, contentValues);
        return result != -1; //if result = -1 data absent insert
    }

    // To insert from the Scanlist screen
    public void insert(DataClass s){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String plant = "S095";
        String value = "0";
        contentValues.put(BARCODE, s.getBarcode());
        contentValues.put(SCANLIST_ITEM_ATP, s.getAtp());
        contentValues.put(SCANLIST_ITEM_STORAGE_BIN, s.getStorage());
        contentValues.put(SCANLIST_ITEM_PLANT, plant);
        contentValues.put(SCANLIST_SAFETY_STOCK, value);
        db.insert(DATABASE_TEMP_TABLE,null,contentValues);
        db.close();
    }

    // To update product in the Scanlist Table
    public int update(DataClass s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCANLIST_ITEM_ATP, s.getAtp());
        contentValues.put(SCANLIST_ITEM_STORAGE_BIN, s.getStorage());
        return db.update(DATABASE_TEMP_TABLE,contentValues, "BARCODE=?",new String[]{s.getBarcode()});
    }

    // To delete product from the Scanlist Table
    public int delete(DataClass s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TEMP_TABLE, "BARCODE=?",new String[]{s.getBarcode()});
    }

    public void clearDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DATABASE_TABLE, null, null);
    }

    public void clearScanlist(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DATABASE_TEMP_TABLE, null, null);
        //db.execSQL("DROP TABLE IF EXISTS DATABASE_TEMP_TABLE");
    }

    // IS THIS FUNCTION IN USE OR IS IT OBSOLETE?
    public boolean importDatabase(StringBuilder statement) throws IOException {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL(statement.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}


