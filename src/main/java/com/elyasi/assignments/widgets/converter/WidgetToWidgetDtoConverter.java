package com.elyasi.assignments.widgets.converter;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Component
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


    public List<WidgetDto> convert(List<Widget> widgetList) {
        List<WidgetDto> dtos = new ArrayList<>(widgetList.size());
        widgetList.forEach(w -> dtos.add(convert(w)));
        return dtos;
    }
}
