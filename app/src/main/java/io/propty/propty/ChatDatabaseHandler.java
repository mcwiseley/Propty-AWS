package io.propty.propty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by hankerins on 7/18/16.
 */
public class ChatDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String CHAT_DATABASE_NAME = "chatDB.db";
    private static final String TABLE_CHAT = "chat";

    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_DATETIME = "time";
    private static final String KEY_MESSAGE = "message";


    public ChatDatabaseHandler(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, CHAT_DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHAT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_UID + " TEXT," +
            KEY_DATETIME + " REAL," +
            KEY_MESSAGE + " TEXT" + ")";
        db.execSQL(CREATE_CHAT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        // Create tables again
        onCreate(db);
    }

    public void addMessage(ChatMessage message){

        //Retrieve the user ID, date-time, and message from the chat message class and put in a
        //ContentValues object

        ContentValues values = new ContentValues();
        values.put(KEY_UID, message.getUid());
        values.put(KEY_DATETIME, message.getDateAndTime().getTimeInMillis());
        values.put(KEY_MESSAGE, message.getMessage());

        //Add the ContentValues to a new row on the chat table.
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CHAT, null, values);
        db.close();
    }

    public ArrayList<ChatMessage> loadRecentMessages(ArrayList<ChatMessage> newList, int limit){

        //Write a query to get most recent messages
        String query = "Select * FROM " + TABLE_CHAT +
                " ORDER BY " + KEY_DATETIME + " DESC " + " LIMIT " + limit;

        //Execute query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //Check to make sure the table isn't empty
        if (cursor.moveToFirst() == false)
            return  newList;
        //Move to last message in table (first message chronologically)
        cursor.moveToLast();

        //Loop through the list of messages and add them to our array list.
        for(int i = 0; i < cursor.getCount(); i ++) {
            ChatMessage message = new ChatMessage();
            message.setUid(cursor.getString(1));
            message.setDateAndTime((long)Float.parseFloat(cursor.getString(2)));
            message.setMessage(cursor.getString(3));
            newList.add(message);
            cursor.moveToPrevious();
        }

        //Close the cursor and return the array list.
        cursor.close();
        return newList;
    }

}
