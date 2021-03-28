package com.elyasi.assignments.widgets.converter;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.model.WidgetDto;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Ghasem on 27/03/2021
 */
public class WidgetToWidgetDtoConverter implements Converter<Widget, WidgetDto> {

    @Override
    public WidgetDto convert(Widget widget) {
        return WidgetDto.builder()
                .id(widget.getId())
                .x(widget.getX())
                .y(widget.getY())
                .z(widget.getZ())
                .width(widget.getWidth())
                .height(widget.getHeight())
                .build();
    }
}
