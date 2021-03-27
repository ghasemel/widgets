package com.miro.assignments.widgets.repository;

import com.miro.assignments.widgets.domain.Area;
import com.miro.assignments.widgets.domain.Widget;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WidgetRepository {
    Optional<Widget> findById(UUID id);

    Widget insert(Widget widget);

    Widget update(Widget widget);

    boolean delete(UUID id);

    int getMaxZ();

    boolean isZExist(int z);

    List<Widget> getWidgetsWithZGreaterThanOrEqual(final int z);

    List<Widget> findAll(int pageSize, int pageIndex);
    List<Widget> findAllInArea(Area area, int pageSize, int pageIndex);
}
