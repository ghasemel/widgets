package com.elyasi.assignments.widgets.dto;

import com.elyasi.assignments.widgets.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WidgetListDtoTest {

    private WidgetListDto widgetListDto;

    @BeforeEach
    void setUp() {
        widgetListDto = new WidgetListDto();
    }

    @Test
    void givenNullWidgets_thenSetWidgets_assertCount() {
        // given
        List<WidgetDto> list = null;

        // then
        widgetListDto.setWidgets(list);

        //
        assertEquals(0, widgetListDto.getCount());
    }

    @Test
    void givenWidgets_thenSetWidgets_assertCount() {
        // given
        List<WidgetDto> list = List.of(TestHelper.getWidgetDto(), TestHelper.getWidgetDto());

        // then
        widgetListDto.setWidgets(list);

        //
        assertEquals(list.size(), widgetListDto.getCount());
    }

    @Test
    void givenNullWidgetsAndNoneZeroCount_thenSetWidgets_assertCountIsZero() {
        // given
        List<WidgetDto> list = null;
        widgetListDto.setCount(10);

        // then
        widgetListDto.setWidgets(list);

        //
        assertEquals(0, widgetListDto.getCount());
    }
}