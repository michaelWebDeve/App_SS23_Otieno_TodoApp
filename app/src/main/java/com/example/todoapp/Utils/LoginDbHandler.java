package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.UserSingleton;

public class LoginDbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static String NAME = "UserTodoDB"; //database name
    static final String USER_TABLE = "user"; // table name
    private static final String ID = "id";
    private static final String EMAIL = "email"; //Spaltename
    private static final String PASSWORD = "password";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL + " TEXT UNIQUE, " + PASSWORD + " TEXT)";

    private SQLiteDatabase loginDb;
    UserSingleton userSingleton= UserSingleton.getInstance();

    public LoginDbHandler(Context context) {
        super(context, NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//on upgrade drop table
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE); // erst wird alte Tabelle gelöscht
        onCreate(db);
    }

    public void openDatabase() {
        loginDb = this.getWritableDatabase(); // initialisiert db objekt um es für oncreate oder onUpdate zu nutzen
    }

    public Boolean insertUser(UserSingleton userSingleton) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EMAIL, userSingleton.getEmail());
        cv.put(PASSWORD, userSingleton.getPassword());
        long result = loginDb.insert(USER_TABLE, null, cv);
        if (result == -1) return false;
        else //überprüft, ob result den Wert -1 hat. Wenn dies der Fall ist,
            // bedeutet es, dass das Einfügen fehlgeschlagen ist
            return true;
    }




    public void deleteUser(int id) {
        openDatabase();
        loginDb.delete(USER_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
    public int getUserId(String userMail) {
        openDatabase();
        SQLiteDatabase MyDB = this.getWritableDatabase();

        userMail = String.valueOf(userSingleton.getEmail());
        Cursor cursor = MyDB.rawQuery("SELECT id FROM user WHERE email = ?", new String[]{userMail});
        int userId= 0; // Initialisierung der Variablen

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0); // Wert aus der ersten Spalte (Index 0) des Cursors erhalten
        }

        cursor.close();

        return userId;
    }


    public Boolean checkEmail(UserSingleton userSingleton) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String userMail = userSingleton.getEmail();
        Cursor cursor = MyDB.rawQuery("Select * from user where email = ?", new String[]{userMail});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkEmailPassword(UserSingleton userSingleton) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String userMail = userSingleton.getEmail();
        String userPw = userSingleton.getPassword();
        Cursor cursor = MyDB.rawQuery("Select * from user where email = ? and password = ?", new String[]{userMail, userPw});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

}

