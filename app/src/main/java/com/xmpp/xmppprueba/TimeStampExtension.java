package com.xmpp.xmppprueba;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampExtension implements ExtensionElement {

    public static final String ELEMENT = "david";
    public static final String NAMESPACE = "http://jabber.org/protocol/timestamps";

    public Long TIME = null;


    public TimeStampExtension() {
        setTime();
    }


    public void setTime() {
        this.TIME = new Date().getTime();
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(TIME));
    }

    public void setTime(Date date) {
        this.TIME = date.getTime();
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.append(">");
        xml.element("time", TIME.toString());
        xml.closeElement(ELEMENT);
        return xml;
    }

    public static class Provider extends ExtensionElementProvider<TimeStampExtension> {

        @Override
        public TimeStampExtension parse(XmlPullParser parser, int initialDepth) {
            Date date;
            try {
                parser.next();
                parser.next();
                date = new Date(Long.parseLong(parser.getText()));
            } catch (Exception ex) {
                date = new Date();
            }
            TimeStampExtension time = new TimeStampExtension();
            time.setTime(date);
            return time;
        }
    }
}