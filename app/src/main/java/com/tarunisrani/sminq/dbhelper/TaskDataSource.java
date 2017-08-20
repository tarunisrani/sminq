package com.tarunisrani.sminq.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarunisrani.sminq.model.Task;

import java.util.ArrayList;


/**
 * Created by tarunisrani on 12/21/16.
 */
public class TaskDataSource {

    public static final String TABLE_NAME = "task";

    public static final String COLUMN_ID = "task_id";
    public static final String COLUMN_ID_SERVER = "task_id_server";
//    public static final String COLUMN_SHEET_ID = "expense_sheet_id";
    public static final String COLUMN_DATE = "task_date";
    public static final String COLUMN_TITLE = "task_title";
    public static final String COLUMN_DETAIL = "task_detail";
//    public static final String COLUMN_GROUP = "expense_group";
//    public static final String COLUMN_AMOUNT = "expense_amount";
//    public static final String COLUMN_EXPENSE_TYPE = "expense_type";
    public static final String COLUMN_TASK_TYPE = "task_type";

    private DatabaseHelper databaseHelper;

    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + "( "
            + COLUMN_ID + " text primary key, "
            + COLUMN_ID_SERVER + " text, "
            + COLUMN_DATE + " text, "
            + COLUMN_TITLE + " text, "
            + COLUMN_DETAIL + " text, "
            + COLUMN_TASK_TYPE + " int "
            +");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CLEAN_TABLE =
            "DELETE FROM " + TABLE_NAME;

    private String[] allColumns = {COLUMN_ID, COLUMN_ID_SERVER, COLUMN_DATE, COLUMN_TITLE, COLUMN_DETAIL, COLUMN_TASK_TYPE};


    private ValueEventListener valueEventListener;


    public TaskDataSource(Context context) {
        databaseHelper = DatabaseHelper.getmInstance(context);
        databaseHelper.executeQuery(DATABASE_CREATE);
    }

    public void dropTable(){
        databaseHelper.executeQuery(SQL_DELETE_ENTRIES);
    }

    public void cleanTable(){
        databaseHelper.executeQuery(SQL_CLEAN_TABLE);
    }

    public boolean createTaskEntry(Task task){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, task.getTask_id());
        values.put(COLUMN_ID_SERVER, task.getServer_task_id());
        values.put(COLUMN_DATE, task.getTask_date());
        values.put(COLUMN_TITLE, task.getTask_title());
        values.put(COLUMN_DETAIL, task.getTask_details());
        values.put(COLUMN_TASK_TYPE, task.getTask_type());

        long insertId = database.insert(TABLE_NAME, null,
                values);
        database.close();
        return insertId != -1 ;
    }

    public int createTaskEntry(ArrayList<Task> taskArrayList){
        int count = 0;
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        for(Task task : taskArrayList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, task.getTask_id());
            values.put(COLUMN_ID_SERVER, task.getServer_task_id());
            values.put(COLUMN_DATE, task.getTask_date());
            values.put(COLUMN_TITLE, task.getTask_title());
            values.put(COLUMN_DETAIL, task.getTask_details());
            values.put(COLUMN_TASK_TYPE, task.getTask_type());
            long insertId = database.insert(TABLE_NAME, null,
                    values);
            count += insertId != -1?1:0;
        }
        database.close();
        return count;
    }

    public boolean updateTaskEntry(Task task){
        Log.i("Updating database", task.toString());

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(COLUMN_ID, task.getTask_id());
        values.put(COLUMN_ID_SERVER, task.getServer_task_id());
        values.put(COLUMN_DATE, task.getTask_date());
        values.put(COLUMN_TITLE, task.getTask_title());
        values.put(COLUMN_DETAIL, task.getTask_details());
        values.put(COLUMN_TASK_TYPE, task.getTask_type());

        long count = database.update(TABLE_NAME,
                values, COLUMN_ID + " = " + DatabaseUtils.sqlEscapeString(task.getTask_id()), null);
        database.close();

        return (count > 0);
    }

    public boolean updateTaskEntryWithServerID(Task task){
        Log.i("Updating database", task.toString());


        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, task.getTask_id());
//        values.put(COLUMN_ID_SERVER, task.getServer_task_id());
        values.put(COLUMN_DATE, task.getTask_date());
        values.put(COLUMN_TITLE, task.getTask_title());
        values.put(COLUMN_DETAIL, task.getTask_details());
        values.put(COLUMN_TASK_TYPE, task.getTask_type());

        long count = 0;
        try {
            database.update(TABLE_NAME,
                    values, COLUMN_ID_SERVER + " = " + DatabaseUtils.sqlEscapeString(task.getServer_task_id()), null);
        }catch (SQLException exp){
            exp.printStackTrace();
        }
        database.close();

        return count > 0;
    }

    /*public ArrayList<Task> getTaskItems(String sheet_id){
        ArrayList<Task> taskArrayList = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                allColumns, COLUMN_SHEET_ID + " = " + DatabaseUtils.sqlEscapeString(sheet_id), null,
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            taskArrayList.add(new Task(cursor));
            cursor.moveToNext();
        }
        database.close();
        cursor.close();
        return taskArrayList;
    }*/

    private void removeSync(long sheet_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(TABLE_NAME);
        if(valueEventListener!=null) {
            reference.child(sheet_id + "").removeEventListener(valueEventListener);
        }

    }

    public ArrayList<Task> getTaskItems(){
        ArrayList<Task> taskArrayList = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            taskArrayList.add(new Task(cursor));
            cursor.moveToNext();
        }
        database.close();
        cursor.close();
        return taskArrayList;
    }

    public boolean removeTaskEntry(Task task){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long count = database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getTask_id())});
        database.close();

        return count > 0;
    }

    public boolean removeTaskEntry(ArrayList<Task> task_list){
        long count = 0;
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        for(Task task : task_list){
            count += database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getTask_id())});
        }
        database.close();
        return count > 0;
    }

    public boolean isTaskEntryExist(Task task){
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                allColumns, COLUMN_ID + " = " + DatabaseUtils.sqlEscapeString(task.getTask_id()), null,
                null, null, null);

        boolean exist = cursor.getCount()>0;

        database.close();
        cursor.close();
        return exist;
    }
}
