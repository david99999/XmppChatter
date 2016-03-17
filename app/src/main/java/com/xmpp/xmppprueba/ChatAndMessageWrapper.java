package com.xmpp.xmppprueba;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by david on 17/03/16.
 */
public class ChatAndMessageWrapper {
    public Chat chat;
    public Message message;
    public Boolean consumed = false;

    public ChatAndMessageWrapper(Chat chat, Message message) {
        this.chat = chat;
        this.message = message;
    }
}
