package com.xmpp.xmppprueba;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChatMessageListener, ChatManagerListener {

    private static final String PASSWORD = "qwerty";
    private XMPPTCPConnection connection;
    EditText tvUser, tvMessage, etTargetUser;
    private Chat chatWithUser;
    private RelativeLayout registerContainer;
    private RelativeLayout messagingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new myAsyncConnection().execute();
        tvMessage = (EditText) findViewById(R.id.etMessage);
        tvUser = (EditText) findViewById(R.id.etName);
        etTargetUser = (EditText) findViewById(R.id.etTargetUser);
        registerContainer = (RelativeLayout) findViewById(R.id.rlRegisterContainer);
        messagingContainer = (RelativeLayout) findViewById(R.id.rlChatMessagesContainer);
    }

    @Override
    public void processMessage(Chat chat, Message message) {

    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        chatWithUser = chat;
        chatWithUser.addMessageListener(this);
    }

    class myAsyncConnection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Create the configuration for this new connection
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
//            configBuilder.setUsernameAndPassword("admin", "davinchy");
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            configBuilder.setResource("Android");
            configBuilder.setServiceName("david-a665");
            configBuilder.setHost("192.168.0.15");
            configBuilder.setPort(5222);
            configBuilder.setConnectTimeout(30000);

            connection = new XMPPTCPConnection(configBuilder.build());
            try {
                connection.connect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            // Disconnect from the server
//            connection.disconnect();
            return null;
        }
    }


    void prepareForChat() {
        registerContainer.setVisibility(View.GONE);
        messagingContainer.setVisibility(View.VISIBLE);
    }

    public void login(View view) throws IOException, SmackException {
        AccountManager accountManager = AccountManager.getInstance(connection);
        try {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("email", "result@mail.com");
            properties.put("name", "tadaaa");
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(tvUser.getText().toString(), PASSWORD, properties);
            connection.login(tvUser.getText().toString(), PASSWORD);
            Presence p = new Presence(Presence.Type.available, "I am busy", 42, Presence.Mode.dnd);
            connection.sendStanza(p);
            ChatManager.getInstanceFor(connection).addChatListener(this);
            Toast.makeText(MainActivity.this, "Logueado", Toast.LENGTH_SHORT).show();
            prepareForChat();

            UserSearchManager usm = new UserSearchManager(connection);
            Form searchForm = usm.getSearchForm("search.david-a665");
            Form answerForm = searchForm.createAnswerForm();

            UserSearch userSearch = new UserSearch();
            answerForm.setAnswer("Username", true);

            answerForm.setAnswer("search", "*");

            ReportedData data = userSearch.sendSearchForm(connection, answerForm, "search." + connection.getServiceName());

            if (data.getRows() != null) {
                System.out.println("not null");
                List<ReportedData.Row> it = data.getRows();
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }


    public void sendMessage(View view) {
        if (chatWithUser == null) {
            chatWithUser = ChatManager.getInstanceFor(connection).createChat(etTargetUser.getText().toString());
        }
        try {
            Message m = new Message();
            m.setType(Message.Type.chat);
            m.setFrom(connection.getUser());
            m.setTo(chatWithUser.getParticipant());
            m.addExtension(DeliveryReceipt.from(m));
            m.setBody(tvMessage.getText().toString());
            chatWithUser.sendMessage(m);
            DeliveryReceiptManager manager = DeliveryReceiptManager.getInstanceFor(connection);
            manager.autoAddDeliveryReceiptRequests();
            manager.addReceiptReceivedListener(new ReceiptReceivedListener() {
                @Override
                public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {

                }
            });
            DeliveryReceiptRequest.from(m);
            connection.sendStanza(m);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


}
