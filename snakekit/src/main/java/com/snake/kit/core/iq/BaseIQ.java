package com.snake.kit.core.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by Yuan on 2016/11/30.
 * Detail 基础IQ包
 */

public abstract class BaseIQ extends IQ{

    private String to;

    protected BaseIQ(String childElementName) {
        super(childElementName);
    }

    protected BaseIQ(String childElementName, String childElementNamespace) {
        super(childElementName, childElementNamespace);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        return null;
    }

}
