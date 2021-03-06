package com.elyasi.assignments.widgets.web.converter;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Ghasem on 27/03/2021
 */
public class WidgetDtoToWidgetConverter implements Converter<WidgetDto, Widget> {

    @Override
    public Widget convert(WidgetDto dto) {
        return Widget.builder()
                .id(dto.getId())
                .x(dto.getX())
                .y(dto.getY())
                .z(dto.getZ())
                .height(dto.getHeight())
                .width(dto.getWidth())
                .build();
    }
}
