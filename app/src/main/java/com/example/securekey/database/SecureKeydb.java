package com.example.securekey.database;

import static com.example.securekey.database.Usertable.COLUMN_PASSWORD;
import static com.example.securekey.database.Usertable.COLUMN_USERNAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.securekey.User;

import java.util.ArrayList;
import java.util.List;

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
        values.put(COLUMN_USERNAME, username);
        values.put(Usertable.COLUMN_PHONE_NUMBER, phone_number);
        values.put(Usertable.COLUMN_EMAIL_ID, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(Usertable.COLUMN_GENDER, gender);
        long newRowId = db.insert(Usertable.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
//    public int deleteUser(long user_id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String selection = Usertable.COLUMN_USER_ID + "= ?";
//        String[] selectionArgs = {String.valueOf(user_id)};
//        int deletedRows = db.delete(Usertable.CREATE_TABLE, selection, selectionArgs);
//        return deletedRows;
//
//    }
//    public User getUserById(long user_id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String[] projection = {
//                Usertable.COLUMN_USER_ID,
//                COLUMN_USERNAME,
//                Usertable.COLUMN_PHONE_NUMBER,
//                Usertable.COLUMN_EMAIL_ID,
//                COLUMN_PASSWORD,
//                Usertable.COLUMN_GENDER
//        };
//        String selection = Usertable.COLUMN_USER_ID + "= ?";
//        String[] selectionArgs = {String.valueOf(user_id)};
//        Cursor cursor = db.query(
//                Usertable.CREATE_TABLE,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//        User specific_user = null;
//        if(cursor != null && cursor.moveToFirst()){
//            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
//            String email = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_EMAIL_ID));
//            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PHONE_NUMBER));
//            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
//            String gender = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_GENDER));
//            specific_user = new User(user_id,username,phoneNumber,email,password,gender);
//            cursor.close();
//        }
//        db.close();
//        return specific_user;
//    }
    public User getUserByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] projection = {
                Usertable.COLUMN_USER_ID,
                Usertable.COLUMN_USERNAME,
                Usertable.COLUMN_PHONE_NUMBER,
                Usertable.COLUMN_EMAIL_ID,
                Usertable.COLUMN_PASSWORD,
                Usertable.COLUMN_GENDER
        };

        String selection = Usertable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { name };

        Cursor cursor = db.query(
                Usertable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USERNAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_EMAIL_ID));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PASSWORD));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_GENDER));

            user = new User(userId, username, phoneNumber, email, password, gender);

            cursor.close();
        }

        db.close();

        return user;
    }
    public User getUserByNumber(String phone_number){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;


        String[] projection = {
                Usertable.COLUMN_USER_ID,
                Usertable.COLUMN_USERNAME,
                Usertable.COLUMN_PHONE_NUMBER,
                Usertable.COLUMN_EMAIL_ID,
                Usertable.COLUMN_PASSWORD,
                Usertable.COLUMN_GENDER
        };


        String selection = Usertable.COLUMN_PHONE_NUMBER + " = ?";
        String[] selectionArgs = { phone_number };


        Cursor cursor = db.query(
                Usertable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USERNAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_EMAIL_ID));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PASSWORD));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_GENDER));

            user = new User(userId, username, phoneNumber, email, password, gender);

            cursor.close();
        }

        db.close();

        return user;

    }
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {Usertable.COLUMN_USER_ID};
        String selection = Usertable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(Usertable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }
    public boolean isPhoneNumberExists(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {Usertable.COLUMN_USER_ID};
        String selection = Usertable.COLUMN_PHONE_NUMBER + " = ?";
        String[] selectionArgs = {phoneNumber};
        Cursor cursor = db.query(Usertable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }
//    public long addUserAppInfo(long userId, String appName, String appPassword) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(AppCredentials.COLUMN_USER_ID, userId);
//        values.put(AppCredentials.COLUMN_APP_NAME, appName);
//        values.put(AppCredentials.COLUMN_APP_PASSWORD, appPassword);
//        long newRowId = db.insert(AppCredentials.TABLE_NAME, null, values);
//        db.close();
//        return newRowId;
//    }

//    public List<AppCredentials> getUserAppInfo(long userId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<AppCredentials> userAppInfos = new ArrayList<>();
//        String[] projection = {
//                AppCredentials.COLUMN_APP_NAME,
//                AppCredentials.COLUMN_APP_PASSWORD
//        };
//        String selection = AppCredentials.COLUMN_USER_ID + "=?";
//        String[] selectionArgs = {String.valueOf(userId)};
//        Cursor cursor = db.query(
//                AppCredentials.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                String appName = cursor.getString(cursor.getColumnIndexOrThrow(AppCredentials.COLUMN_APP_NAME));
//                String appPassword = cursor.getString(cursor.getColumnIndexOrThrow(AppCredentials.COLUMN_APP_PASSWORD));
//                AppCredentials appCredentials = new AppCredentials(userId, appName, appPassword);
//                userAppInfos.add(appCredentials);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//        db.close();
//        return userAppInfos;
//    }

// Potentially useful in future.
}



