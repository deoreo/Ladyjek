package ladyjek.twiscode.com.ladyjek.Database;

/**
 * Created by User on 8/3/2015.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.Model.User;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "LadyjekDB";
    // Contacts table name
    private static final String T_USER = "t_article";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_HOME_LAT = "home_lat";
    private static final String KEY_HOME_LON = "home_lon";
    private static final String KEY_OFFICE_LAT = "office_lat";
    private static final String KEY_OFFICE_LON = "office_lon";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DBMatome", "onCreate");
        String CREATE_TABLE = "CREATE TABLE " + T_USER + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_PAYMENT + " TEXT,"
                + KEY_HOME_LAT + " TEXT,"
                + KEY_HOME_LON + " TEXT,"
                + KEY_OFFICE_LAT + " TEXT,"
                + KEY_OFFICE_LON + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("DBMatome", "OnUpgrade");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + T_USER);

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_PAYMENT, user.getPayment());
        values.put(KEY_HOME_LAT, user.getHomeLat());
        values.put(KEY_HOME_LON, user.getHomeLon());
        values.put(KEY_OFFICE_LAT, user.getOfficeLat());
        values.put(KEY_OFFICE_LON, user.getOfficeLon());

        // Inserting Row
        db.insert(T_USER, null, values);
        db.close(); // Closing database connection
    }

    public User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_USER, new String[]{KEY_ID,
                        KEY_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_PHONE,
                        KEY_PAYMENT, KEY_HOME_LAT, KEY_HOME_LON,
                        KEY_OFFICE_LAT, KEY_OFFICE_LON}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9)

        );
        return user;
    }


    /*
       Deleting a todo
    */
    public void deleteSaved(String article_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(T_USER, KEY_ID + " = ?",
                new String[]{String.valueOf(article_id)});
    }


}
