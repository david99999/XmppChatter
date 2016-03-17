package com.xmpp.xmppprueba;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.chatstates.ChatState;

/**
 * Created by david on 17/03/16.
 */
public class ChatAndStateWrapper {
    public Chat chat;
    public ChatState state;

    public ChatAndStateWrapper(Chat chat, ChatState state) {
        this.chat = chat;
        this.state = state;
    }
}
