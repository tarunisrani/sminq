package com.tarunisrani.sminq.listeners;

import org.json.JSONObject;

/**
 * Created by tarunisrani on 4/21/17.
 */

public interface NetworkResponseListener {
    void onSuccess(JSONObject jsonObject);
    void onFail();
}
