package com.xmpp.xmppprueba;

import com.squareup.otto.Bus;

/**
 * Created by david on 16/03/16.
 */
public class BusHelper extends Bus{

    private static BusHelper instance;

    protected BusHelper() {
    }

    public static BusHelper getInstance() {
        if (instance == null) {
            instance = new BusHelper();
        }
        return instance;
    }
}
