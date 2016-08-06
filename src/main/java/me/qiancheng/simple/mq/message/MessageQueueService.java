package me.qiancheng.simple.mq.message;


import me.qiancheng.simple.mq.config.MessageQueueConfig;
import me.qiancheng.simple.mq.config.PersistentMessageQueueConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The entry point for getting/creating and deleting message queues
 *
 * @author 千橙<Joseph>
 */
public final class MessageQueueService {

    private static final Map<String, MessageQueue> queues = new HashMap<String, MessageQueue>();

    private MessageQueueService() {
        //
    }

    /**
     * If a queue by the name of 'queueName' allready excists - a reference to that queue is returned.
     * If no queue is found - a new "in-memory" queue is created with the name 'queueName' and a default {@link MessageQueueConfig}.
     *
     * @param queueName - the name of the queue
     * @return a "in-memory" message queue
     */
    public static MessageQueue getMessageQueue(String queueName) {
        return getMessageQueue(queueName, false);
    }

    /**
     * If a queue by the name of 'queueName' allready excists - a reference to that queue is returned.
     * If no queue is found and 'Persistent' is set to 'true' - a new "Persistent" queue is created with the name 'queueName' and a
     * default {@link PersistentMessageQueueConfig} or else a "in-memory" queue is created with the name 'queueName' and a default {@link MessageQueueConfig}.
     *
     * @param queueName  - the name of the queue
     * @param persistent - 'true' creates a "persistens" queue, 'false' creates a "in-memory" queue
     * @return a message queue
     */
    public static MessageQueue getMessageQueue(String queueName, boolean persistent) {
        if (persistent) {
            return getMessageQueue(queueName, new PersistentMessageQueueConfig());
        } else {
            return getMessageQueue(queueName, new MessageQueueConfig());
        }
    }

    /**
     * If a queue by the name of 'queueName' allready excists - a reference to that queue is returned.
     * If no queue is found and 'config' is an instance of {@link PersistentMessageQueueConfig}
     * - a new "Persistent" queue is created with the name 'queueName'. Or if 'config' is an instance of
     * {@link MessageQueueConfig} - a new "in-memory" queue is created
     * with the name 'queueName'.
     * <p/>
     * The config object is copied/cloned by the message queue. To get a reference to the copied/cloned
     * config object use {@link MessageQueue#getMessageQueueConfig()}.
     *
     * @param queueName - the name of the queue
     * @param config    - an instance of {@link PersistentMessageQueueConfig}
     *                  or {@link MessageQueueConfig}
     * @return a message queue
     */
    public static MessageQueue getMessageQueue(String queueName, MessageQueueConfig config) {
        if (queueName == null) {
            throw new NullPointerException("The name of the queue cannot be 'null'");
        }

        synchronized (queues) {
            if (queues.containsKey(queueName)) {
                return queues.get(queueName);
            } else {
                MessageQueue newQueue = new MessageQueueImp(queueName, config);

                queues.put(queueName, newQueue);

                return newQueue;
            }
        }
    }

    /**
     * Deletes a message queue and all remaning messages in it.
     * If it is a "Persistent" queue all files is also deleted.
     *
     * @param queueName
     * @return true - if the deletion was successfull
     */
    public static boolean deleteMessageQueue(String queueName) {
        if (queueName == null) {
            throw new NullPointerException("The name of the queue cannot be 'null'");
        }

        synchronized (queues) {
            if (queues.containsKey(queueName)) {
                MessageQueue queue = queues.get(queueName);
                MessageQueueImp mqi = (MessageQueueImp) queue;

                mqi.deleteQueue();
                queues.remove(queueName);

                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Return the names of all exsisting message queues
     *
     * @return all queue names
     */
    public static Collection<String> getMessageQueueNames() {
        return queues.keySet();
    }

    public static
    boolean forgetMessageQueue(MessageQueue queue)
    {
        synchronized (queues)
        {
            return (queues.remove(queue.getQueueName())!=null);
        }
    }

    public static
    boolean forgetMessageQueue(String queueName)
    {
        synchronized (queues)
        {
            return (queues.remove(queueName)!=null);
        }
    }

}
