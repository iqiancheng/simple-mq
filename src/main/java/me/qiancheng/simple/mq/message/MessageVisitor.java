package me.qiancheng.simple.mq.message;

import java.util.List;

/**
 * User: Joseph
 * Date: 2014/04/01
 * Time: 3:34 PM
 */
public
interface MessageVisitor
{
    void visit(List<Message> messages) throws Exception;
}
