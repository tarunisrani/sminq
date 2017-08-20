package com.tarunisrani.sminq.listeners;

import android.view.View;

import com.tarunisrani.sminq.adapters.TaskListAdapter;


/**
 * Created by tarunisrani on 12/22/16.
 */
public interface ExpenseListClickListener {
    void onItemClick(int index, View view);
    void onLongClick(int index, TaskListAdapter.ViewHolder viewHolder);
}
