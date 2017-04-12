package com.xiumi.qirenbao.widget.swich;

/**
 * Created by qianbailu on 2017/3/11.
 */

public class SpringSystem extends BaseSpringSystem {

    /**
     * Create a new SpringSystem providing the appropriate constructor parameters to work properly
     * in an Android environment.
     * @return the SpringSystem
     */
    public static SpringSystem create() {
        return new SpringSystem(AndroidSpringLooperFactory.createSpringLooper());
    }

    private SpringSystem(SpringLooper springLooper) {
        super(springLooper);
    }

}