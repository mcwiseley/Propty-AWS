package io.propty.propty;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by hankerins on 6/14/16.
 */
public class PropertyDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "propertyDB.db";
    private static final String TABLE_PROPERTIES = "properties";


    public static final String COLUMN_ID = "id";
    public static final String DESC = "desc";
    public static final String LIST_ID = "listId";
    public static final String AREA_UNITS = "areaUnits";
    public static final String TYPE = "type";
    public static final String BEDS = "beds";
    public static final String AREA = "area";
    public static final String IMAGE = "image";
    public static final String BATHS = "baths";
    public static final String PRICE = "price";
    public static final String ADDRESS = "address";
    public static final String ZIP = "zip";

    //private String desc, listId, areaUnits, type;
    //private int beds, area, image;
    //private double baths, price;
    //string address, int zip

    public PropertyDatabaseHandler(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            TABLE_PROPERTIES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + DESC +
            " TEXT," + LIST_ID + " TEXT," + AREA_UNITS + " TEXT," + TYPE + " TEXT," +
            BEDS + " INTEGER," + AREA + " INTEGER," + IMAGE + " INTEGER," + BATHS + " REAL," +
           PRICE + " REAL," + ADDRESS + " TEXT," + ZIP + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        onCreate(db);
    }

    public void addProperty(Property property) {

        ContentValues values = new ContentValues();
        values.put(DESC, property.getDesc());
        values.put(LIST_ID, property.getListId());
        values.put(AREA_UNITS, property.getAreaUnits());
        values.put(TYPE, property.getType());
        values.put(BEDS, property.getBeds());
        values.put(AREA, property.getArea());
        values.put(IMAGE, property.getImage());
        values.put(BATHS, property.getBaths());
        values.put(PRICE, property.getPrice());
        values.put(ADDRESS, property.getAddress());
        values.put(ZIP, property.getZip());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PROPERTIES, null, values);
        db.close();
    }


    public ArrayList<Property> listProperties(Context context, ArrayList<Property> newList, int limit){

        final  String EXTRA = "io.propty.propty.MESSAGE";
        final String PREFS = "MyPrefsFile";

        final  String numBedrooms_string = EXTRA + "numBedrooms";
        final  String numBathrooms_string = EXTRA + "numBathrooms";
        final  String minPrice_string = EXTRA + "minPrice";
        final  String maxPrice_string = EXTRA + "maxPrice";
        final  String squareFootage_string = EXTRA + "squareFootage";
        final  String structure_string = EXTRA + "structure";
        final  String pool_string = EXTRA + "pool";
        final  String garage_string = EXTRA + "garage";
        final  String within_string = EXTRA + "within";
        final  String radioCurrent_string = EXTRA + "radioCurrent";
        final  String radioZip_string = EXTRA + "radioZip";
        final  String zip_string = EXTRA + "zip";
        SharedPreferences settings = context.getSharedPreferences(PREFS, 0);

        int numBedrooms_val = settings.getInt(numBedrooms_string, 1);
        float numBathrooms_val = settings.getFloat(numBathrooms_string, 1);
        int minPrice_val = settings.getInt(minPrice_string, 0);
        int maxPrice_val = settings.getInt(maxPrice_string, 0);
        int squareFootage_val = settings.getInt(squareFootage_string, 0);
        int structure_val = settings.getInt(structure_string, 0);
        boolean pool_val = settings.getBoolean(pool_string, false);
        boolean garage_val = settings.getBoolean(garage_string, false);
        int within_val = settings.getInt(within_string, 0);
        boolean radioCurrent_val = settings.getBoolean(radioCurrent_string, true);
        boolean radioZip_val = settings.getBoolean(radioZip_string, false);
        int zip_val = settings.getInt(zip_string, 0);

        String[] structure_arr = context.getResources().getStringArray(R.array.structure_array);
        String structure_str = structure_arr[structure_val].toLowerCase();


        String query = "Select * FROM " + TABLE_PROPERTIES +
                " WHERE (" + BEDS + " =  " + numBedrooms_val +
                ") AND (" + BATHS + " = " + numBathrooms_val +
                ") AND (" + PRICE + " >= " + minPrice_val +
                ") AND (" + PRICE + " <= " + maxPrice_val +
                ") AND (" + TYPE + " LIKE \'%" + structure_str + "%\')" +
                " ORDER BY " + PRICE + " LIMIT " + limit;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);



        if (cursor.moveToFirst() == false)
            return  newList;
        for(int i = 0; i < cursor.getCount(); i ++){
            Property property = new Property();
            property.setId(Integer.parseInt(cursor.getString(0)));
            property.setDesc(cursor.getString(1));
            property.setListId(cursor.getString(2));
            property.setAreaUnits(cursor.getString(3));
            property.setType(cursor.getString(4));
            property.setBeds(Integer.parseInt(cursor.getString(5)));
            property.setArea(Integer.parseInt(cursor.getString(6)));
            property.setImage(Integer.parseInt(cursor.getString(7)));
            property.setBaths(Double.parseDouble(cursor.getString(8)));
            property.setPrice(Double.parseDouble(cursor.getString(9)));
            property.setAddress(cursor.getString(10));
            property.setZip(Integer.parseInt(cursor.getString(11)));
            newList.add(property);
            cursor.moveToNext();
        }
        cursor.close();
        return newList;

    }

    public Property findProperty(String listId) {
        String query = "Select * FROM " + TABLE_PROPERTIES + " WHERE " + LIST_ID + " =  \"" + listId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Property property = new Property();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            property.setId(Integer.parseInt(cursor.getString(0)));
            property.setDesc(cursor.getString(1));
            property.setListId(cursor.getString(2));
            property.setAreaUnits(cursor.getString(3));
            property.setType(cursor.getString(4));
            property.setBeds(Integer.parseInt(cursor.getString(5)));
            property.setArea(Integer.parseInt(cursor.getString(6)));
            property.setImage(Integer.parseInt(cursor.getString(7)));
            property.setBaths(Double.parseDouble(cursor.getString(8)));
            property.setPrice(Double.parseDouble(cursor.getString(9)));
            property.setAddress(cursor.getString(10));
            property.setZip(Integer.parseInt(cursor.getString(11)));
            cursor.close();
        } else {
            property = null;
        }
        db.close();
        return property;
    }

    public boolean deleteProperty(String listId) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PROPERTIES + " WHERE " + LIST_ID + " =  \"" + listId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Property property = new Property();

        if (cursor.moveToFirst()) {
            property.setId(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PROPERTIES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(property.getId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}

