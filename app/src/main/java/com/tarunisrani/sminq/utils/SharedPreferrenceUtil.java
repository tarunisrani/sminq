package com.tarunisrani.sminq.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tarunisrani on 2/20/17.
 */

public class SharedPreferrenceUtil {
    public void setUserID(Context context, String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("UID", uid).commit();
    }

    public String fetchUserID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return sharedPreferences.getString("UID", "");
    }

    public void setUserName(Context context, String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("NAME", uid).commit();
    }

    public String fetchUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return sharedPreferences.getString("NAME", "");
    }

    public void setSelectedGroup(Context context, String groupid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("GROUPID", groupid).commit();
    }

    public String fetchSelectedGroupID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return sharedPreferences.getString("GROUPID", "");
    }

    public void setExpenseSheetID(Context context, String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("ExpenseSheetID", id).commit();
    }

    public String fetchExpenseSheetID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ExpenseSheetID", "");
    }


}
