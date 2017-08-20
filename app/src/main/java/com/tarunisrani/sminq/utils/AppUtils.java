package com.tarunisrani.sminq.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.tarunisrani.sminq.listeners.ServiceConnectionListener;
import com.tarunisrani.sminq.services.BackendService;

import static com.tarunisrani.sminq.utils.AppConstant.INTENT_KEY_BUNDLE;

/**
 * Created by tarunisrani on 2/14/17.
 */

public class AppUtils {
    private static BackendService mService = null;
    private static ServiceConnectionListener mListener;

    public static BackendService getService(Context context) {
        if(mService == null){
            mService = new BackendService(context);
        }
        return mService;
    }

    public static void setService(BackendService mService) {
        AppUtils.mService = mService;
    }

    public static String generateUniqueKey(Context context){
        long currentTimeMillis = System.currentTimeMillis();
        String timeStamp = String.valueOf(currentTimeMillis);
        String user = new SharedPreferrenceUtil().fetchUserID(context);
        String key = user + "_" + timeStamp;
        return key;
    }

    public static String convertSimpleStringToJWTString(String jsonData) throws Exception
    {
//        String key = "-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCZCsHQt4wzbce9\\nz36raQsyrKDc3xL0xLvdvN4j4op/uRuJ3t7NhHDbIX27YdGsSPnmuhIuW0G5Oteu\\ngxzZVMb8nJqqhBQQzyZeMYbVQgdQ3oFbcMSejQL0ngubsaYac9c8U0cDE/oVlSL4\\nIxLfyv5XohNJDPsPy141uZK+uYmBccwhF7EPgqrz/IYzoDCPLNd8ICDrhQ6VRs3l\\nIvKKUKJDFIS5rPgBBKQDwXlRV7ipd6lnklpLxRpw1wZw4LPheqIIOYba5w+UVP01\\nVWbWaEQ0ESlRFUbRdd9AkDKx1qzJqAc5xleQqqSSDLQ5bDrwhYg23tszVzCLdyD7\\nSfIoybanAgMBAAECggEADBd3fMAjyaRw1aRhQ7XG9rgA9bKCUuiAV2KmwnsRqkm7\\nzUbYn7BJImchAf8Wi+AwLnShHxyCstndVKJzUxCJsEfHSJWq6DEVvPd4ybyvy0np\\nOopMXqKRizY5gJGmqAxjAxcpUyru5WmlG6nZwejN86qQj4MBCxxsUx5YY81Q+fMe\\ngHiKwc7DbdIrHakxSeT6wb73vZWbk3DV9OPJXfzRhqV9SsEplKlNZLlGE5J4FWst\\n1xFlO0poQGSpmqkjahdWlXrtQ4l1wEpJNY78aCKSr4JcRSFCpL0gWA2NEeHmVhCq\\nqKValUiYkKyS3RYe6GDNmx9AnQVoP0IHWB2NTcg1kQKBgQDG8j5C6Il2rWDfhFNm\\nN3pRoCUkLregK1IhC3h5kqF5HT8Fch3mOlfwHWy4s071pXBJFdhDMHC5R3S2YZZC\\nJhAjZCWJ6HU5Aq1TH8ZHPOWmrwC2DRuUxqsyUZQHbgsA33d+8gjn1kJM0uHWhzBG\\ncp1J/P/kLRl7PI36OtfrhjbRewKBgQDE7m1LOviMvpG3gLZY36TYY1xPeQDKnfI7\\npXhT5hMNr4YANPJtWPkiWOZc55Lp+t6TVDyYzaC8yKJjFAAVN/epals+YfIFjJw4\\nFVoG0wKGeglw0Zg2C/1Y7RyLzLI/pKCgV49lgxNQRJlWcgMywvCMvYlZGYWH/QZK\\nigRll7iZxQKBgQCHjZran/K5yByPb2wb44UWhNWJt8MqARpupZAq1bvr9/9uyHiS\\n3AH6+zGPD62nNlgxlA7MiP7lX0io1Ak7fQ/V7XCebKMiey55ytagx196mRcU9A1s\\n64jOOkNwZIxexeR3vJZQ0O5PxghW0boU5hdByH7ai9gm3fWnH9zq/4HubQKBgQCV\\nUaOVU1V6glTCqtmYcZrwaAZqS/mJqFEvYLvoAZ+Y1Onp5jubJcdhf77L2trOhuWV\\nGF4KDoA+JHLcZSHa9ODYmEgu+N0l0APzklz8p6pWhNn7rTCYambisobeiYruLsmM\\nSlf0RHq0dg8+OJysNJtoW5507KEPB49pnVTQI0ZZcQKBgQCG4uWM2npEQxmJDd1k\\nG4TrHGKpYOQvG8UYLEGHUxOX09nMF3fRx/YlRaYg3rpFmee6tfo9YzxTsQxV1su4\\necxN7fWmQzYaDI5W/aoDOPxmDpQ2pnoDlCLrvpdDpzXfhiyrtq7VSPDWrGBsT0g8\\n74c7l/CHyAb97lk2jFP+/eqEgQ==\\n-----END PRIVATE KEY-----\\n";
//        byte[] data = key.getBytes("UTF-8");
//        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
//        String response = Jwts.builder().setPayload(jsonData).signWith(SignatureAlgorithm.HS256, base64).compact();


        return "";
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void performServiceStartOperation(Context context, ServiceConnectionListener listener){

        mListener = listener;

        if(!isServiceRunning(context, BackendService.class)){
            context.startService(new Intent(context, BackendService.class));
        }

        Intent intent = new Intent(context, BackendService.class);
        context.bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    public static void performUnbindService(Context context){
        try {
            context.unbindService(mConnection);
        }catch (IllegalArgumentException exp){
            exp.printStackTrace();
        }
    }

    private static ServiceConnetionClass mConnection = new ServiceConnetionClass(new ServiceConnectionListener() {
        @Override
        public void onServiceBind(BackendService service) {
            mService = service;
            mListener.onServiceBind(service);
        }
    });

    public static void openNewScreen(Activity activity, Class cls, boolean finish, Bundle bundle){
        Intent intent = new Intent(activity, cls);

        if(bundle != null){
            intent.putExtra(INTENT_KEY_BUNDLE, bundle);
        }
        activity.startActivity(intent);
        if(finish){
            activity.finishAffinity();
        }
    }

    public static void showAlertDialog(Activity activity, String title, String msg, boolean showCancel, DialogInterface.OnClickListener positivelistener, DialogInterface.OnClickListener negativelistener){
        if(activity != null && !activity.isFinishing()){
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle(title);
            alert.setMessage(msg);
            alert.setPositiveButton("YES", positivelistener);
            if(showCancel) {
                alert.setNegativeButton("NO", negativelistener);
            }
            alert.show();
        }
    }

}
