/*
 * Copyright 2008 千橙<Joseph>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.qiancheng.simple.mq.message;

import me.qiancheng.simple.mq.config.PersistentMessageQueueConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.testng.Assert.*;


public class TestPersistentMessageQueue {

    public static final int REVIVE_TIME = 1;
    public static final int REMOVE_TIME = 3;
    private static final String TEST_DATABASE = "test-database";
    private MessageQueue queue;

    @BeforeClass
    public void setUp() {

        PersistentMessageQueueConfig config = new PersistentMessageQueueConfig()
                .setMessageReviveTime(10)
                .setMessageRemoveTime(30)
                .setReviveNonDeletedMessagsThreadDelay(1)
                .setDeleteOldMessagesThreadDelay(1);

        queue = MessageQueueService.getMessageQueue(TEST_DATABASE, config);
        assertFalse(queue.deleted());
    }

    @Test
    public void testQueueService() {
        Collection<String> queues = MessageQueueService.getMessageQueueNames();
        assertTrue(queues.contains(TEST_DATABASE));
    }

    @Test
    public void testMessageQueue() {
        assertNotNull(queue);
        assertTrue(queue.isPersistent());
    }

    @Test
    public void testReviveAndToOld() {

        // check the config startup values for revive and remove
        assertEquals(10, queue.getMessageQueueConfig().getMessageReviveTime());
        assertEquals(30, queue.getMessageQueueConfig().getMessageRemoveTime());

        // test the setter for revive and remove
        queue.getMessageQueueConfig().setMessageReviveTime(1);
        queue.getMessageQueueConfig().setMessageRemoveTime(3);

        // check the new values set above
        assertEquals(1, queue.getMessageQueueConfig().getMessageReviveTime());
        assertEquals(3, queue.getMessageQueueConfig().getMessageRemoveTime());

        queue.send(new MessageInput("hello"));

        MessageInput mi = new MessageInput();
        mi.setObject("there");
        queue.send(mi);

        assertEquals(2, queue.unreadMessageCount());

        // test that I get the same queue instance back
        assertTrue(queue==MessageQueueService.getMessageQueue(TEST_DATABASE));
        assertEquals(2, queue.unreadMessageCount());

        // receive 1 message, but don't delete it.
        {
            Message msg = queue.receive();
            assertEquals(msg.getBody(), "hello");
            assertEquals(1, queue.unreadMessageCount());
        }

        // wait for the received message to be revived.
        wait(2000);

        // now the message should be revived by the "revive" thread and there
        // should therefor be 2 message in the queue again
        assertEquals(2, queue.unreadMessageCount());

        // now wait for the 2 messages to be "to old"
        wait(2000);

        // now the "to old" thread should have remove the 2 messages from the queue
        // and it should be empty.
        assertEquals(0, queue.unreadMessageCount());

        {
            Message msg = queue.receive();
            assertNull(msg);
        }

    }

    private void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void deleteQueueWithNullName() {
        MessageQueueService.deleteMessageQueue(null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getQueueWithNullName() {
        MessageQueueService.getMessageQueue(null);
    }


    @Test
    public void deleteQueueWithWrongName() {
        assertFalse(MessageQueueService.deleteMessageQueue("sdfgfsdgfsd"));
    }

    @AfterClass
    public void tearDown()
    {
        if (queue==null) return;

        assertFalse(queue.deleted());
        MessageQueueService.deleteMessageQueue(TEST_DATABASE);
        assertTrue(queue.deleted());

        Collection<String> queues = MessageQueueService.getMessageQueueNames();
        assertFalse(queues.contains(TEST_DATABASE));
    }

    @Test
    public void reloadSavedQueue()
    {
        queue.shutdown();
        MessageQueueService.forgetMessageQueue(queue);
        setUp();
    }

}
