package com.snake.kit.core.iq;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IntrospectionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Yuan on 2016/11/29.
 * Detail
 */

public class TestIQ extends IQ{

    private String var1;
    private String var2;
    private String var3;

    public TestIQ(String var1, String var2, String var3) {
        super("test","test");
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    protected TestIQ(String childElementName) {
        super(childElementName);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.append("<var1>").append(var1).append("</var1>");
        xml.append("<var2>").append(var2).append("</var2>");
        xml.append("<var3>").append(var1).append("</var3>");
        return xml;
    }

    public class TestProvider extends IntrospectionProvider.IQIntrospectionProvider<TestIQ> {

        public TestProvider() {
            super(TestIQ.class);
        }

        @Override
        public TestIQ parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
            return super.parse(parser, initialDepth);
        }
    }
}
