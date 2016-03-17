package com.xmpp.xmppprueba;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xmpp.xmppprueba.models.User;

import java.util.ArrayList;

public class ContactsActivity extends BaseActivity implements BaseActivity.BoundServiceListener{

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ArrayList<User> users;
    private RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        toolbar = (Toolbar) findViewById(R.id.tbContacts);
        setSupportActionBar(toolbar);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        fab = (FloatingActionButton) findViewById(R.id.fbContacts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadContacts();
            }
        });
        listener = this;
    }

    private void loadContacts() {
        users = mService.getHelper().getUsers();
        rvContacts.setAdapter(new ContactsAdapter(users));
    }

    @Override
    public void OnServiceConnected() {
        loadContacts();
    }
}
