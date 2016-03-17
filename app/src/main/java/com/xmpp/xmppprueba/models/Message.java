package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by david on 16/03/16.
 */
public class Message extends SugarRecord implements Serializable{
    public String body;
    public String targetUser;
    public String user;
    public String threadId;

    public Message() {
    }
}
