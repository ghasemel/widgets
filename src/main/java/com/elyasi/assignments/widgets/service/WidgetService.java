package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;

import java.util.UUID;

public interface WidgetService {
    Widget getById(UUID id);

    Widget create(Widget widgetDto);

    Widget update(Widget widget);

    void deleteById(UUID id);

    WidgetList getList(Area area, Integer pageSize, Integer startIndex);
}
