package com.miro.assignments.widgets.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by taaelgh1 on 27/03/2021
 */
@Slf4j
@EnableConfigurationProperties
@ConfigurationProperties("widget.app")
@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class Config {
    private int defaultPageSize;
}
