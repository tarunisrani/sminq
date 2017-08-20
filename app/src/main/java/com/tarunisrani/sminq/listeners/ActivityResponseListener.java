package com.tarunisrani.sminq.listeners;

/**
 * Created by tarunisrani on 4/21/17.
 */

public interface ActivityResponseListener<T> {
    void onSuccess(T result);
    void onFail();
}
