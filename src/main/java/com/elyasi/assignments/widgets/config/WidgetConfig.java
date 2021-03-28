package com.elyasi.assignments.widgets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.elyasi.assignments.widgets.constant.GlobalConstant.WIDGET_LOCK_BEAN;

/**
 * Created by taaelgh1 on 28/03/2021
 */
@Configuration
public class WidgetConfig {

    @Bean(WIDGET_LOCK_BEAN)
    public ReadWriteLock widgetReadWriteLock() {
        return new ReentrantReadWriteLock(true);
    }
}
