package com.xmpp.xmppprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class RegisterActivity extends BaseActivity {

    EditText tvName, tvPass, etEmail, etUserName, tvLoginName, tvLoginPass;
    Button btRegister, btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (User.findAll(User.class).hasNext()) {
            goToChatsScreen();
        }
        setContentView(R.layout.activity_register);
        initViews();
        BusHelper.getInstance().register(this);
    }

    @Subscribe
    public void enableForRegistration(XMPPTCPConnection connection) {
        Toast.makeText(this, "Listo para crear usuario", Toast.LENGTH_SHORT).show();
        btRegister.setEnabled(true);
        btLogin.setEnabled(true);
    }

    void initViews() {
        tvLoginPass = (EditText) findViewById(R.id.etLoginPass);
        tvLoginName = (EditText) findViewById(R.id.etLoginUserName);
        tvPass = (EditText) findViewById(R.id.etPass);
        tvName = (EditText) findViewById(R.id.etName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btRegister = (Button) findViewById(R.id.btRegister);
        btLogin = (Button) findViewById(R.id.btLogin);
        if (mService != null && mService.getHelper().connection.isConnected()) {
            enableForRegistration(null);
        }
    }

    public void register(View view) {
        mService.getHelper().createAccount(
                tvName.getText().toString(),
                etEmail.getText().toString(),
                tvPass.getText().toString(),
                etUserName.getText().toString()
        );
        DBUtils.storeUser(
                tvName.getText().toString(),
                etEmail.getText().toString(),
                tvPass.getText().toString(),
                etUserName.getText().toString()
        );
        mService.getHelper().login(
                etUserName.getText().toString(),
                tvPass.getText().toString()
        );
        goToChatsScreen();
    }

    public void login(View view) {
        mService.getHelper().login(
                tvLoginName.getText().toString(),
                tvLoginPass.getText().toString()
        );
        DBUtils.storeUser("", "", tvLoginPass.getText().toString(), tvLoginName.getText().toString());

        goToChatsScreen();
    }

    void goToChatsScreen() {
        startActivity(new Intent(this, ChatsActivity.class));
        finish();
    }
}
