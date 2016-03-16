package com.xmpp.xmppprueba;

import android.content.Intent;
import android.widget.Toast;

import com.orm.SugarApp;

/**
 * Created by david on 16/03/16.
 */
public class XmppApp extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        BusHelper.getInstance();
        Intent intent = new Intent(this, ConnectXmpp.class);
        if(startService(intent) != null) {
            Toast.makeText(getBaseContext(), "Service is already running", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
