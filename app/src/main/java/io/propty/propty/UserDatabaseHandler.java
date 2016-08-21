package io.propty.propty;

/**
 * Created by micheal on 11/18/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class UserDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "propty_login";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    //Settings table name
    private static final String TABLE_SETTINGS = "settings";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

<<<<<<< HEAD:app/src/main/java/io/propty/propty/DatabaseHandler.java
    //Settings Table Column names
    private static final String KEY_KEYWORDS = "keywords";
    private static final String KEY_BEDS = "beds";
    private static final String KEY_BATHS = "baths";
    private static final String KEY_MIN_PRICE = "min_price";
    private static final String KEY_MAX_PRICE = "max_price";
    private static final String KEY_SQ_FT = "sq_ft";
    private static final String KEY_STRUCTURE = "structure";
    private static final String KEY_WITHIN = "within";
    private static final String KEY_ZIP = "zip";





    public DatabaseHandler(Context context) {
=======
    public UserDatabaseHandler(Context context) {
>>>>>>> 1acad8634be55c36d0275d713c6144ab9803e96f:app/src/main/java/io/propty/propty/UserDatabaseHandler.java
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " TEXT,"
                + KEY_KEYWORDS + " TEXT,"
                + KEY_BEDS + " REAL,"
                + KEY_BATHS + " REAL,"
                + KEY_MIN_PRICE + " INTEGER,"
                + KEY_MAX_PRICE + " INTEGER,"
                + KEY_SQ_FT + " INTEGER,"
                + KEY_STRUCTURE + " INTEGER,"
                + KEY_WITHIN + " INTEGER,"
                + KEY_ZIP + " INTEGER" + ")";
        db.execSQL(CREATE_SETTINGS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String uname, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, fname); // FirstName
        values.put(KEY_LASTNAME, lname); // LastName
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, uname); // UserName
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);

        //Write the UID into MyPrefsFile so we can read settings from the settings table
        ContentValues settingsUID = new ContentValues();
        settingsUID.put(KEY_UID, uid);
        settingsUID.put(KEY_KEYWORDS, "");
        settingsUID.put(KEY_BEDS, 1);
        settingsUID.put(KEY_BATHS, 1);
        settingsUID.put(KEY_MIN_PRICE, 0);
        settingsUID.put(KEY_MAX_PRICE, 0);
        settingsUID.put(KEY_SQ_FT, 0);
        settingsUID.put(KEY_STRUCTURE, 0);
        settingsUID.put(KEY_WITHIN, 0);
        settingsUID.put(KEY_ZIP, 0);

        db.insert(TABLE_SETTINGS, null, settingsUID);
        db.close(); // Closing database connection

    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("uid", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        return user;
    }

    public UserSettings getUserSettings(String uid) {
        UserSettings userSettings = new UserSettings();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS + " WHERE " + KEY_UID +
                " = \'" + uid + "\';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userSettings.setUid(uid);
            userSettings.setKeywords(cursor.getString(2));
            userSettings.setNumBedrooms(cursor.getFloat(3));
            userSettings.setNumBathrooms(cursor.getFloat(4));
            userSettings.setMinPrice(cursor.getInt(5));
            userSettings.setMaxPrice(cursor.getInt(6));
            userSettings.setSquareFootage(cursor.getInt(7));
            userSettings.setStructure(cursor.getInt(8));
            userSettings.setWithin(cursor.getInt(9));
            userSettings.setZip(cursor.getInt(10));
        }
        cursor.close();
        db.close();
        return userSettings;
    }

    public void updateSettings(UserSettings settings){
        String updateStatement = "UPDATE " + TABLE_SETTINGS + " SET " +
                KEY_KEYWORDS + "=\'" + settings.getKeywords() + "\', " +
                KEY_BEDS + "=\'" + settings.getNumBedrooms() + "\', " +
                KEY_BATHS + "=\'" + settings.getNumBathrooms() + "\', " +
                KEY_MIN_PRICE + "=\'" + settings.getMinPrice() + "\', " +
                KEY_MAX_PRICE + "=\'" + settings.getMaxPrice() + "\', " +
                KEY_SQ_FT + "=\'" + settings.getSquareFootage() + "\', " +
                KEY_STRUCTURE + "=\'" + settings.getStructure() + "\', " +
                KEY_WITHIN + "=\'" + settings.getWithin() + "\', " +
                KEY_ZIP + "=\'" + settings.getZip() + "\' " +
                "WHERE " + KEY_UID + "=\'" + settings.getUid() + "\';";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateStatement);
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
