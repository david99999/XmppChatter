package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by david on 16/03/16.
 */
public class ThreadChat extends SugarRecord implements Serializable {
    public String key;
    public String targetUserId;

    public ThreadChat() {
    }
}
