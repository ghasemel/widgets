package com.elyasi.assignments.widgets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.*;
import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Widget {

    @Id
    private UUID id;

    private int x;
    private int y;
    private Integer z;

    private int width;
    private int height;

    public Widget incrementZ() {
        if (z != null)
            z++;
        return this;
    }

    public Area area() {
        return new Area(new Point(x, y), new Point(x + width, y + height));
    }

    public static void test() {

    }
}
