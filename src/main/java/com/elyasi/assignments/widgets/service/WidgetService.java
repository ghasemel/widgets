package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.dto.ListWidgetDto;
import com.elyasi.assignments.widgets.dto.WidgetDto;

import java.util.UUID;

public interface WidgetService {
    WidgetDto getById(UUID id);

    WidgetDto create(WidgetDto widgetDto);

    WidgetDto update(UUID id, WidgetDto widgetDto);

    void deleteById(UUID id);

    ListWidgetDto getList(String areaFilter, Integer pageSize, Integer startIndex);
}