package com.github.vmorev.amazon;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.util.BinaryUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * User: Valentin_Morev
 * Date: 14.02.13
 */
public class SQSQueue extends AmazonService {
    private AmazonSQS sqs;
    private String url;

    public SQSQueue() {
        super();
    }

    public SQSQueue(String name) {
        super();
        setName(name);
    }

    public SQSQueue withName(String name) {
        setName(name);
        return this;
    }

    public AmazonSQS getSQS() {
        if (sqs == null)
            sqs = new AmazonSQSClient(getCredentials(), new ClientConfiguration());
        return sqs;
    }

    public List<String> listQueues() {
        return listQueues(null);
    }

    public List<String> listQueues(String prefix) {
        return getSQS().listQueues(new ListQueuesRequest(prefix)).getQueueUrls();
    }

    public void createQueue() {
        if (!isQueueExists())
            getSQS().createQueue(new CreateQueueRequest().withQueueName(name));
    }

    public void deleteQueue() {
        getSQS().deleteQueue(new DeleteQueueRequest(getUrl()));
    }

    public void sendMessage(Object obj) throws IOException {
        getSQS().sendMessage(new SendMessageRequest(getUrl(), BinaryUtils.toBase64(new ObjectMapper().writeValueAsBytes(obj))));
    }

    public <T> void receiveMessages(int visibilityTimeout, int numberOfMessages, Class<T> clazz, final ListFunc<T> func) throws IOException {
        ReceiveMessageResult result = receiveMessage(visibilityTimeout, numberOfMessages);
        for (Message m : result.getMessages()) {
            T obj = decodeMessage(m, clazz);
            try {
                func.process(obj);
                deleteMessage(m.getReceiptHandle());
            } catch (Exception e) {
                //just do not delete message in case of failure
            }
        }
    }

    protected ReceiveMessageResult receiveMessage(int visibilityTimeout, int numberOfMessages) {
        ReceiveMessageRequest request = new ReceiveMessageRequest(getUrl());
        if (visibilityTimeout >= 0)
            request.setVisibilityTimeout(visibilityTimeout);
        if (numberOfMessages > 0)
            request.setMaxNumberOfMessages(numberOfMessages);
        return getSQS().receiveMessage(request);
    }

    protected void deleteMessage(String receiptHandle) {
        getSQS().deleteMessage(new DeleteMessageRequest(getUrl(), receiptHandle));
    }

    protected <T> T decodeMessage(Message m, Class<T> clazz) throws IOException {
        String mBody = m.getBody();
        if (!mBody.startsWith("{")) {
            mBody = new String(BinaryUtils.fromBase64(mBody));
        }
        return new ObjectMapper().readValue(mBody, clazz);
    }

    protected String getUrl() {
        if (url == null)
            url = getSQS().getQueueUrl(new GetQueueUrlRequest(name)).getQueueUrl();
        return url;
    }

    protected boolean isQueueExists() {
        List<String> urls = getSQS().listQueues().getQueueUrls();
        for (String queueUrl : urls)
            if (queueUrl.equals(url))
                return true;
        return false;
    }
}
