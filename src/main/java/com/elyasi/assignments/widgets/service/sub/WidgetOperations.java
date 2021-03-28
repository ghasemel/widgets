package com.elyasi.assignments.widgets.service.sub;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
public interface WidgetOperations {

    Optional<Widget> readWidget(UUID id);

    Widget addWidgetWithoutZIndex(Widget widget);

    Widget addWidgetWithZIndex(Widget widget);

    Widget updateWidgetWithoutZIndex(Widget widget);

    Widget updateWidgetWithZIndex(Widget widget);

    void deleteWidget(UUID id);

    List<Widget> allWidget(int pageSize, int pageIndex);

    List<Widget> allWidgetInArea(Area area, int pageSize, int pageIndex);


}
