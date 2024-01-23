package com.example.swim_todo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
//Done with aid from https://chat.openai.com

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "tasks";

    //Columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK_TEXT = "task_text";
    private static final String COLUMN_TASK_TAGS = "task_tags";
    private static final String COLUMN_TASK_ISDONE = "task_isdone";
    private static final String COLUMN_TASK_PRIORITY = "task_priority";
    private static final String COLUMN_TASK_DUEDATE = "task_duedate";

    private static String convertDateToISO(long dateinms){
        SimpleDateFormat isodate = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return isodate.format(new Date(dateinms));
        } catch (Exception e) {
            e.printStackTrace();
            return "";                         //TODO: Can change it to common ERROR_STRING constant
        }
    };

    private static long convertDateToms(String dateinISO){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateinISO).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    };

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TASK_TEXT + " TEXT NOT NULL," +
                    COLUMN_TASK_DUEDATE + " DATE NOT NULL," +
                    COLUMN_TASK_PRIORITY + " TEXT NOT NULL DEFAULT 'Low'," +
                    COLUMN_TASK_TAGS + " TEXT NOT NULL," +
                    COLUMN_TASK_ISDONE + " BOOLEAN NOT NULL DEFAULT 'false');";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table initialization
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //FIXME: When you change table structure migrate data (dump>transform>import)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Dodawanie zadania
    public long insertTask(
            String taskText,
            long dueDate,
            String Priority,
            String Tags,
            Boolean isDone
            ) {
        String dueDateFormatted = convertDateToISO(dueDate);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TEXT, taskText);
        values.put(COLUMN_TASK_DUEDATE, dueDateFormatted);
        values.put(COLUMN_TASK_PRIORITY, Priority);
        values.put(COLUMN_TASK_TAGS, Tags);
        values.put(COLUMN_TASK_ISDONE, isDone);

        try {
            long rowId = db.insert(TABLE_NAME, null, values);
            db.close();
            return rowId;
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return -1;
        }
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        int columnIndexText = cursor.getColumnIndex(COLUMN_TASK_TEXT);
        int columnIndexDueDate = cursor.getColumnIndex(COLUMN_TASK_DUEDATE);
        int columnIndexPriority = cursor.getColumnIndex(COLUMN_TASK_PRIORITY);
        int columnIndexTags = cursor.getColumnIndex(COLUMN_TASK_TAGS);
        int columnIndexIsDone = cursor.getColumnIndex(COLUMN_TASK_ISDONE);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(columnIndexId);
            String text = cursor.getString(columnIndexText);
            // Convert Date from SQLITE String format to application epochtime in ms format
            long dueDateInMillis = convertDateToms(cursor.getString(columnIndexDueDate));
            String priority = cursor.getString(columnIndexPriority);
            String tags = cursor.getString(columnIndexTags);
            boolean isDone = cursor.getInt(columnIndexIsDone) == 1;

            Task task = new Task(id, text, tags, priority, dueDateInMillis, isDone, null);
            taskList.add(task);
        }

        cursor.close();
        return taskList;
    }

    public Task getTask(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        Task task = null;

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        int columnIndexText = cursor.getColumnIndex(COLUMN_TASK_TEXT);
        int columnIndexDueDate = cursor.getColumnIndex(COLUMN_TASK_DUEDATE);
        int columnIndexPriority = cursor.getColumnIndex(COLUMN_TASK_PRIORITY);
        int columnIndexTags = cursor.getColumnIndex(COLUMN_TASK_TAGS);
        int columnIndexIsDone = cursor.getColumnIndex(COLUMN_TASK_ISDONE);

        if (cursor.moveToFirst()) {
            long id = taskId;
            String text = cursor.getString(columnIndexText);
            // Convert Date from SQLITE String format to application epochtime in ms format
            long dueDateInMillis = convertDateToms(cursor.getString(columnIndexDueDate));
            String priority = cursor.getString(columnIndexPriority);
            String tags = cursor.getString(columnIndexTags);
            boolean isDone = cursor.getInt(columnIndexIsDone) == 1;

            task = new Task(id, text, tags, priority, dueDateInMillis, isDone, null);
        }

        cursor.close();
        return task;
    }

    public int updateTask(Task task) {
        String dueDateFormatted = convertDateToISO(task.getDueDate());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TEXT, task.getName());
        values.put(COLUMN_TASK_DUEDATE, dueDateFormatted);
        values.put(COLUMN_TASK_TAGS, task.getTags());
        values.put(COLUMN_TASK_PRIORITY, task.getPriority());
        values.put(COLUMN_TASK_ISDONE, task.isDone() ? 1 : 0);

        try {
            return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getID())});
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    public int deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Usunięcie wiersza o zadanym ID
            return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }
}
