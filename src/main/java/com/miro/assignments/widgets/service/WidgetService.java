package com.miro.assignments.widgets.service;

import com.miro.assignments.widgets.dto.WidgetDto;

import java.util.List;
import java.util.UUID;

public interface WidgetService {
    WidgetDto getById(UUID id);

    WidgetDto create(WidgetDto widgetDto);

    WidgetDto update(UUID id, WidgetDto widgetDto);

    void deleteById(UUID id);

    List<WidgetDto> getList(String areaFilter, Integer pageSize, Integer startIndex);
}
