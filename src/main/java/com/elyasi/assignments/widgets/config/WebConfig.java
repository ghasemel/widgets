package com.elyasi.assignments.widgets.config;

import com.elyasi.assignments.widgets.web.converter.StringToAreaConverter;
import com.elyasi.assignments.widgets.web.converter.WidgetDtoToWidgetConverter;
import com.elyasi.assignments.widgets.web.converter.WidgetToWidgetDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Ghasem on 28/03/2021
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToAreaConverter());
        registry.addConverter(new WidgetDtoToWidgetConverter());
        registry.addConverter(new WidgetToWidgetDtoConverter());
    }
}