package com.elyasi.assignments.widgets.web.converter;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.dto.WidgetListDto;
import com.elyasi.assignments.widgets.exception.defined.InvalidTypeException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Ghasem on 27/03/2021
 */
public class WidgetToWidgetDtoConverter implements GenericConverter {


    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(
                new ConvertiblePair(Widget.class, WidgetDto.class),
                new ConvertiblePair(WidgetList.class, WidgetListDto.class)
        );
    }

    @Override
    public Object convert(Object object, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() == Widget.class) {
            return convert((Widget) object);
        }

        if (sourceType.getType() == WidgetList.class) {
            return convertList((WidgetList) object);
        }

        throw new InvalidTypeException(sourceType.getType().getTypeName());
    }

    private WidgetDto convert(Widget widget) {
        return WidgetDto.builder()
                .id(widget.getId())
                .x(widget.getX())
                .y(widget.getY())
                .z(widget.getZ())
                .width(widget.getWidth())
                .height(widget.getHeight())
                .build();
    }

    private WidgetListDto convertList(WidgetList widgetList) {
        return WidgetListDto.builder()
                .pageIndex(widgetList.getPageIndex())
                .pageSize(widgetList.getPageSize())
                .widgets(mapToWidgetDto(widgetList.getWidgets()))
                .count(widgetList.getWidgets() == null ? 0 : widgetList.getWidgets().size())
                .build();
    }

    private List<WidgetDto> mapToWidgetDto(List<Widget> widgetList) {
        return widgetList == null ? null :
                widgetList.stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
    }
}
