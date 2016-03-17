package com.xmpp.xmppprueba;


import android.os.AsyncTask;
import android.util.Log;

import com.xmpp.xmppprueba.models.ThreadChat;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ankit on 10/3/2015.
 */
public class XmppHelper implements ChatMessageListener, ChatManagerListener {
    private static final String LOCAL_TAG = XmppHelper.class.getSimpleName();

    private static final String DOMAIN = "MYXMPP";
    private static final String HOST = "192.81.217.199";
    private static final String RESOURCE = "Android";
    XMPPTCPConnection connection;
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
            try {
                ThreadChat chat = DBUtils.getChatWhitUser(targetUserId);
                Chat newChat;
                if (chat != null) {
                    newChat = chatmanager.getThreadChat(chat.key);
                } else {
                    newChat = chatmanager.createChat(targetUserId);
                    DBUtils.storeNewChat(newChat);
                }
                newChat.sendMessage(content);
            } catch (SmackException.NotConnectedException e) {
                Log.e(LOCAL_TAG, e.getMessage());
            }
        }
    }

    public void createAccount(String name, String email, String password, String username) {
        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            HashMap<String, String> properties = new HashMap<>();
            properties.put("email", email);
            properties.put("name", name);
            accountManager.createAccount(username, password, properties);
            Log.i(LOCAL_TAG, "account created");
        } catch (SmackException.NoResponseException | SmackException.NotConnectedException | XMPPException e) {
            Log.e(LOCAL_TAG, e.getMessage());
        }
    }

    public ArrayList<User> getUsers() {

        ArrayList<User> users = new ArrayList<>();
        try {
            UserSearchManager usm = new UserSearchManager(connection);
            Form searchForm = usm.getSearchForm("search." + connection.getServiceName());
            Form answerForm = searchForm.createAnswerForm();

            UserSearch userSearch = new UserSearch();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", "*");

            ReportedData data = null;
            data = userSearch.sendSearchForm(connection, answerForm, "search." + connection.getServiceName());

            if (data.getRows() != null) {
                System.out.println("not null");
                List<ReportedData.Row> datas = data.getRows();
                for (ReportedData.Row userInAnswer : datas) {
                    User user = new User();
                    user.email = userInAnswer.getValues("email").get(0);
                    user.name = userInAnswer.getValues("name").get(0);
                    user.username = userInAnswer.getValues("username").get(0);
                    users.add(user);
                }
            }
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void login(String userName, String passWord) {
        try {
            connection.login(userName, passWord);
            User user = new User();
            user.name = userName;
            user.password = passWord;
            user.save();
        } catch (XMPPException | SmackException | IOException e) {
            Log.e(LOCAL_TAG, e.getMessage());
        }
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        ThreadChat previousChat = DBUtils.getChatWhitKey(chat.getThreadID());
        if (previousChat == null) {
            DBUtils.storeNewChat(chat);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        BusHelper.getInstance().post(message);
    }


    //Connection Listener to check connection state
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(XMPPConnection connection) {
            Log.d(LOCAL_TAG, "Connected!");
            if (chatmanager == null) {
                chatmanager = ChatManager.getInstanceFor(connection);
                chatmanager.addChatListener(XmppHelper.this);
            }

            if (User.count(User.class) > 0) {
                User user = User.listAll(User.class).get(0);
                login(user.name, user.password);
            }
            BusHelper.getInstance().post(XmppHelper.this.connection);
        }

        @Override
        public void connectionClosed() {
            Log.d(LOCAL_TAG, "ConnectionCLosed!");
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            Log.d(LOCAL_TAG, "ConnectionClosedOn Error!");
            connectConnection();
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
            if (chatmanager == null) {
                chatmanager = ChatManager.getInstanceFor(connection);
                chatmanager.addChatListener(XmppHelper.this);
            }
            if (User.count(User.class) > 0) {
                User user = User.listAll(User.class).get(0);
                login(user.name, user.password);
            }
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d(LOCAL_TAG, "Authenticated!");
        }
    }
}