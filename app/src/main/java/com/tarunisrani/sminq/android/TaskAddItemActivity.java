package com.tarunisrani.sminq.android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tarunisrani.sminq.R;
import com.tarunisrani.sminq.model.Task;
import com.tarunisrani.sminq.utils.AppConstant;
import com.tarunisrani.sminq.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskAddItemActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText task_date;
    private EditText task_title;
    private EditText task_details;
    private Spinner task_type_spinner;
    private TextView task_submit;
    private TextView task_reset;
    private TextView task_edit;

    private ArrayList<String> task_type_list;

    private String selected_task_type = "";


    public static final int OPEN_MODE_EDIT = 1;
    public static final int OPEN_MODE_NEW = 2;

    private int open_mode = OPEN_MODE_NEW;

    private String task_id = null;
    private String server_task_id = "";

    private boolean editable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_add_item_layout);

        task_date = (EditText) findViewById(R.id.task_date);
        task_title = (EditText) findViewById(R.id.task_title);
        task_details = (EditText) findViewById(R.id.task_details);
        task_type_spinner = (Spinner) findViewById(R.id.task_type_spinner);
        task_submit = (TextView) findViewById(R.id.task_submit);
        task_reset = (TextView) findViewById(R.id.task_reset);
        task_edit = (TextView) findViewById(R.id.task_edit);

        task_type_list = new ArrayList<>();
        task_type_list.add("Personal");
        task_type_list.add("Office");
        task_type_list.add("Social");

        ArrayAdapter<String> payment_type_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, task_type_list);

        task_type_spinner.setAdapter(payment_type_adapter);
        task_type_spinner.setOnItemSelectedListener(this);


        Intent intent = getIntent();
        if(intent!=null){
            Task task = intent.getParcelableExtra(AppConstant.INTENT_KEY_TASK);
            editable = intent.getBooleanExtra(AppConstant.INTENT_KEY_EDITABLE, true);
            processIntent(task);
            if(editable){
                performEditOperation();
            }
        }

        task_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });

        task_submit.setOnClickListener(this);
        task_edit.setOnClickListener(this);
        task_date.setOnClickListener(this);
        task_reset.setOnClickListener(this);
    }

    private void processIntent(Task task){
        if(task !=null) {

            task_id = task.getTask_id();
            server_task_id = task.getServer_task_id();

            task_date.setText(task.getTask_date());
            task_date.setEnabled(false);
            task_title.setText(task.getTask_title());
            task_title.setEnabled(false);
            task_details.setText(String.valueOf(task.getTask_details()));
            task_details.setEnabled(false);
            task_type_spinner.setSelection(task_type_list.indexOf(task.getTask_type()));
            task_type_spinner.setEnabled(false);

            task_submit.setVisibility(View.GONE);
            task_reset.setVisibility(View.GONE);
            task_edit.setVisibility(View.VISIBLE);
        }

    }

    private void showDatePicker() {

        if(!editable)
            return;

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final String prevDate = task_date.getText().toString();

        Dialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dob_year, int dob_monthOfYear, int dob_dayOfMonth) {
                task_date.setText(String.format("%02d/%02d/%d", dob_dayOfMonth, dob_monthOfYear + 1, dob_year));
            }
        }, year, month, day);
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                task_date.setText(prevDate);
            }
        });

        d.setCancelable(true);

        d.show();
    }

    private void performSubmitOperation(){
        String date = task_date.getText().toString();
        String title = task_title.getText().toString();
        String details = task_details.getText().toString();

        if(date.isEmpty()){
            Toast.makeText(this, "Please enter date!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(title.isEmpty()){
            Toast.makeText(this, "Please enter title!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(details.isEmpty()){
            Toast.makeText(this, "Please enter detail!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!date.isEmpty() && !title.isEmpty() && !details.isEmpty()) {

            Intent intent = new Intent();

            Task task = new Task();
            Log.e("task_id: ", ""+ task_id);
            task.setTask_id(task_id);
            task.setTask_date(date);
            task.setTask_title(title);
            task.setTask_details(details);
            task.setServer_task_id(server_task_id);
            task.setTask_type(selected_task_type);
            intent.putExtra(AppConstant.INTENT_KEY_TASK, task);
            setResult(200, intent);
            finish();
        }
    }

    private void performEditOperation(){
        editable = true;
        task_submit.setVisibility(View.VISIBLE);
        task_reset.setVisibility(View.VISIBLE);
        task_edit.setVisibility(View.GONE);

        task_date.setEnabled(true);
        task_title.setEnabled(true);
        task_details.setEnabled(true);
        task_type_spinner.setEnabled(true);
        task_type_spinner.setEnabled(true);


        open_mode = OPEN_MODE_EDIT;
    }

    private void performResetOperation(){
        task_date.setText("");
        task_title.setText("");
        task_details.setText("");
        task_type_spinner.setSelection(0);
        task_type_spinner.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.task_submit:
                performSubmitOperation();
                break;
            case R.id.task_edit:
                performEditOperation();
                break;
            case R.id.task_reset:
                performResetOperation();
                break;
            case R.id.task_date:
                showDatePicker();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected_item = (String)parent.getSelectedItem();
        switch (parent.getId()){
            case R.id.task_type_spinner:
                selected_task_type = selected_item;
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AppUtils.getService(this).isFirebaseInitialized()){
            AppUtils.getService(this).initializeFirebase();
        }
    }
}
