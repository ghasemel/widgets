package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.config.Config;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.converter.WidgetToWidgetDtoConverter;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.model.ListWidgetDto;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.model.WidgetDto;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidValueException;
import com.elyasi.assignments.widgets.service.helper.BoardOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Ghasem on 27/03/2021
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WidgetServiceImpl implements WidgetService {
    private final ConversionService conversion;
    private final BoardOperations boardOperations;
    private final Config config;


    @Override
    public WidgetDto getById(UUID id) {
        log.debug("call getById(): {}", id);

        if (id == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // retrieve widget
        Optional<Widget> widgetOptional = boardOperations.readWidget(id);
        if (widgetOptional.isEmpty())
            throw new WidgetNotFoundException(id);

        WidgetDto widgetDto = conversion.convert(widgetOptional.get(), WidgetDto.class);
        log.debug("getById(): retrieved widget: {}", widgetDto);

        return widgetDto;
    }


    @Override
    public WidgetDto create(WidgetDto widgetDto) {
        log.debug("call create(): {}", widgetDto);

        if (widgetDto == null)
            throw new InvalidRequestBodyException(GlobalConstant.NULL_VALUE);

        if (widgetDto.getId() != null)
            throw new MutabilityException(WidgetDto.ID_NAME);

        // convert to widget
        Widget widget = conversion.convert(widgetDto, Widget.class);
        assert widget != null;

        // if z-index is null
        if (widgetDto.getZ() == null) {
            widget = boardOperations.addWidgetWithNoZIndex(widget);
        } else {
            widget = boardOperations.addWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        WidgetDto result = conversion.convert(widget, WidgetDto.class);
        log.debug("create(): inserted widget: {}", result);

        return result;
    }

    @Override
    public WidgetDto update(UUID id, WidgetDto widgetDto) {
        log.debug("call update(): {}", widgetDto);

        if (widgetDto == null)
            throw new InvalidRequestBodyException(GlobalConstant.NULL_VALUE);

        if (widgetDto.getId() != null && !id.equals(widgetDto.getId()))
            throw new MutabilityException(WidgetDto.ID_NAME);

        // convert to widget
        Widget widget = conversion.convert(widgetDto, Widget.class);
        assert widget != null;
        widget.setId(id);

        // if z-index is null
        if (widgetDto.getZ() == null) {
            widget = boardOperations.updateWidgetWithoutZIndex(widget);
        } else {
            widget = boardOperations.updateWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        WidgetDto result = conversion.convert(widget, WidgetDto.class);
        log.debug("update(): updated widget: {}", result);

        return result;
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("call deleteById(): {}", id);

        if (id == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // delete widget
        boardOperations.deleteWidget(id);

        log.debug("deleteById(): widget {} has been deleted", id);
    }

    @Override
    public ListWidgetDto getList(String areaFilter, Integer pageSize, Integer pageIndex) {
        log.debug("call getList(): areaFilter: {}, pageSize: {}, pageIndex: {}", areaFilter, pageSize, pageIndex);

        if (pageSize == null || pageSize <= 0) {
            pageSize = config.getDefaultPageSize();
        }

        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }

        List<Widget> widgets;
        if (ObjectUtils.isEmpty(areaFilter)) {
            // retrieve all
            widgets = boardOperations.allWidget(pageSize, pageIndex);
        } else {
            // retrieve by filter
            widgets = boardOperations.allWidgetInArea(conversion.convert(areaFilter, Area.class), pageSize, pageIndex);
        }

        // prepare result
        ListWidgetDto listWidgetDto = ListWidgetDto.builder().pageSize(pageSize).pageIndex(pageIndex).build();
        if (widgets == null || widgets.isEmpty())
            return listWidgetDto;

        // convert widget list to widget dto list
        listWidgetDto.setWidgets(getWidgetDtoList(widgets));

        return listWidgetDto;
    }

    /** convert widget list to widgetDto list
     * @param widgets list of widgets
     * @return list of widgetDto
     */
    private List<WidgetDto> getWidgetDtoList(List<Widget> widgets) {
        return widgets.stream().map(w -> conversion.convert(w, WidgetDto.class)).collect(Collectors.toList());
    }
}
