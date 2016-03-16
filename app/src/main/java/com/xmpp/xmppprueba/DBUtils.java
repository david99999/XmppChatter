package com.xmpp.xmppprueba;

import com.xmpp.xmppprueba.models.ThreadChat;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.chat.Chat;

import java.util.List;

/**
 * Created by david on 16/03/16.
 */
public class DBUtils {
    public static void storeNewChat(Chat newChat) {
        ThreadChat chatForStore = new ThreadChat();
        chatForStore.key = newChat.getThreadID();
        chatForStore.targetUserId = newChat.getParticipant();
        chatForStore.save();
    }

    public static ThreadChat getChatWhitUser(String targetUserId) {
        List<ThreadChat> chat = ThreadChat.find(ThreadChat.class, "target_user_id = ?", targetUserId);
        return (chat != null && chat.size() > 0) ? chat.get(0) : null;
    }

    public static void storeUser(String name, String email, String password, String username) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.password = password;
        user.username = username;
        user.save();
    }

    public static ThreadChat getChatWhitKey(String threadID) {
        List<ThreadChat> chat = ThreadChat.find(ThreadChat.class, "key = ?", threadID);
        return (chat != null && chat.size() > 0) ? chat.get(0) : null;
    }
}
