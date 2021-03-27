package com.miro.assignments.widgets.converter;

import com.miro.assignments.widgets.domain.Widget;
import com.miro.assignments.widgets.dto.WidgetDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Ghasem on 27/03/2021
 */
@Component
public class WidgetDtoToWidgetConverter implements Converter<WidgetDto, Widget> {

    @Override
    public Widget convert(WidgetDto dto) {
        return Widget.builder()
                .id(dto.getId())
                .x(dto.getX())
                .y(dto.getY())
                .z(dto.getZ() == null ? 0 : dto.getZ())
                .height(dto.getHeight())
                .width(dto.getWidth())
                .build();
    }
}
