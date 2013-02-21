package com.github.vmorev.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

/**
 * User: Valentin_Morev
 * Date: 21.01.13
 */
public abstract class AmazonService {
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static AmazonConfig config;
    private AWSCredentials credentials;
    protected String accessKey;
    protected String secretKey;
    protected String name;

    protected AmazonService() {
        setCredentials(null, null);
    }

    public static AmazonConfig getConfig() {
        if (config == null)
            config = new AmazonConfig();
        return config;
    }

    protected AWSCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(String accessKey, String secretKey) {
        if (accessKey != null) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        } else {
            this.accessKey = getConfig().getValue(ACCESS_KEY);
            this.secretKey = getConfig().getValue(SECRET_KEY);
        }
        credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //get name if url was provided instead of name
        if (name.contains("/"))
            name = name.substring(name.lastIndexOf("/") + 1, name.length());
        this.name = name;
    }

    public interface ListFunc<T> {
        void process(T obj) throws Exception;
    }

}
