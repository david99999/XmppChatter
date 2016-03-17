package com.xmpp.xmppprueba;

import android.util.Log;

import com.xmpp.xmppprueba.models.ThreadChat;
import com.xmpp.xmppprueba.models.User;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 16/03/16.
 */
public class DBUtils {
    private static final String LOCAL_TAG = DBUtils.class.getSimpleName();

    public static void storeNewChat(Chat newChat) {
        ThreadChat chatForStore = new ThreadChat();
        chatForStore.key = newChat.getThreadID();
        chatForStore.targetUserId = newChat.getParticipant();
        chatForStore.save();
    }

    public static ThreadChat getChatWhitUser(String targetUserId) {
        List<ThreadChat> chat = ThreadChat.find(ThreadChat.class, "target_user_id = ?", targetUserId);
        return (chat != null && chat.size() > 0) ? chat.get(chat.size() - 1) : null;
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
        return (chat != null && chat.size() > 0) ? chat.get(chat.size() - 1) : null;
    }

    public static void storeNewMessage(Message message) {
        com.xmpp.xmppprueba.models.Message newMessage = new com.xmpp.xmppprueba.models.Message();
        newMessage.body = message.getBody();
        newMessage.targetUser = message.getTo();
        newMessage.user = message.getFrom();
        newMessage.threadId = message.getThread();
        newMessage.timestamp = Utils.getCreateTimeStampFromMessage(message);
        newMessage.save();
    }

    public static ArrayList<com.xmpp.xmppprueba.models.Message> getMessagesWithUser(User user) {
        try {
            User me = User.first(User.class);
            return (ArrayList<com.xmpp.xmppprueba.models.Message>) com.xmpp.xmppprueba.models.Message.
                    find(com.xmpp.xmppprueba.models.Message.class,
                    "user like '%?%'  or target_user like '%?%'  or user like '%?%'  or target_user like '%?%",
                    Utils.generateFullUserName(user.username),
                    Utils.generateFullUserName(me.username),
                    Utils.generateFullUserName(user.username),
                    Utils.generateFullUserName(me.username));
        } catch (Exception e) {
            Log.e(LOCAL_TAG, e.getMessage());
            return new ArrayList<>();
        }
    }
}
