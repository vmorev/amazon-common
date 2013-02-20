package com.github.vmorev.amazon;

import com.amazonaws.services.sqs.model.ListQueuesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.collection.IsIn.isIn;
import static org.junit.Assert.*;

/**
 * User: Valentin_Morev
 * Date: 19.02.13
 */
public class SQSQueueTest {
    protected static Random random;
    private SQSQueue testQueue;

    @BeforeClass
    public static void setUpClass() {
        random = new Random(System.currentTimeMillis());
    }

    @Before
    public void setUp() {
        String modifier = "-" + random.nextLong();
        testQueue = new SQSQueue("test" + modifier);
        testQueue.createQueue();
    }

    @After
    public void cleanUp() {
        testQueue.deleteQueue();
    }

    @Test
    public void testListQueues() throws IOException {
        String queueUrl = testQueue.getUrl();
        List<String> queueUrls = SQSQueue.getSQS().listQueues(new ListQueuesRequest(testQueue.getName())).getQueueUrls();
        assertThat(queueUrl, isIn(queueUrls));
    }

    @Test
    public void testSendReceiveOne() throws Exception {
        SampleBean bean = SampleBean.getSampleBean();

        testQueue.sendMessage(bean);

        final List<SampleBean> objects = new ArrayList<>();
        testQueue.receiveMessages(1, 1, SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                objects.add(obj);
            }
        });

        assertEquals(1, objects.size());
        assertEquals(bean, objects.get(0));
    }

    @Test
    public void testSendReceiveMany() throws Exception {
        List<SampleBean> beans = new ArrayList<>();
        beans.add(SampleBean.getSampleBean());
        beans.add(SampleBean.getSampleBean());
        beans.add(SampleBean.getSampleBean());
        beans.add(SampleBean.getSampleBean());
        beans.add(SampleBean.getSampleBean());

        for (SampleBean bean : beans)
            testQueue.sendMessage(bean);

        final List<SampleBean> objects = new ArrayList<>();
        final long[] size = new long[1];
        do {
            size[0] = 0;
            testQueue.receiveMessages(1, 5, SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
                public void process(SampleBean obj) throws Exception {
                    objects.add(obj);
                    size[0]++;
                }
            });
        } while (size[0] > 0);

        assertEquals(5, objects.size());
        for (SampleBean bean : objects)
            assertTrue(beans.contains(bean));
    }

}
