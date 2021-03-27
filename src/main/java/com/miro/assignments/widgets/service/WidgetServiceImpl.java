package com.miro.assignments.widgets.service;

import com.miro.assignments.widgets.config.Config;
import com.miro.assignments.widgets.constant.GlobalConstant;
import com.miro.assignments.widgets.converter.WidgetDtoToWidgetConverter;
import com.miro.assignments.widgets.converter.WidgetToWidgetDtoConverter;
import com.miro.assignments.widgets.domain.Widget;
import com.miro.assignments.widgets.dto.ListWidgetDto;
import com.miro.assignments.widgets.dto.WidgetDto;
import com.miro.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.miro.assignments.widgets.exception.defined.bad.InvalidValueException;
import com.miro.assignments.widgets.exception.defined.bad.MutabilityException;
import com.miro.assignments.widgets.exception.defined.WidgetNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WidgetServiceImpl implements WidgetService {
    private final WidgetToWidgetDtoConverter toWidgetDtoConverter;
    private final WidgetDtoToWidgetConverter toWidgetConverter;
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

        WidgetDto widgetDto = toWidgetDtoConverter.convert(widgetOptional.get());
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
        Widget widget = toWidgetConverter.convert(widgetDto);
        assert widget != null;

        // if z-index is null
        if (widgetDto.getZ() == null) {
            widget = boardOperations.addWidgetWithNoZIndex(widget);
        } else {
            widget = boardOperations.addWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        WidgetDto result = toWidgetDtoConverter.convert(widget);
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
        Widget widget = toWidgetConverter.convert(widgetDto);
        assert widget != null;
        widget.setId(id);

        // if z-index is null
        if (widgetDto.getZ() == null) {
            widget = boardOperations.updateWidgetWithNoZIndex(widget);
        } else {
            widget = boardOperations.updateWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        WidgetDto result = toWidgetDtoConverter.convert(widget);
        log.debug("update(): updated widget: {}", result);

        return result;
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("call deleteById(): {}", id);

        if (id == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // retrieve widget
        boolean deleteResult = boardOperations.deleteWidget(id);
        if (deleteResult)
            throw new WidgetNotFoundException(id);

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

        List<Widget> widgets = boardOperations.allWidget(pageSize, pageIndex);


        if (widgets == null || widgets.isEmpty())
            return ListWidgetDto.builder().pageSize(pageSize).pageIndex(pageIndex).count(0).build();

        return ListWidgetDto.builder()
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .count(widgets.size())
                .widgets(toWidgetDtoConverter.convert(widgets)).build();
    }
}
