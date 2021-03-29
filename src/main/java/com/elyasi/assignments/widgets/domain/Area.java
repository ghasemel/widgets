package com.elyasi.assignments.widgets.domain;

import lombok.*;

import java.awt.*;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@NoArgsConstructor
public class Area {

    private Point start;
    private Point end;

    public Area(Point start, Point end) {
        setStart(start);
        setEnd(end);
    }

    public void setStart(Point start) {
        this.start = start;
        checkAndSwapIfStartXIsGreaterThanEndX();
    }

    public void setEnd(Point end) {
        this.end = end;
        checkAndSwapIfStartXIsGreaterThanEndX();
    }

    /**
     * this method helps to keep:
     * start point on left bottom corner
     * end point on top right corner
     */
    private void checkAndSwapIfStartXIsGreaterThanEndX() {
        if (end == null || start == null)
            return;

        // swap if start.x is bigger
        if (start.x > end.x) {
            Point tmp = start;
            start = end;
            end = tmp;
        }

        // start point in left bottom corner
        // end point in top right corner
        if (start.y > end.y) {
            int shiftY = start.y - end.y;
            start.y -= shiftY;
            end.y += shiftY;
        }
    }

    public boolean contains(Area sub) {
        if (this.start == null || this.end == null || sub.start == null || sub.end == null)
            return false;

        if (this.start.x > sub.start.x || this.start.y > sub.start.y)
            return false;

        return this.end.x >= sub.end.x && this.end.y >= sub.end.y;
    }
}
