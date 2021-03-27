package com.miro.assignments.widgets.domain;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.awt.*;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    private Point start;
    private Point end;

    public boolean contains(Area area) {
        if (this.start.x > area.start.x || this.start.y > area.start.y)
            return false;

        return this.end.x >= area.end.x && this.end.y >= area.end.y;
    }
}
