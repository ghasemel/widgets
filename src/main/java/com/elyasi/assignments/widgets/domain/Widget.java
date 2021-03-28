package com.elyasi.assignments.widgets.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int z;

    private int width;
    private int height;

    public Widget incrementZ() {
        z++;
        return this;
    }

    //@JsonIgnore
    public Area getArea() {
        return Area.builder()
                .start(new Point(x, y))
                .end(new Point(x + width, y + height))
                .build();
    }
}
