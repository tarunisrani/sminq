package com.tarunisrani.sminq.listeners;


import com.tarunisrani.sminq.services.BackendService;

/**
 * Created by tarunisrani on 12/22/16.
 */
public interface ServiceConnectionListener {
    void onServiceBind(BackendService service);
}
