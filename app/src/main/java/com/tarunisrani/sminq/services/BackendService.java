package com.tarunisrani.sminq.services;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarunisrani.sminq.dbhelper.TaskDataSource;
import com.tarunisrani.sminq.model.Task;
import com.tarunisrani.sminq.utils.SharedPreferrenceUtil;

import java.util.HashMap;

/**
 * Created by tarunisrani on 2/13/17.
 */

public class BackendService {

    private static final String TAG = "BackendService";
    private static final String DB_NAME = "sminq";

    public static final String FILTER_EXPENSE = "com.tarunisrani.dailykharcha.android.backendservice.expense";
    public static final String FILTER_SHEET = "com.tarunisrani.dailykharcha.android.backendservice.sheet";
    public static final String FILTER_GROUP = "com.tarunisrani.dailykharcha.android.backendservice.group";
    public static final String ACTION_ADDED = "ACTION_ADDED";
    public static final String ACTION_MODIFIED = "ACTION_MODIFIED";
    public static final String ACTION_REMOVED = "ACTION_REMOVED";

    private Context mContext;

    private final MyBinder mBinder = new MyBinder();

//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database ;
//    private DatabaseReference global_user_reference;
//    private DatabaseReference global_user_details_reference;
    private DatabaseReference global_database_reference;

    private HashMap<String, ChildEventListener> listenerHashMap = new HashMap<>();

    public BackendService(Context context){
        mContext = context;
    }

    public class MyBinder extends Binder {
        public Intent getmIntent() {
            return mIntent;
        }

        public void setmIntent(Intent mIntent) {
            this.mIntent = mIntent;
        }

        private Intent mIntent;

        public BackendService getService() {
            return BackendService.this;
        }
    }

    public void initializeFirebase(){
        database = FirebaseDatabase.getInstance();

//        global_user_reference = database.getReference(DB_NAME).child("users").child(user_id);
//        global_user_details_reference = database.getReference(DB_NAME).child("users");
//        global_database_reference = database.getReference("dailykharcha").child("databases").child(selected_group_id);
        global_database_reference = database.getReference(DB_NAME).child("databases");
        global_database_reference.keepSynced(true);
    }

    public boolean isFirebaseInitialized(){
        return database!=null && global_database_reference!=null;
    }



    /*private void startExpenseListener(String selected_group_id){
        Log.d("Listener", "Task listener started");
        final DatabaseReference reference = global_database_reference.child(selected_group_id).child("expense");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    Log.e("Task onChildAdded", dataSnapshot.getValue().toString());
                    Task task = dataSnapshot.getValue(Task.class);
                    task.setServer_task_id(dataSnapshot.getKey());
                    addExpenseIfNotExist(task);

                }else{
                    Log.e("Task onChildAdded", "null");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    Log.e("Task onChildChanged", dataSnapshot.getValue().toString());
                    Task task = dataSnapshot.getValue(Task.class);
                    task.setServer_task_id(dataSnapshot.getKey());
                    if(updateExpense(task)) {
                        publishExpenseResults(task, ACTION_MODIFIED);
                    }
                }else{
                    Log.e("Task onChildChanged", "null");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    Log.e("Task onChildRemoved", dataSnapshot.getValue().toString());
                }else{
                    Log.e("Task onChildRemoved", "null");
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    Log.e("Task onChildMoved", dataSnapshot.getValue().toString());
                }else{
                    Log.e("Task onChildMoved", "null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listenerHashMap.put(selected_group_id, childEventListener);

        reference.addChildEventListener(childEventListener);
    }

    private void removeExpenseListener(String selected_group_id) {
        DatabaseReference reference = global_database_reference.child(selected_group_id).child("expense");
        ChildEventListener childEventListener = listenerHashMap.remove(selected_group_id);
        if(childEventListener!=null) {
            reference.removeEventListener(childEventListener);
        }
    }

    private void addExpenseIfNotExist(Task task){
        TaskDataSource expenseDataSource = new TaskDataSource(this);
        if(!expenseDataSource.isTaskEntryExist(task)){
            if(createExpenseEntryInDb(task)){
                publishExpenseResults(task, ACTION_ADDED);
            }
        }
    }*/

    /*private boolean createExpenseEntryInDb(Task task){
        TaskDataSource expenseDataSource = new TaskDataSource(mContext);
        return expenseDataSource.createTaskEntry(task);
    }

    public boolean updateExpense(Task task){
        TaskDataSource expenseDataSource = new TaskDataSource(mContext);
        return expenseDataSource.updateTaskEntry(task);
    }*/

    public void createTaskEntryOnServer(final Task task) {
        String selected_group_id = new SharedPreferrenceUtil().fetchSelectedGroupID(mContext);
        DatabaseReference reference = global_database_reference.child(selected_group_id).child("task").push();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    TaskDataSource expenseDataSource = new TaskDataSource(mContext);
                    Log.e("Task", dataSnapshot.getKey() + "  --  " + dataSnapshot.getValue().toString());
                    task.setServer_task_id(dataSnapshot.getKey());
                    if (expenseDataSource.updateTaskEntry(task)) {
                        Log.d("Update", "Successfully updated server task id");
                    } else {
                        Log.d("Update", "Failed to update server task id");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.setValue(task);
    }

    public void updateTaskEntryOnServer(Task task) {
        String selected_group_id = new SharedPreferrenceUtil().fetchSelectedGroupID(mContext);
        DatabaseReference reference = global_database_reference.child(selected_group_id).child("task").child(task.getServer_task_id());
        reference.setValue(task);
    }

    public void removeTaskEntryFromServer(Task task) {
        String selected_group_id = new SharedPreferrenceUtil().fetchSelectedGroupID(mContext);
        DatabaseReference reference = global_database_reference.child(selected_group_id).child("task").child(task.getServer_task_id());
        reference.setValue(null);
    }

}
