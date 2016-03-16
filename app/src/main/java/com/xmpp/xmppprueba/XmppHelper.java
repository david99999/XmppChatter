package com.xmpp.xmppprueba;


import android.os.AsyncTask;
import android.util.Log;

import com.xmpp.xmppprueba.models.ThreadChat;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Ankit on 10/3/2015.
 */
public class XmppHelper {
    private static final String LOCAL_TAG = XmppHelper.class.getSimpleName();

    private static final String DOMAIN = "MYXMPP";
    private static final String HOST = "192.81.217.199";
    private static final String RESOURCE = "Android";
    AbstractXMPPConnection connection;
    ChatManager chatmanager;
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();

    public void init() {
        Log.i(LOCAL_TAG, "Initializing!");
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        configBuilder.setResource(RESOURCE);
        configBuilder.setServiceName(DOMAIN);
        configBuilder.setHost(HOST);
        connection = new XMPPTCPConnection(configBuilder.build());
        connection.addConnectionListener(connectionListener);
    }

    public void disconnectConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connectConnection() {
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... arg0) {
                try {
                    connection.connect();
                } catch (IOException | SmackException | XMPPException e) {
                    Log.e(LOCAL_TAG, e.getMessage());
                }
                return null;
            }
        };
        connectionThread.execute();
    }


    public void sendMsg(String targetUserId, String content) {
        if (connection.isConnected() && connection.isAuthenticated()) {
            if (chatmanager == null) {
                chatmanager = ChatManager.getInstanceFor(connection);
            }
            try {
                ThreadChat chat = DBUtils.getChatWhitUser(targetUserId);
                Chat newChat;
                if (chat != null){
                    newChat = chatmanager.getThreadChat(chat.key);
                }else {
                    newChat = chatmanager.createChat(targetUserId);
                    DBUtils.storeNewChat(newChat);
                }
                newChat.sendMessage(content);
            } catch (SmackException.NotConnectedException e) {
                Log.e(LOCAL_TAG, e.getMessage());
            }
        }
    }

    public void login(String userName, String passWord) throws Exception {
        try {
            connection.login(userName, passWord);
        } catch (XMPPException | SmackException | IOException e) {
            Log.e(LOCAL_TAG, e.getMessage());
        }
    }


    //Connection Listener to check connection state
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {
            Log.d(LOCAL_TAG, "Connected!");
            BusHelper.getInstance().post(connection);
        }

        @Override
        public void connectionClosed() {
            Log.d(LOCAL_TAG, "ConnectionCLosed!");
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            Log.d(LOCAL_TAG, "ConnectionClosedOn Error!");
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log.d(LOCAL_TAG, "Reconnectingin " + arg0);
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            Log.d(LOCAL_TAG, "ReconnectionFailed!");
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d(LOCAL_TAG, "ReconnectionSuccessful");
            BusHelper.getInstance().post(connection);
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d(LOCAL_TAG, "Authenticated!");
        }
    }
}