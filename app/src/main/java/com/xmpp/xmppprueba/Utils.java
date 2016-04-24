package com.xmpp.xmppprueba;

import org.jivesoftware.smack.packet.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 17/03/16.
 */
public class Utils {

    public static Long getCreateTimeStampFromMessage(Message message) {
        try {
            return ((TimeStampExtension) message.getExtension(TimeStampExtension.NAMESPACE)).getTime();
        }catch (Exception e){
            return new Date().getTime();
        }
    }
    public static String getFormatedTimestamp(Long timestamp){
        try {
            return new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp));
        }catch (Exception e){
            return "";
        }
    }
    public static String generateFullUserName(String user){
        return (user +  "@" + XmppHelper.DOMAIN + "/" + XmppHelper.RESOURCE).toLowerCase();
    }
}

