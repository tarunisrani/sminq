package com.tarunisrani.sminq.utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.tarunisrani.sminq.listeners.ServiceConnectionListener;
import com.tarunisrani.sminq.services.BackendService;


/**
 * Created by tarunisrani on 3/1/17.
 */

public class ServiceConnetionClass implements ServiceConnection {

    private ServiceConnectionListener mListener;

    public ServiceConnetionClass(ServiceConnectionListener listener){
        this.mListener = listener;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BackendService.MyBinder b = (BackendService.MyBinder) service;
        if(mListener!=null){
            mListener.onServiceBind(b.getService());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
