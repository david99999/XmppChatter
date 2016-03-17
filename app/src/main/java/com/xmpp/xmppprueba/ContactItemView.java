package com.xmpp.xmppprueba;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xmpp.xmppprueba.models.User;


/**
 * Created by david on 4/03/16.
 */
public class ContactItemView extends CardView {
    private TextView tvName;

    public ContactItemView(Context context) {
        super(context);
    }

    public ContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvName = (TextView) findViewById(R.id.tvContactName);
    }

    public void setUser(User user) {
        tvName.setText(user.name);
    }

}
