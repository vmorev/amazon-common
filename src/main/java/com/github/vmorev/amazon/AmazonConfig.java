package com.github.vmorev.amazon;

import com.github.vmorev.amazon.utils.ConfigStorage;

import java.util.Map;

/**
 * User: Valentin_Morev
 * Date: 14.02.13
 */
public class AmazonConfig {
    private static final String CONFIG_FILE = "aws.json";

    private Map<String, String> configStorage;

    public AmazonConfig() {
        configStorage = ConfigStorage.loadMap(CONFIG_FILE, true);
    }

    public String getValue(String name) {
        return configStorage.get(name);
    }
}
