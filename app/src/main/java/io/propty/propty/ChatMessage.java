package io.propty.propty;

import java.util.Calendar;

/**
 * Created by hankerins on 7/18/16.
 * This class contains the user ID, date-time, and message text for a given message sent in chat client.
 * The date-time is saved as a calendar object here, but as a long on the SQL database.
 * The Calendar object is used to better format how the time is displayed to the user.
 */
public class ChatMessage {
    private String uid, message;
    private Calendar dateAndTime;

    //Constructors

    ChatMessage(){

    }

    ChatMessage(String uid, String message){
        this.uid = uid;
        this.message = message;
        dateAndTime = Calendar.getInstance();
    }

    ChatMessage(String uid, String message, long timeInMillis){
        this.uid = uid;
        this.message = message;
        dateAndTime.setTimeInMillis(timeInMillis);
    }

    //Get Methods

    String getUid() { return uid; };
    String getMessage() { return message; }
    Calendar getDateAndTime() { return dateAndTime; }

    //Set Methods

    void setUid(String uid) { this.uid = uid; }
    void setMessage(String message) { this.message = message; }
    void setDateAndTime(long timeInMillis) {
        dateAndTime = Calendar.getInstance();
        dateAndTime.setTimeInMillis(timeInMillis); }
}
