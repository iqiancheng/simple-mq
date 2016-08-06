package me.qiancheng.simple.mq.message;

/**
 * The OnCollision Message status.
 *
 * @author 千橙<Joseph>
 */
public enum OnCollision
{
    DROP,    /* new message dies, old messages maintains it's place in the queue */
    DEMOTE,  /* new message dies, but old message is moved to the end of the queue */
    REPLACE, /* old message dies, new message is placed at the end of the queue */
    SWAP,    /* old message dies, but new message takes it's place in the queue (i.e. the queue time) */
    EXCLUDE, /* both messages die */
}
