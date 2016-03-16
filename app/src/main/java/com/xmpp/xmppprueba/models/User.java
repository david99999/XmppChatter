package com.xmpp.xmppprueba.models;

import com.orm.SugarRecord;

/**
 * Created by david on 16/03/16.
 */
public class User extends SugarRecord{
    public String name;
    public String email;
    public String username;
    public String password;

    public User(){}
}
