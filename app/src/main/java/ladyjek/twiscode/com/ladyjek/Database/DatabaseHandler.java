package ladyjek.twiscode.com.ladyjek.Database;

/**
 * Created by ModelUser on 8/3/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ladyjek.twiscode.com.ladyjek.Model.ModelHistory;
import ladyjek.twiscode.com.ladyjek.Model.ModelUser;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "LadyjekDB";
    // ModelUser table name
    private static final String T_USER = "t_user";
    private static final String T_HISTORY = "t_history";

    // ModelUser Table Columns names
    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_HOME_LAT = "home_lat";
    private static final String KEY_HOME_LON = "home_lon";
    private static final String KEY_OFFICE_LAT = "office_lat";
    private static final String KEY_OFFICE_LON = "office_lon";

    private static String KEY_ID_HISTORY ="id_hisory";
    private static String KEY_DATE = "date";
    private static String KEY_TIME = "time";
    private static String KEY_DRIVER = "driver";
    private static String KEY_FROM = "user_from";
    private static String KEY_DESTINATION = "user_destination";
    private static String KEY_DISTANCE = "distance";
    private static String KEY_DURATION = "duration";
    private static String KEY_PRICE = "price";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DBUser", "onCreate");
        String CREATE_TABLE_USER = "CREATE TABLE " + T_USER + "("
                + KEY_ID_USER + " TEXT PRIMARY KEY,"
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

        String CREATE_TABLE_HISTORY = "CREATE TABLE " + T_HISTORY + "("
                + KEY_ID_HISTORY + " TEXT PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_FROM + " TEXT,"
                + KEY_DESTINATION + " TEXT,"
                + KEY_DISTANCE + " TEXT,"
                + KEY_DURATION + " TEXT,"
                + KEY_PRICE + " TEXT"
                + ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + T_USER);
        db.execSQL("DROP TABLE IF EXISTS " + T_HISTORY);
        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new modelUser
    public void insertUser(ModelUser modelUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, modelUser.getId());
        values.put(KEY_NAME, modelUser.getName());
        values.put(KEY_EMAIL, modelUser.getEmail());
        values.put(KEY_PASSWORD, modelUser.getPassword());
        values.put(KEY_PHONE, modelUser.getPhone());
        values.put(KEY_PAYMENT, modelUser.getPayment());
        values.put(KEY_HOME_LAT, modelUser.getHomeLat());
        values.put(KEY_HOME_LON, modelUser.getHomeLon());
        values.put(KEY_OFFICE_LAT, modelUser.getOfficeLat());
        values.put(KEY_OFFICE_LON, modelUser.getOfficeLon());

        // Inserting Row
        db.insert(T_USER, null, values);
        db.close(); // Closing database connection
    }

    // Adding new modelHistory
    public void insertHistory(ModelHistory modelHistory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_HISTORY, modelHistory.getId());
        values.put(KEY_DATE, modelHistory.getDate());
        values.put(KEY_TIME, modelHistory.getTime());
        values.put(KEY_DRIVER, modelHistory.getDriver());
        values.put(KEY_FROM, modelHistory.getFrom());
        values.put(KEY_DESTINATION, modelHistory.getDestination());
        values.put(KEY_DISTANCE, modelHistory.getDistance());
        values.put(KEY_DURATION, modelHistory.getDuration());
        values.put(KEY_PRICE, modelHistory.getPrice());

        // Inserting Row
        db.insert(T_HISTORY, null, values);
        db.close(); // Closing database connection
    }


    public ModelUser getUser() {
        String allData = "SELECT  * FROM " + T_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(allData, null);
        cursor.close();

        ModelUser modelUser = new ModelUser(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9)
        );
        return modelUser;
    }

    public ModelUser getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_USER, new String[]{KEY_ID_USER,
                        KEY_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_PHONE,
                        KEY_PAYMENT, KEY_HOME_LAT, KEY_HOME_LON,
                        KEY_OFFICE_LAT, KEY_OFFICE_LON}, KEY_ID_USER + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ModelUser modelUser = new ModelUser(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9)

        );
        return modelUser;
    }

    public ModelHistory getHistory(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_HISTORY, new String[]{KEY_ID_HISTORY,
                        KEY_DATE, KEY_TIME, KEY_DRIVER, KEY_FROM,
                        KEY_DESTINATION, KEY_DISTANCE, KEY_DURATION,
                        KEY_PRICE}, KEY_ID_HISTORY + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ModelHistory modelHistory = new ModelHistory(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8)

        );
        return modelHistory;
    }
    // Getting contacts Count
    public int getUserCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + T_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        // return count
        return count;
    }

    /*
       Deleting a todo
    */
    public void deleteSaved(String article_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(T_USER, KEY_ID_USER + " = ?",
                new String[]{String.valueOf(article_id)});
    }


}
