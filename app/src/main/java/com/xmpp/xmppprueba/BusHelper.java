package com.xmpp.xmppprueba;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by david on 16/03/16.
 */
public class BusHelper extends Bus {

    private static BusHelper instance;

    protected BusHelper(ThreadEnforcer enforcer) {
        super(enforcer);
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BusHelper.super.post(event);
                }
            });
        }
    }

    public static BusHelper getInstance() {
        if (instance == null) {
            instance = new BusHelper(ThreadEnforcer.ANY);
        }
        return instance;
    }
}
