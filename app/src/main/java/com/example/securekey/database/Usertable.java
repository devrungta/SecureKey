package com.example.securekey.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Usertable {
    public static final String TABLE_NAME= "user_credentials";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PHONE_NUMBER = "phone";
    public static final String COLUMN_EMAIL_ID = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_GENDER = "gender";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PHONE_NUMBER + " TEXT,"
            + COLUMN_EMAIL_ID + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_GENDER + " TEXT"
            + ")";


}
