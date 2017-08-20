package com.tarunisrani.sminq.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.tarunisrani.sminq.dbhelper.TaskDataSource;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by tarunisrani on 12/20/16.
 */
public class Task implements Parcelable {

    protected Task(Parcel in) {
        task_date = in.readString();
        task_title = in.readString();
        task_details = in.readString();
        task_type = in.readString();
        server_task_id = in.readString();
        task_id = in.readString();
        completed = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private String task_date;
    private String task_title;
    private String task_details = "";
    private String task_id;
    private String server_task_id;
    private String task_type;

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_details() {
        return task_details;
    }

    public void setTask_details(String task_details) {
        this.task_details = task_details;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getServer_task_id() {
        return server_task_id;
    }

    public void setServer_task_id(String server_task_id) {
        this.server_task_id = server_task_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    private boolean completed = false;

    public Task(){

    }

    public Task(Cursor cursor){
        this.task_id = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_ID));
        this.server_task_id = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_ID_SERVER));
        this.task_date = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_DATE));
        this.task_title = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_TITLE));
        this.task_details = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_DETAIL));
        this.task_type = cursor.getString(cursor.getColumnIndex(TaskDataSource.COLUMN_TASK_TYPE));
    }

    public void updateExpense(Task task){
        this.task_id = task.getTask_id();
        this.server_task_id = task.getServer_task_id();
        this.task_date = task.getTask_date();
        this.task_title = task.getTask_title();
        this.task_details = task.getTask_details();
        this.task_type = task.getTask_type();
        this.completed = task.isCompleted();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task_date);
        dest.writeString(task_title);
        dest.writeString(task_details);
        dest.writeString(task_type);
        dest.writeString(server_task_id);
        dest.writeString(task_id);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Task){
            if(((Task)o).getServer_task_id()!=null && this.server_task_id !=null) {
                return ((Task) o).getServer_task_id().equalsIgnoreCase(this.server_task_id);
            }else{
                return ((Task) o).getTask_id() == this.task_id;
            }
        }
        return super.equals(o);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("task_id = "+this.task_id +"\t");
        builder.append("server_task_id = "+this.server_task_id +"\t");
        builder.append("task_date = "+this.task_date +"\t");
        builder.append("task_title = "+this.task_title +"\t");
        builder.append("task_details = "+this.task_details +"\t");
        builder.append("task_type = "+this.task_type +"\t");

        return builder.toString();
    }

    public JSONObject toJson(String user_id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TaskDataSource.COLUMN_ID, this.task_id);
        jsonObject.put(TaskDataSource.COLUMN_DATE, this.task_date);
        jsonObject.put(TaskDataSource.COLUMN_DATE, this.task_title);
        jsonObject.put(TaskDataSource.COLUMN_DETAIL, this.task_title);
        jsonObject.put(TaskDataSource.COLUMN_TASK_TYPE, this.task_type);
        jsonObject.put("user_id", user_id);
        return jsonObject;
    }
}
