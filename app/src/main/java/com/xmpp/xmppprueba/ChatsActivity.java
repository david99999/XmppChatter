package com.xmpp.xmppprueba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xmpp.xmppprueba.models.User;

public class ChatsActivity extends BaseActivity {

    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        toolbar = (Toolbar) findViewById(R.id.tbChats);
        setSupportActionBar(toolbar);
        setTitle("Chats de " + User.findAll(User.class).next().name);
        fab = (FloatingActionButton) findViewById(R.id.fbChats);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatsActivity.this, ContactsActivity.class));
            }
        });
    }

}
