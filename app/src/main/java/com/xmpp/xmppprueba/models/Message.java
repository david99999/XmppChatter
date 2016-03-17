package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

/**
 * Created by david on 16/03/16.
 */
public class Message extends SugarRecord {
    public String body;
    public String targetUser;
    public String user;
    public String threadId;

    public Message() {
    }
}
