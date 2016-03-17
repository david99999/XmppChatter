package com.xmpp.xmppprueba;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xmpp.xmppprueba.models.Message;
import com.xmpp.xmppprueba.models.User;

import java.util.ArrayList;

public class MessagingActivity extends BaseActivity implements BaseActivity.BoundServiceListener {

    LinearLayout messagesContainer;
    EditText etMessage;
    User user;

    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        user = (User) getIntent().getExtras().getSerializable("User");
        etMessage = (EditText) findViewById(R.id.etNewMessage);
        messagesContainer = (LinearLayout) findViewById(R.id.llMessagesContainer);
        messages = DBUtils.getMessagesWithUser(user);
    }

    @Override
    public void OnServiceConnected() {

    }

    public void sendMessage(View view) {
        mService.getHelper().sendMsg(user.username, etMessage.getText().toString());
    }
}
