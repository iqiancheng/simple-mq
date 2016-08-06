package me.qiancheng.simple.mq.message;

import java.util.List;

/**
 * The MessageVisitor
 *
 * @author 千橙<Joseph>
 */
public
interface MessageVisitor
{
    void visit(List<Message> messages) throws Exception;
}
