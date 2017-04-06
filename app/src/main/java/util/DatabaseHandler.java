package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amanleenpuri on 4/29/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "gogreenClient";

    // Contacts table name
    private static final String TABLE_USER_DETAILS = "userDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_DETAILS_TABLE = "CREATE TABLE " + TABLE_USER_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT )";
        db.execSQL(CREATE_USER_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);

        // Create tables again
        onCreate(db);
    }

    // Adding new user
    public void addUser(String userName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER_DETAILS, "1", null);

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userName); // Contact Name
        values.put(KEY_ID, userId); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_USER_DETAILS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Cursor getUserNameFromId(int id) {
        String selectQuery = "SELECT  "+KEY_NAME+","+KEY_ID+" FROM " + TABLE_USER_DETAILS+" WHERE"+KEY_ID+"="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        db.close(); // Closing database connection
        return res;
    }

    public int getUserId(){
        String selectQuery = "SELECT  "+KEY_ID+" FROM " + TABLE_USER_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i=0;
        int userId = 0;

        while(cursor.moveToNext()){
            userId = cursor.getInt(cursor.getColumnIndex("id"));
            i++;
        }
        db.close(); // Closing database connection
        return userId;
    }

    public String getUsername(){
        String selectQuery = "SELECT  "+KEY_NAME+","+KEY_ID+" FROM " + TABLE_USER_DETAILS+" ORDER BY "+KEY_ID+" ASC Limit 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i=0;
        String userName = null;
        while(cursor.moveToNext()){
            userName = cursor.getString(cursor.getColumnIndex("name"));
            i++;
        }
        db.close(); // Closing database connection
        return userName;
    }


}
