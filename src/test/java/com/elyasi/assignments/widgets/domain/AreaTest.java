package com.elyasi.assignments.widgets.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

    private Area main;

    /**
     * here are the definition of areas
     *
     *                   y
     *                   ^
     *                   |
     *                   |
     *        zone 2     |     zone 1
     *                   |
     * ------------------|------------------> x
     *                   |
     *                   |
     *        zone 3     |     zone 4
     *                   |
     *                   |
     */

    @BeforeEach
    void setUp() {
        main = new Area();
    }

    @DisplayName("given start points of sub-area and main-area in zone 3 and end points in zone 2 -> assert contains")
    @Test
    void givenSubAndMainStartInZone3AndEndInZone2_thenContains_assertContains() {
        // given
        main.setStart(new Point(-50, -50));
        main.setEnd(new Point(-5, 50));

        Area sub = new Area(new Point(-30, -30), new Point(-10, 20));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertTrue(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 3 and end points in zone 2 -> assert does not contain")
    @Test
    void givenSubAndMainStartInZone3AndEndInZone2_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(-30, -50));
        main.setEnd(new Point(-5, 50));

        Area sub = new Area(new Point(-50, -50), new Point(-10, 20));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 2 and end points in zone 3 -> assert contains")
    @Test
    void givenSubAndMainStartInZone2AndEndInZone3_thenContains_assertContains() {
        // given
        main.setStart(new Point(-5, 50));
        main.setEnd(new Point(-50, -50));

        Area sub = new Area(new Point(-10, 20), new Point(-30, -30));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertTrue(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 2 and end points in zone 4 -> assert does not contain")
    @Test
    void givenSubAndMainStartInZone2AndEndInZone4_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(-5, 50));
        main.setEnd(new Point(50, -50));

        Area sub = new Area(new Point(-10, 20), new Point(10, -20));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 2 and end points in zone 4 -> assert contain")
    @Test
    void givenSubAndMainStartInZone2AndEndInZone4_thenContains_assertContains() {
        // given
        main.setStart(new Point(-5, 50));
        main.setEnd(new Point(50, -50));

        Area sub = new Area(new Point(-3, 20), new Point(10, -20));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertTrue(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 1 and end points in zone 3 -> assert does not contain")
    @Test
    void givenSubAndMainStartInZone1AndEndInZone3_thenContains_assertContains() {
        // given
        main.setStart(new Point(50, 50));
        main.setEnd(new Point(-50, -50));

        Area sub = new Area(new Point(20, 20), new Point(-20, -80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 1 and end points in zone 1 -> assert does not contain")
    @Test
    void givenSubAndMainStartInZone1AndEndInZone1_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(10, 50));
        main.setEnd(new Point(50, 10));

        Area sub = new Area(new Point(20, 20), new Point(30, 80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @DisplayName("given start points of sub-area and main-area in zone 1 and end points in zone 1 -> assert contains")
    @Test
    void givenSubAndMainStartInZone1AndEndInZone1_thenContains_assertContains() {
        // given
        main.setStart(new Point(50, 10));
        main.setEnd(new Point(10, 50));

        Area sub = new Area(new Point(20, 20), new Point(30, 50));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertTrue(contains);
    }

    @Test
    void givenMainStartNull_thenContains_assertDoesNotContain() {
        // given
        //main.setStart(null);
        main.setEnd(new Point(-50, -50));
        Area sub = new Area(new Point(20, 20), new Point(-20, -80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @Test
    void givenMainEndNull_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(-50, -50));
        //main.setEnd(null);
        Area sub = new Area(new Point(20, 20), new Point(-20, -80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @Test
    void givenSubStartNull_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(-50, -50));
        main.setEnd(new Point(-50, -50));
        Area sub = new Area();
        sub.setEnd(new Point(-20, -80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @Test
    void givenSubEndNull_thenContains_assertDoesNotContain() {
        // given
        main.setStart(new Point(-50, -50));
        main.setEnd(new Point(-50, -50));
        Area sub = new Area();
        sub.setStart(new Point(-20, -80));

        // then
        boolean contains = main.contains(sub);

        // assert
        assertFalse(contains);
    }

    @Test
    void givenEndNullStartNotNull_thenSetStart_assertNoException() {
        // given
        Point point = new Point(-50, -50);


        // assert
        assertDoesNotThrow(() -> {
            // then
            main.setStart(point);
        });
    }

    @Test
    void givenStartNullEndNotNull_thenSetStart_assertNoException() {
        // given
        Point point = new Point(-50, -50);


        // assert
        assertDoesNotThrow(() -> {
            // then
            main.setEnd(point);
        });
    }

    @Test
    void givenStartXLessThanEndX_thenSetEnd_assertNoSwap() {
        // given
        Point start = new Point(20, -50);
        Point end = new Point(30, -50);

        // then
        Area area = new Area(start, end);

        // assert
        assertEquals(start.x, area.getStart().x);
        assertEquals(start.y, area.getStart().y);
        assertEquals(end.x, area.getEnd().x);
        assertEquals(end.y, area.getEnd().y);
    }

    @Test
    void givenStartXGreaterThanEndX_thenSetEnd_assertSwap() {
        // given
        Point start = new Point(40, -50);
        Point end = new Point(30, -50);

        // then
        Area area = new Area(start, end);

        // assert
        assertEquals(end.x, area.getStart().x);
        assertEquals(end.y, area.getStart().y);
        assertEquals(start.x, area.getEnd().x);
        assertEquals(start.y, area.getEnd().y);
    }
}