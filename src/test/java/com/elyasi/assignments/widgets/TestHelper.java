package com.elyasi.assignments.widgets;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ghasem on 27/03/2021
 */
public class TestHelper {
    public static WidgetDto getWidgetDto() {
        return WidgetDto.builder()
                .x(getRandomNum())
                .y(getRandomNum())
                .z(getRandomNum())
                .width(getRandomNumGreaterThanZero())
                .height(getRandomNumGreaterThanZero())
                .build();
    }

    public static WidgetDto getWidgetDtoWithId() {
        return WidgetDto.builder()
                .id(UUID.randomUUID())
                .x(getRandomNum())
                .y(getRandomNum())
                .z(getRandomNum())
                .width(getRandomNumGreaterThanZero())
                .height(getRandomNumGreaterThanZero())
                .build();
    }

    public static Widget getWidget() {
        return getWidget(getRandomNum());
    }

    public static Widget getWidget(Integer z) {
        return Widget.builder()
                .id(UUID.randomUUID())
                .x(getRandomNum())
                .y(getRandomNum())
                .z(z)
                .width(getRandomNumGreaterThanZero())
                .height(getRandomNumGreaterThanZero())
                .build();
    }


    public static int getRandomNum() {
        return ThreadLocalRandom.current().nextInt(-100, 999);
    }

    public static int getRandomNumGreaterThanZero() {
        return ThreadLocalRandom.current().nextInt(100, 999);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WidgetDto asWidgetObj(final String content) {
        try {
            return new ObjectMapper().readValue(content, WidgetDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
