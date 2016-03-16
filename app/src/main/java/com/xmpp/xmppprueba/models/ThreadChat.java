package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

/**
 * Created by david on 16/03/16.
 */
public class ThreadChat extends SugarRecord {
    public String key;
    public String targetUserId;

    public ThreadChat() {
    }
}
