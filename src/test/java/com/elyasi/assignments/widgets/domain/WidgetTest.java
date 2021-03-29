package com.elyasi.assignments.widgets.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WidgetTest {


    @Test
    void givenZNull_thenIncrementZ_assertZero() {
        // given
        Widget widget = new Widget();


        // then
        widget.incrementZ();

        // assert
        assertNull(widget.getZ());
    }

    @Test
    void givenZ_thenIncrementZ_assertIncrement() {
        // given
        Widget widget = new Widget();
        widget.setZ(5);

        // then
        widget.incrementZ();

        // assert
        assertEquals(6, widget.getZ());
    }

    @Test
    void givenXYWidthHeight_thenGetArea_assertArea() {
        // given
        Widget widget = Widget.builder()
                .height(20)
                .width(30)
                .x(10)
                .y(5)
                .build();

        // then
        Area area = widget.area();

        // assert
        assertEquals(widget.getX(), area.getStart().x);
        assertEquals(widget.getY(), area.getStart().y);
        assertEquals(widget.getX() + widget.getWidth(), area.getEnd().x);
        assertEquals(widget.getY() + widget.getHeight(), area.getEnd().y);
    }
}