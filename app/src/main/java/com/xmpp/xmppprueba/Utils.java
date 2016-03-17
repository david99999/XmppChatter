package com.xmpp.xmppprueba;

import org.jivesoftware.smack.packet.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 17/03/16.
 */
public class Utils {

    public static Long getCreateTimeStampFromMessage(Message message) {
        return ((TimeStampExtension) message.getExtension(TimeStampExtension.NAMESPACE)).getTime();
    }
    public static String getFormatedTimestamp(Long timestamp){
        return new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp));
    }
    public static String generateFullUserName(String user){
        return (user +  "@" + XmppHelper.DOMAIN + "/" + XmppHelper.RESOURCE).toLowerCase();
    }
}

