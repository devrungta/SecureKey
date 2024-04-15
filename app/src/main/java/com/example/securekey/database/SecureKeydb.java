package com.example.securekey.database;

import static com.example.securekey.database.Usertable.COLUMN_PASSWORD;
import static com.example.securekey.database.Usertable.COLUMN_USERNAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.securekey.User;

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

public void addUserAsync(String username, String phone_number, String email, String password, String gender, UserAddListener listener) {
    new AddUserTask(listener).execute(username, phone_number, email, password, gender);
}

    private class AddUserTask extends AsyncTask<String, Void, Long> {
        private UserAddListener listener;

        public AddUserTask(UserAddListener listener) {
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(String... params) {
            // Perform database insertion operation in background
            String username = params[0];
            String phoneNumber = params[1];
            String email = params[2];
            String password = params[3];
            String gender = params[4];

            SQLiteDatabase db = SecureKeydb.this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(Usertable.COLUMN_PHONE_NUMBER, phoneNumber);
            values.put(Usertable.COLUMN_EMAIL_ID, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(Usertable.COLUMN_GENDER, gender);
            return db.insert(Usertable.TABLE_NAME, null, values);
        }

        @Override
        protected void onPostExecute(Long newRowId) {
            if (listener != null) {
                if (newRowId != -1) {
                    listener.onUserAdded(newRowId);
                } else {
                    listener.onUserAddFailed();
                }
            }
        }
    }

    public interface UserAddListener {
        void onUserAdded(long newRowId);
        void onUserAddFailed();
    }
    public void getUserByNumberAsync(String phoneNumber, GetUserByNumberListener listener) {
        new GetUserByNumberTask(listener).execute(phoneNumber);
    }

    private class GetUserByNumberTask extends AsyncTask<String, Void, User> {
        private GetUserByNumberListener listener;

        public GetUserByNumberTask(GetUserByNumberListener listener) {
            this.listener = listener;
        }

        @Override
        protected User doInBackground(String... params) {
            String phoneNumber = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
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
            String[] selectionArgs = { phoneNumber };

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
                String email = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_EMAIL_ID));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_PASSWORD));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(Usertable.COLUMN_GENDER));

                user = new User(userId, username, phoneNumber, email, password, gender);

                cursor.close();
            }

            db.close();

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (listener != null) {
                listener.onUserRetrieved(user);
            }
        }
    }

    public void isPhoneNumberExistsAsync(String phoneNumber, PhoneNumberExistsListener listener) {
        new IsPhoneNumberExistsTask(listener).execute(phoneNumber);
    }

    private class IsPhoneNumberExistsTask extends AsyncTask<String, Void, Boolean> {
        private PhoneNumberExistsListener listener;

        public IsPhoneNumberExistsTask(PhoneNumberExistsListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String phoneNumber = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
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

        @Override
        protected void onPostExecute(Boolean exists) {
            if (listener != null) {
                listener.onPhoneNumberExistsCheckCompleted(exists);
            }
        }
    }

    public interface GetUserByNumberListener {
        void onUserRetrieved(User user);
    }

    public interface PhoneNumberExistsListener {
        void onPhoneNumberExistsCheckCompleted(boolean exists);
    }
    public void isUsernameExistsAsync(String username, UsernameExistsListener listener) {
        new UsernameExistsAsyncTask(listener).execute(username);
    }

    public void getUserByNameAsync(String name, GetUserByNameListener listener) {
        new GetUserByNameAsyncTask(listener).execute(name);

    }

    public interface UsernameExistsListener {
        void onUsernameExistsCheckCompleted(boolean exists);
    }

    public interface GetUserByNameListener {
        void onGetUserByNameCompleted(User user);
        void onGetUserByNameFailed();
    }

    private class UsernameExistsAsyncTask extends AsyncTask<String, Void, Boolean> {
        private UsernameExistsListener listener;

        public UsernameExistsAsyncTask(UsernameExistsListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
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

        @Override
        protected void onPostExecute(Boolean exists) {
            if (listener != null) {
                listener.onUsernameExistsCheckCompleted(exists);
            }
        }
    }

    private class GetUserByNameAsyncTask extends AsyncTask<String, Void, User> {
        private GetUserByNameListener listener;

        public GetUserByNameAsyncTask(GetUserByNameListener listener) {
            this.listener = listener;
        }

        @Override
        protected User doInBackground(String... params) {
            String name = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
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
            Cursor cursor = db.query(Usertable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
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

        @Override
        protected void onPostExecute(User user) {
            if (listener != null) {
                if (user != null) {
                    listener.onGetUserByNameCompleted(user);
                } else {
                    listener.onGetUserByNameFailed();
                }
            }
        }
    }
// In SecureKeydb class

    public void getUserByUsernameAsync(String username, GetUserByUsernameListener listener) {
        new GetUserByUsernameAsyncTask(listener).execute(username);
    }

    private class GetUserByUsernameAsyncTask extends AsyncTask<String, Void, User> {
        private GetUserByUsernameListener listener;

        public GetUserByUsernameAsyncTask(GetUserByUsernameListener listener) {
            this.listener = listener;
        }

        @Override
        protected User doInBackground(String... params) {
            String username = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
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
            String[] selectionArgs = { username };
            Cursor cursor = db.query(Usertable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                long userId = cursor.getLong(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USER_ID));
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

        @Override
        protected void onPostExecute(User user) {
            if (listener != null) {
                if (user != null) {
                    listener.onUserRetrieved(user);
                } else {
                    listener.onUserNotFound();
                }
            }
        }
    }

    public interface GetUserByUsernameListener {
        void onUserRetrieved(User user);
        void onUserNotFound();
    }
    public void getUserIdByPhoneNumberAsync(String phoneNumber, GetUserIdByPhoneNumberListener listener) {
        new GetUserIdByPhoneNumberAsyncTask(listener).execute(phoneNumber);
    }

    private class GetUserIdByPhoneNumberAsyncTask extends AsyncTask<String, Void, Long> {
        private GetUserIdByPhoneNumberListener listener;

        public GetUserIdByPhoneNumberAsyncTask(GetUserIdByPhoneNumberListener listener) {
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(String... params) {
            String phoneNumber = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
            long userId = -1;

            String[] projection = {
                    Usertable.COLUMN_USER_ID
            };

            String selection = Usertable.COLUMN_PHONE_NUMBER + " = ?";
            String[] selectionArgs = { phoneNumber };

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
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USER_ID));
                cursor.close();
            }

            db.close();

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            if (listener != null) {
                listener.onUserIdRetrieved(userId);
            }
        }
    }

    public interface GetUserIdByPhoneNumberListener {
        void onUserIdRetrieved(long userId);
    }
    public void getUserIdByUsernameAsync(String username, GetUserIdByUsernameListener listener) {
        new GetUserIdByUsernameAsyncTask(listener).execute(username);
    }

    private class GetUserIdByUsernameAsyncTask extends AsyncTask<String, Void, Long> {
        private GetUserIdByUsernameListener listener;

        public GetUserIdByUsernameAsyncTask(GetUserIdByUsernameListener listener) {
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(String... params) {
            String username = params[0];
            SQLiteDatabase db = SecureKeydb.this.getReadableDatabase();
            long userId = -1;

            String[] projection = {
                    Usertable.COLUMN_USER_ID
            };

            String selection = Usertable.COLUMN_USERNAME + " = ?";
            String[] selectionArgs = { username };

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
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(Usertable.COLUMN_USER_ID));
                cursor.close();
            }

            db.close();

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            if (listener != null) {
                listener.onUserIdRetrieved(userId);
            }
        }
    }

    public interface GetUserIdByUsernameListener {
        void onUserIdRetrieved(long userId);
    }




}




