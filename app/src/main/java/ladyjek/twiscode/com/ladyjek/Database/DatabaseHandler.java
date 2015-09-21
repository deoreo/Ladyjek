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

import ladyjek.twiscode.com.ladyjek.Model.ModelDriver;
import ladyjek.twiscode.com.ladyjek.Model.ModelHistory;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "LadyjekDB";
    // ModelUser table name
    private static final String T_USER = "t_user";
    private static final String T_HISTORY = "t_history";
    private static final String T_DRIVER = "t_driver";

    // ModelUser Table Columns names
    private static final String KEY_USER_ID = "id_user";
    private static final String KE_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_PHONE_2nd = "phone2nd";
    private static final String KEY_USER_PAYMENT = "payment";
    private static final String KEY_USER_HOMELAT = "home_lat";
    private static final String KEY_USER_HOMELON = "home_lon";
    private static final String KEY_USER_OFFICELAT = "office_lat";
    private static final String KEY_USER_OFFICELON = "office_lon";

    private static String KEY_HISTORY_ID ="id_hisory";
    private static String KEY_HISTORY_DATE = "date";
    private static String KEY_HISTORY_TIME = "time";
    private static String KEY_HISTORY_DRIVER = "driver";
    private static String KEY_HISTORY_FROM = "user_from";
    private static String KEY_HISTORY_DESTINATION = "user_destination";
    private static String KEY_HISTORY_DISTANCE = "distance";
    private static String KEY_HISTORY_DURATION_ = "duration";
    private static String KEY_HISTORY_PRICE = "price";

    private static final String KEY_DRIVER_ID = "id_driver";
    private static final String KEY_DRIVER_NAME = "name_driver";
    private static final String KEY_DRIVER_IMAGE = "image_driver";
    private static final String KEY_DRIVER_NOPOL = "nopol_driver";
    private static final String KEY_DRIVER_PHONE = "phone_driver";
    private static final String KEY_DRIVER_RATE = "rate_driver";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DBUser", "onCreate");
        String CREATE_TABLE_USER = "CREATE TABLE " + T_USER + "("
                + KEY_USER_ID + " TEXT PRIMARY KEY,"
                + KE_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_PHONE + " TEXT,"
                + KEY_USER_PHONE_2nd + " TEXT,"
                + KEY_USER_PAYMENT + " TEXT,"
                + KEY_USER_HOMELAT + " TEXT,"
                + KEY_USER_HOMELON + " TEXT,"
                + KEY_USER_OFFICELAT + " TEXT,"
                + KEY_USER_OFFICELON + " TEXT"
                + ")";

        String CREATE_TABLE_HISTORY = "CREATE TABLE " + T_HISTORY + "("
                + KEY_HISTORY_ID + " TEXT PRIMARY KEY,"
                + KEY_HISTORY_DATE + " TEXT,"
                + KEY_HISTORY_TIME + " TEXT,"
                + KEY_HISTORY_DRIVER + " TEXT,"
                + KEY_HISTORY_FROM + " TEXT,"
                + KEY_HISTORY_DESTINATION + " TEXT,"
                + KEY_HISTORY_DISTANCE + " TEXT,"
                + KEY_HISTORY_DURATION_ + " TEXT,"
                + KEY_HISTORY_PRICE + " TEXT"
                + ")";


        String CREATE_TABLE_DRIVER = "CREATE TABLE " + T_DRIVER + "("
                + KEY_DRIVER_ID + " TEXT PRIMARY KEY,"
                + KEY_DRIVER_NAME + " TEXT,"
                + KEY_DRIVER_IMAGE + " TEXT,"
                + KEY_DRIVER_NOPOL + " TEXT,"
                + KEY_DRIVER_PHONE + " TEXT,"
                + KEY_DRIVER_RATE + " TEXT"
                + ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_DRIVER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + T_USER);
        db.execSQL("DROP TABLE IF EXISTS " + T_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + T_DRIVER);
        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new modelUser
    public void insertUser(ModelUserOrder modelUserOrder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, modelUserOrder.getId());
        values.put(KE_USER_NAME, modelUserOrder.getName());
        values.put(KEY_USER_EMAIL, modelUserOrder.getEmail());
        values.put(KEY_USER_PASSWORD, modelUserOrder.getPassword());
        values.put(KEY_USER_PHONE, modelUserOrder.getPhone());
        values.put(KEY_USER_PHONE_2nd, modelUserOrder.getPhone2nd());
        values.put(KEY_USER_PAYMENT, modelUserOrder.getPayment());
        values.put(KEY_USER_HOMELAT, modelUserOrder.getHomeLat());
        values.put(KEY_USER_HOMELON, modelUserOrder.getHomeLon());
        values.put(KEY_USER_OFFICELAT, modelUserOrder.getOfficeLat());
        values.put(KEY_USER_OFFICELON, modelUserOrder.getOfficeLon());

        // Inserting Row
        db.insert(T_USER, null, values);
        db.close(); // Closing database connection
    }

    // Adding new modelHistory
    public void insertHistory(ModelHistory modelHistory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY_ID, modelHistory.getId());
        values.put(KEY_HISTORY_DATE, modelHistory.getDate());
        values.put(KEY_HISTORY_TIME, modelHistory.getTime());
        values.put(KEY_HISTORY_DRIVER, modelHistory.getDriver());
        values.put(KEY_HISTORY_FROM, modelHistory.getFrom());
        values.put(KEY_HISTORY_DESTINATION, modelHistory.getDestination());
        values.put(KEY_HISTORY_DISTANCE, modelHistory.getDistance());
        values.put(KEY_HISTORY_DURATION_, modelHistory.getDuration());
        values.put(KEY_HISTORY_PRICE, modelHistory.getPrice());

        // Inserting Row
        db.insert(T_HISTORY, null, values);
        db.close(); // Closing database connection
    }

    public void insertDriver(ModelDriver modelDriver) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRIVER_ID, modelDriver.getId());
        values.put(KEY_DRIVER_NAME, modelDriver.getName());
        values.put(KEY_DRIVER_IMAGE, modelDriver.getImage());
        values.put(KEY_DRIVER_NOPOL, modelDriver.getNopol());
        values.put(KEY_DRIVER_PHONE, modelDriver.getPhone());
        values.put(KEY_DRIVER_RATE, modelDriver.getRate());

        // Inserting Row
        db.insert(T_DRIVER, null, values);
        db.close(); // Closing database connection
    }


    public ModelUserOrder getUser() {
        String allData = "SELECT  * FROM " + T_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(allData, null);
        cursor.close();

        ModelUserOrder modelUserOrder = new ModelUserOrder(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9),cursor.getString(10)
        );
        return modelUserOrder;
    }

    public ModelUserOrder getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_USER, new String[]{KEY_USER_ID,
                        KE_USER_NAME, KEY_USER_EMAIL, KEY_USER_PASSWORD, KEY_USER_PHONE,
                        KEY_USER_PAYMENT, KEY_USER_HOMELAT, KEY_USER_HOMELON,
                        KEY_USER_OFFICELAT, KEY_USER_OFFICELON}, KEY_USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ModelUserOrder modelUserOrder = new ModelUserOrder(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9),cursor.getString(10)

        );
        return modelUserOrder;
    }

    public ModelHistory getHistory(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_HISTORY, new String[]{KEY_HISTORY_ID,
                        KEY_HISTORY_DATE, KEY_HISTORY_TIME, KEY_HISTORY_DRIVER, KEY_HISTORY_FROM,
                        KEY_HISTORY_DESTINATION, KEY_HISTORY_DISTANCE, KEY_HISTORY_DURATION_,
                        KEY_HISTORY_PRICE}, KEY_HISTORY_ID + "=?",
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

    public ModelDriver getDriver() {
        String allData = "SELECT  * FROM " + T_DRIVER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(allData, null);
        cursor.close();

        ModelDriver modelDriver = new ModelDriver(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5)
        );
        return modelDriver;
    }

    /*
       Deleting a todo
    */
    public void deleteSaved(String article_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(T_USER, KEY_USER_ID + " = ?",
                new String[]{String.valueOf(article_id)});
    }

    public void logout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ T_USER);

    }


}
