package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by david on 16/03/16.
 */
public class User extends SugarRecord implements Serializable {
    public String name;
    public String email;
    public String username;
    public String password;

    public User() {
    }
}
