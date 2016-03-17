package com.xmpp.xmppprueba;

import android.content.Intent;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by david on 16/03/16.
 */
public class XmppApp extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        startService(new Intent(this, ConnectXmpp.class));
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}
