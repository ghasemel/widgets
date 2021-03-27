package com.miro.assignments.widgets.converter;

import com.miro.assignments.widgets.domain.Area;
import com.miro.assignments.widgets.domain.Widget;
import com.miro.assignments.widgets.dto.WidgetDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * Created by Ghasem on 27/03/2021
 */
@Component
public class StringToAreaConverter implements Converter<String, Area> {

    @Override
    public Area convert(String stringValue) {
        Area area = new Area();

        String[] split = stringValue.split(",");
        if (split.length == 2) {
            area.setStart(extractPoint(split[0]));
            area.setEnd(extractPoint(split[1]));
        }
        return area;
    }

    private Point extractPoint(String s) {
        String[] point = s.split(":");
        if (point.length == 2)
            return new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]));
        return new Point();
    }
}
