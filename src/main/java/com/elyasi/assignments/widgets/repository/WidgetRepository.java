package com.elyasi.assignments.widgets.repository;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WidgetRepository {
    Optional<Widget> findById(UUID id);

    Widget insert(Widget widget);

    Widget update(Widget widget);

    void delete(UUID id);

    boolean existsById(UUID id);

    int getMaxZ();

    boolean isZExist(int z);

    List<Widget> getWidgetsWithZGreaterThanOrEqual(final int z);

    /**
     * find all widgets considering pageSize and pageIndex
     * @param pageSize
     * @param pageIndex zero-based index
     * @return
     */
    List<Widget> findAll(int pageSize, int pageIndex);

    /**
     * find all widgets considering pageSize and pageIndex
     * @param area
     * @param pageSize
     * @param pageIndex zero-based index
     * @return
     */
    List<Widget> findAllInArea(Area area, int pageSize, int pageIndex);


    default List<Widget> extractByZInRow(int z, List<Widget> greaterThanList) {
        // select widgets with z-indexes in a row
        // e.g.: 4,5,6,8,9 -> selected: 4,5,6
        List<Widget> selectedList = new ArrayList<>();
        for (int i = 0; i < greaterThanList.size(); i++) {
            Widget widget = greaterThanList.get(i);
            if (widget.getZ() - z - i <= 1) // was in a row
                selectedList.add(widget);
        }
        return selectedList;
    }
}
