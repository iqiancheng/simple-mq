package me.qiancheng.simple.mq.message;


import java.io.Serializable;

/**
 * A Message is an immutable object returned by calling the recieve methods on a {@link MessageQueue}.
 *
 * @author 千橙<Joseph>
 * @see MessageQueue#receive()
 */
public interface Message {

    /**
     * Returns the {@link String} body
     *
     * @return the body
     */
    String getBody();

    /**
     * Returns the {@link Serializable} object
     *
     * @return the {@link Serializable} object
     */
    Serializable getObject();

    /**
     * Returns the internal id
     * The internal id is set by the {@link MessageQueue}
     *
     * @return the internal id
     */
    long getId();

    /**
     * @return the time in milliseconds that this message became available (usually the enqueue time)
     */
    long getTime();

    /**
     * @return the string used to detect and automatically act upon similar/same messages in the queue, or null
     */
    String getDuplicateSuppressionKey();

    /**
     * @return the action that was to be performed if this message collided with another in the same message queue, or null only if the duplicate suppression key is also null
     */
    OnCollision getDuplicateSuppressionAction();

    /**
     * @return the amount of time (in milliseconds) that this message would be in a quiet period (immediately after being enqueued), before which it could not be dequeued.
     */
    long getStartDelay();
}
