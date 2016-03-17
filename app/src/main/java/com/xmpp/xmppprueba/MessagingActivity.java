package com.xmpp.xmppprueba;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.xmpp.xmppprueba.models.Message;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateManager;

import java.util.ArrayList;

public class MessagingActivity extends BaseActivity implements TextWatcher {

    private static final String LOCAL_TAG = MessagingActivity.class.getSimpleName();
    LinearLayout messagesContainer;
    EditText etMessage;
    User user;

    ArrayList<Message> messages;
    private ChatStateManager manager;
    private Chat currentChat;
    private boolean isCurrentlyTyping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        user = (User) getIntent().getExtras().getSerializable("User");
        etMessage = (EditText) findViewById(R.id.etNewMessage);
        messagesContainer = (LinearLayout) findViewById(R.id.llMessagesContainer);
        messages = DBUtils.getMessagesWithUser(user);
        etMessage.addTextChangedListener(this);
        listener = this;
        setTitle("Chat con " + user.username);
    }

    @Override
    public void OnServiceConnected() {
        mService.getHelper().createChatWitUser(user.username);
        manager = ChatStateManager.getInstance(mService.getHelper().connection);
    }

    public void sendMessage(View view) {
        mService.getHelper().sendMsg(currentChat, etMessage.getText().toString());
        addMessageToList(etMessage.getText().toString(), true);
        etMessage.removeTextChangedListener(this);
        etMessage.getText().clear();
        etMessage.addTextChangedListener(this);
    }

    @Subscribe
    public void OnMessageReceived(ChatAndMessageWrapper wrapper) {
        wrapper.consumed = true;
        if (wrapper.chat.getParticipant().equalsIgnoreCase(user.username + "@" + XmppHelper.DOMAIN + "/" + XmppHelper.RESOURCE)) {
            currentChat = wrapper.chat;
        }
        if (wrapper.message.getBody() != null)
            addMessageToList(wrapper.message.getBody(), false);
    }

    private void addMessageToList(String message, boolean isMyMessage) {
        TextView tvMessage = new TextView(this);
        tvMessage.setBackgroundColor(isMyMessage ? Color.BLUE : Color.LTGRAY);
        tvMessage.setText((isMyMessage ? "yo" : user.username) + ": " + message);
        messagesContainer.addView(tvMessage);
    }

    @Subscribe
    public void OnChatCreated(Chat chat) {
        currentChat = chat;
    }

    @Subscribe
    public void OnChatEvent(ChatAndStateWrapper wrapper) {
        if (wrapper.chat.getParticipant().equalsIgnoreCase(user.username + "@" + XmppHelper.DOMAIN + "/" + XmppHelper.RESOURCE)) {
            currentChat = wrapper.chat;
        }
        if (currentChat.equals(wrapper.chat)) {
            getSupportActionBar().setSubtitle("Estado: " + wrapper.state.name());
        }
    }

    CountDownTimer timer = null;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHelper.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusHelper.getInstance().unregister(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            if (!isCurrentlyTyping) {
                manager.setCurrentState(ChatState.composing, currentChat);
                isCurrentlyTyping = true;
            }
            if (timer != null) {
                timer.cancel();
            }

            timer = new CountDownTimer(1000, 500) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        manager.setCurrentState(ChatState.active, currentChat);
                        isCurrentlyTyping = false;
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (SmackException.NotConnectedException e) {
            Log.e(LOCAL_TAG, e.getMessage());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
