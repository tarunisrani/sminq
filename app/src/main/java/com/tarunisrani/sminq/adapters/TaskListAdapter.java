package com.tarunisrani.sminq.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tarunisrani.sminq.R;
import com.tarunisrani.sminq.listeners.ExpenseListClickListener;
import com.tarunisrani.sminq.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by tarunisrani on 12/22/16.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    public static final int MENU_OPTION_EDIT = 0;
    public static final int MENU_OPTION_REMOVE = 1;

    private ArrayList<Task> mList = new ArrayList<>();
    private ExpenseListClickListener mListener;
    private Context mContext;

    public TaskListAdapter(Context context){
        mContext = context;
    }

    public void setClickListener(ExpenseListClickListener listener){
        this.mListener = listener;
    }

    public void addExpense(Task task){
        mList.add(task);
    }

    public void updateTask(Task task){
        Task task1 = getItemById(task);
        if(task1 !=null){
            task1.setTask_type(task.getTask_type());
            task1.setTask_date(task.getTask_date());
            task1.setTask_title(task.getTask_title());
            task1.setTask_details(task.getTask_details());
        }
    }

    private Task getItemById(Task task){
        for (Task exp: mList){
            if(exp.getTask_id() == task.getTask_id()){
                return exp;
            }
        }
        return null;
    }

    public void sortList(){
        Collections.sort(mList, new CustomComparator());
    }

    public void setExpenseList(ArrayList<Task> list){
        mList = list;
    }

    public ArrayList<Task> getExpenseList(){
         return mList;
    }

    public Task getItem(int position){
        return mList.get(position);
    }

    public Task removeItem(int position){
        return mList.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Task task = mList.get(position);
        holder.setDetails(task);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(position, holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) {
                    mListener.onItemClick(position, holder.itemView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public void setDetails(Task task){
            this.task_title.setText(task.getTask_title());
            this.task_item_date.setText(task.getTask_date());
            this.task_item_detail.setText(task.getTask_details());
        }

        private TextView task_title;
        private TextView task_item_detail;
        private TextView task_item_date;


        public ViewHolder(View itemView) {
           super(itemView);
            this.task_title = (TextView) itemView.findViewById(R.id.task_item_title);
            this.task_item_date = (TextView) itemView.findViewById(R.id.task_item_date_value);
            this.task_item_detail = (TextView) itemView.findViewById(R.id.task_item_detail);

        }
    }

    public class CustomComparator implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            long code = 0;
            try {
                DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
                Date date1 = df.parse(o1.getTask_date());
                Date date2 = df.parse(o2.getTask_date());
                code = date1.compareTo(date2);
                if(code == 0 && o1.getServer_task_id()!=null && o2.getServer_task_id()!=null){
                    code = o1.getServer_task_id().compareToIgnoreCase(o2.getServer_task_id());
                    if(code == 0){
                        code = o1.getTask_id().compareToIgnoreCase(o2.getTask_id());

                    }
                }
            } catch (ParseException exp){
                exp.printStackTrace();
            }

            return (int)code;
        }
    }
}
