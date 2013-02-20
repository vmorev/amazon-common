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
    private static AWSCredentials credentials;
    private static AmazonConfig config;

    public static AmazonConfig getConfig() {
        if (config == null)
            config = new AmazonConfig();
        return config;
    }

    protected static AWSCredentials getCredentials() {
        if (credentials == null)
            credentials = new BasicAWSCredentials(getConfig().getValue(ACCESS_KEY), getConfig().getValue(SECRET_KEY));
        return credentials;
    }

    public interface ListFunc<T> {
        void process(T obj) throws Exception;
    }

}
