package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.TaskModel;
import com.example.todoapp.Model.UserSingleton;


import java.util.ArrayList;
import java.util.List;


public class TaskDBHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static String NAME = "UserTodoDB"; //database name
    private static final String TODO_TABLE = "todo"; // table name
    private static final String ID = "id";
    private static final String TASKS = "tasks"; //Spaltename
    private static final String STATUS = "status";// checked or not
    private static final String USER_ID = "user_id";
    private static final String USER_TABLE = "user";

    private static final String FRAGMENT_ID = "fragment_id";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASKS + " TEXT, " +
            STATUS + " INTEGER, " +
            FRAGMENT_ID + " INTEGER NOT NULL, " +
            USER_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "))";

    // user als foreign key in der Todo Tabelle
    private SQLiteDatabase taskDb;
    UserSingleton userSingleton = UserSingleton.getInstance();


    public TaskDBHandler(Context context) {
        super(context, NAME, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
        openDatabase();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//on upgrade drop table
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE); // erst wird alte Tabelle gelöscht
        onCreate(db);
    }
    public void openDatabase() {
        taskDb = this.getWritableDatabase(); // initialisiert db objekt um es für oncreate oder onUpdate zu nutzen
    }
    public boolean todoTableExists(String tableName) {
        //Um zweite Todo table zu erstellen hat die Ikrementierung der VERSION nicht funktioniert
        //Diese Methode fragt die Datenbank nach Infos über die todo-Tabelle
        // Falls todo nicht existiert ist die Anfrage fehlerhaft und die onCreate Methode
        // kann in Todo1Fragment aufgerufen werden
        taskDb = this.getReadableDatabase();
        Cursor cursor = taskDb.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        //PRAGMA = SQLlite Version von DESCRIBE
        boolean tableExists = false;

        if (cursor != null) {
            tableExists = cursor.getCount() > 0;
            cursor.close();
        }
        return tableExists;
    }
    public Boolean insertTask(int userId, TaskModel task) {

        openDatabase();
        int id = getId(userSingleton); // user id setten
        userSingleton.setId(id);
        ContentValues cv = new ContentValues();
        cv.put(TASKS, task.getTask());
        cv.put(STATUS, 0);
        cv.put(FRAGMENT_ID, task.getFragment_id());
        cv.put(USER_ID, userSingleton.getId()); // user_id einfügen
        long result = taskDb.insert(TODO_TABLE, null, cv);
        if (result == -1) return false;
        else //überprüft, ob result den Wert -1 hat. Wenn dies der Fall ist,
            // bedeutet es, dass das Einfügen fehlgeschlagen ist
            return true;
    }

    @SuppressLint("Range")
    public List<TaskModel> getAllTasks(int userId, int fragmentId) {
        openDatabase();
        List<TaskModel> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TODO_TABLE + " WHERE " + USER_ID + " = " + userId + " AND " + FRAGMENT_ID + " = " + fragmentId, null);

        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                task.setId(cursor.getInt(0));
                task.setTask(cursor.getString(1));
                task.setStatus(cursor.getInt(2));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public int getId(UserSingleton userSingleton) {
        openDatabase();
        SQLiteDatabase MyDB = this.getWritableDatabase();

        String userMail = userSingleton.getEmail();
        Cursor cursor = MyDB.rawQuery("SELECT id FROM user WHERE email = ?", new String[]{userMail});
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0); // Wert aus der ersten Spalte (Index 0) des Cursors erhalten
        }

        cursor.close();
        return id;
    }

    public void updateTask(int taskId, String task) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASKS, task);
        taskDb.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        openDatabase();
        taskDb.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(taskId)});
    }


    public void deleteAllTask(int userId) {
        openDatabase();
        taskDb.delete(TODO_TABLE, USER_ID + "=?", new String[]{String.valueOf(userId)});
    }
}



