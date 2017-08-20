package com.tarunisrani.sminq.android;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.tarunisrani.sminq.R;
import com.tarunisrani.sminq.adapters.TaskListAdapter;
import com.tarunisrani.sminq.dbhelper.TaskDataSource;
import com.tarunisrani.sminq.listeners.ExpenseListClickListener;
import com.tarunisrani.sminq.model.Task;
import com.tarunisrani.sminq.services.BackendService;
import com.tarunisrani.sminq.utils.AppConstant;
import com.tarunisrani.sminq.utils.AppUtils;

import java.util.ArrayList;


public class TaskDetailActivity extends AppCompatActivity implements View.OnClickListener, ExpenseListClickListener {

    private TaskListAdapter taskListAdapter;

    private ArrayList<Task> prepopulated_task_list;
    private ArrayList<Task> new_added_task_list = new ArrayList<>();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Task task = intent.getParcelableExtra(AppConstant.INTENT_KEY_TASK);
            String action = intent.getStringExtra(AppConstant.INTENT_KEY_ACTION);

            Log.e("Task "+action, task.toString());

            if(action.equalsIgnoreCase(BackendService.ACTION_ADDED)){
                fetchTaskDetail();
            }else if(action.equalsIgnoreCase(BackendService.ACTION_MODIFIED)){
                fetchTaskDetail();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_layout);


        taskListAdapter = new TaskListAdapter(this);

        TextView button_add_task = (TextView) findViewById(R.id.button_add_task);

        TextView button_delete_task = (TextView) findViewById(R.id.button_delete_task);


        RecyclerView task_list_view = (RecyclerView) findViewById(R.id.task_list_view);

        task_list_view.setAdapter(taskListAdapter);

        task_list_view.setHasFixedSize(true);
        LinearLayoutManager linearLayout =new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        task_list_view.setLayoutManager(linearLayout);
        button_add_task.setOnClickListener(this);

        button_delete_task.setOnClickListener(this);

        taskListAdapter.setClickListener(this);

        fetchTaskDetail();
    }

    private void fetchTaskDetail(){
        TaskDataSource expenseDataSource = new TaskDataSource(this);
        prepopulated_task_list = expenseDataSource.getTaskItems();
        taskListAdapter.setExpenseList(prepopulated_task_list);
        taskListAdapter.sortList();
        taskListAdapter.notifyDataSetChanged();

    }

    private void openAddTaskItemScreen(){
        Intent intent = new Intent(this, TaskAddItemActivity.class);
        startActivityForResult(intent, 100);
    }

    private void openTaskItemDetailScreen(Task task, boolean editable){
        Intent intent = new Intent(this, TaskAddItemActivity.class);
        intent.putExtra(AppConstant.INTENT_KEY_TASK, task);
        intent.putExtra(AppConstant.INTENT_KEY_EDITABLE, editable);
        startActivityForResult(intent, 100);
    }

    private void updateDbOperation(Task task){
        TaskDataSource expenseDataSource = new TaskDataSource(this);
        if(expenseDataSource.updateTaskEntry(task)){
            AppUtils.getService(this).updateTaskEntryOnServer(task);
        }else{
            Log.e("ExpenseDetail", "Error while submitting task");
        }
    }

    private void createNewDbOperation(final Task task){

        TaskDataSource expenseDataSource = new TaskDataSource(this);

        if(expenseDataSource.createTaskEntry(task)){
            AppUtils.getService(this).createTaskEntryOnServer(task);
        }else{
            Log.e("ExpenseDetail", "Error while submitting task");
        }
    }

    private void performDeleteOperation(){

    }

    private void curateExpenseList(ArrayList<Task> expensesList){
        TaskDataSource expenseDataSource = new TaskDataSource(this);
        for(Task task : expensesList){
            Log.d("Server ID", task.getServer_task_id()+"");
            if(containsServerId(prepopulated_task_list, task)){
                Log.d("Updating", task.getServer_task_id()+"");
                int index = prepopulated_task_list.indexOf(task);
                Task temp_task = prepopulated_task_list.get(index);
                temp_task.updateExpense(task);
                if(expenseDataSource.updateTaskEntryWithServerID(temp_task)){
                    Log.d("Update Success", "Successfully updated "+ temp_task.getServer_task_id());
                }else{
                    Log.e("Update Failure", "Failed to update "+ temp_task.getServer_task_id());
                }

            }else{
                Log.d("Adding", task.getServer_task_id()+"");
                prepopulated_task_list.add(task);
                if(expenseDataSource.createTaskEntry(task)){
                    Log.d("Add Success", "Successfully added "+ task.getServer_task_id());
                }else{
                    Log.e("Add Failure", "Failed to add "+ task.getServer_task_id());
                }
            }
        }
    }

    private boolean containsServerId(ArrayList<Task> expensesList, Task task){
        for(Task task1 : expensesList){
            if(task1.getServer_task_id().equalsIgnoreCase(task.getServer_task_id())){
                return true;
            }
        }
        return false;
    }

    private void performEditOperation(int position, TaskListAdapter.ViewHolder viewHolder){
        openTaskItemDetailScreen(taskListAdapter.getItem(position), true);
    }

    private void performRemoveOperation(final int position){
        final Task task = taskListAdapter.getItem(position);

        String message = "Are you sure you want to delete task item";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmation");
        alert.setMessage(message);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(removeExpense(task)){
                    taskListAdapter.removeItem(position);
                    taskListAdapter.notifyDataSetChanged();
                }
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private boolean removeExpense(Task task){
        TaskDataSource expenseDataSource = new TaskDataSource(this);
        if(expenseDataSource.removeTaskEntry(task)){
            Log.e("Remove Task", "SUCCESSFUL");
            AppUtils.getService(this).removeTaskEntryFromServer(task);
            return true;
        }else{
            Log.e("Remove Task", "FAILURE");
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_task:
                openAddTaskItemScreen();
                break;
            case R.id.button_delete_task:
                performDeleteOperation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200 && data!=null){
            Task task = data.getParcelableExtra(AppConstant.INTENT_KEY_TASK);
//            task.setTask_id(sheet.getSheet_id());
            Log.e("task.getTask_id(): ", "" + task.getTask_id());
            if(task.getTask_id()!=null){
                updateDbOperation(task);
                taskListAdapter.updateTask(task);
            }else{
                task.setTask_id(AppUtils.generateUniqueKey(this));
                createNewDbOperation(task);
                new_added_task_list.add(task);
                taskListAdapter.addExpense(task);
            }

            taskListAdapter.sortList();
            taskListAdapter.notifyDataSetChanged();


        }
    }

    @Override
    public void onItemClick(int index, View view) {
        openTaskItemDetailScreen(taskListAdapter.getItem(index), false);
    }


    @Override
    public void onLongClick(int index, TaskListAdapter.ViewHolder viewHolder) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AppUtils.getService(this).isFirebaseInitialized()){
            AppUtils.getService(this).initializeFirebase();
        }
//        registerReceiver(receiver, new IntentFilter(BackendService.FILTER_EXPENSE));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_toolbar, menu);
        return true;
    }
}
