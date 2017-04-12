package com.xiumi.qirenbao.widget.swich;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianbailu on 2017/3/11.
 */

public class SpringConfigRegistry {

    private static final SpringConfigRegistry INSTANCE = new SpringConfigRegistry(true);

    public static SpringConfigRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<SpringConfig, String> mSpringConfigMap;

    /**
     * constructor for the SpringConfigRegistry
     */
    SpringConfigRegistry(boolean includeDefaultEntry) {
        mSpringConfigMap = new HashMap<SpringConfig, String>();
        if (includeDefaultEntry) {
            addSpringConfig(SpringConfig.defaultConfig, "default config");
        }
    }

    /**
     * add a SpringConfig to the registry
     *
     * @param springConfig SpringConfig to add to the registry
     * @param configName name to give the SpringConfig in the registry
     * @return true if the SpringConfig was added, false if a config with that name is already
     *    present.
     */
    public boolean addSpringConfig(SpringConfig springConfig, String configName) {
        if (springConfig == null) {
            throw new IllegalArgumentException("springConfig is required");
        }
        if (configName == null) {
            throw new IllegalArgumentException("configName is required");
        }
        if (mSpringConfigMap.containsKey(springConfig)) {
            return false;
        }
        mSpringConfigMap.put(springConfig, configName);
        return true;
    }

    /**
     * remove a specific SpringConfig from the registry
     * @param springConfig the of the SpringConfig to remove
     * @return true if the SpringConfig was removed, false if it was not present.
     */
    public boolean removeSpringConfig(SpringConfig springConfig) {
        if (springConfig == null) {
            throw new IllegalArgumentException("springConfig is required");
        }
        return mSpringConfigMap.remove(springConfig) != null;
    }

    /**
     * retrieve all SpringConfig in the registry
     * @return a list of all SpringConfig
     */
    public Map<SpringConfig, String> getAllSpringConfig() {
        return Collections.unmodifiableMap(mSpringConfigMap);
    }

    /**
     * clear all SpringConfig in the registry
     */
    public void removeAllSpringConfig() {
        mSpringConfigMap.clear();
    }
}

