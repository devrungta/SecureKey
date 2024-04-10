package com.example.securekey.database;

public class AppCredentials {
    public static final String TABLE_NAME = "app_credentials";
    public static final String COLUMN_CREDENTIAL_ID = "credential_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_APP_NAME = "app_name";
    public static final String COLUMN_APP_PASSWORD = "app_password";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_CREDENTIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_APP_NAME + " TEXT,"
            + COLUMN_APP_PASSWORD + " TEXT,"
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + Usertable.TABLE_NAME + "(" + Usertable.COLUMN_USER_ID + ")"
            + ")";
}
