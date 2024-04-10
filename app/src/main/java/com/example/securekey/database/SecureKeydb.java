package com.example.securekey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SecureKeydb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Secure_key.db";
    private static final int DATABASE_VERSION = 1;
    public SecureKeydb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Usertable.CREATE_TABLE);
        db.execSQL(AppCredentials.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long addUser(String username, String phone_number, String email, String password, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Usertable.COLUMN_USERNAME, username);
        values.put(Usertable.COLUMN_PHONE_NUMBER, phone_number);
        values.put(Usertable.COLUMN_EMAIL_ID, email);
        values.put(Usertable.COLUMN_PASSWORD, password);
        values.put(Usertable.COLUMN_GENDER, gender);
        long newRowId = db.insert(Usertable.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
    public int deleteUser(long user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Usertable.COLUMN_USER_ID + "= ?";
        String[] selectionArgs = {String.valueOf(user_id)};
        int deletedRows = db.delete(Usertable.CREATE_TABLE, selection, selectionArgs);
        return deletedRows;

    }
}
