package com.xmpp.xmppprueba;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;

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

    public Long getTime() {
        return TIME;
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
                date = new Date(parser.getAttributeValue(0));
            } catch (Exception ex) {
                date = new Date();
            }
            TimeStampExtension time = new TimeStampExtension();
            time.setTime(date);
            return time;
        }
    }
}