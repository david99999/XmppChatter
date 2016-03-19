package com.xmpp.xmppprueba;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class ConnectXmpp extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        ConnectXmpp getService() {
            // Return this instance of LocalService so clients can call public methods
            return ConnectXmpp.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private XmppHelper xmpp = new XmppHelper();

    public ConnectXmpp() {
    }

    public XmppHelper getHelper() {
        return xmpp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        if (intent != null) {
            xmpp.init();
            xmpp.connectConnection();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        xmpp.disconnectConnection();
        super.onDestroy();
    }
}