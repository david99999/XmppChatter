package com.xmpp.xmppprueba;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.xmpp.xmppprueba.models.Message;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateManager;

import java.util.ArrayList;

public class MessagingActivity extends BaseActivity implements BaseActivity.BoundServiceListener, View.OnKeyListener {

    LinearLayout messagesContainer;
    EditText etMessage;
    User user;

    ArrayList<Message> messages;
    private ChatStateManager manager;
    private Chat currentChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        user = (User) getIntent().getExtras().getSerializable("User");
        etMessage = (EditText) findViewById(R.id.etNewMessage);
        messagesContainer = (LinearLayout) findViewById(R.id.llMessagesContainer);
        messages = DBUtils.getMessagesWithUser(user);
        etMessage.setOnKeyListener(this);
        listener = this;
        setTitle("Chat con " + user.username);
        BusHelper.getInstance().register(this);
    }

    @Override
    public void OnServiceConnected() {
        mService.getHelper().createChatWitUser(user.username);
        manager = ChatStateManager.getInstance(mService.getHelper().connection);
    }

    public void sendMessage(View view) {
        mService.getHelper().sendMsg(user.username, etMessage.getText().toString());
    }

    @Subscribe
    public void OnMessageReceived(org.jivesoftware.smack.packet.Message message) {
        Toast.makeText(this, message.getBody(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void OnChatCreated(Chat chat) {
        currentChat = chat;
    }

    @Subscribe
    public void OnChatEvent(ChatAndStateWrapper wrapper) {
        if (currentChat.equals(wrapper.chat)) {
            getSupportActionBar().setSubtitle("Estado: " + wrapper.state.name());
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        try {
            manager.setCurrentState(ChatState.composing, currentChat);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
