package com.elyasi.assignments.widgets.repository;

import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by Ghasem on 27/03/2021
 */
@Profile(GlobalConstant.PROFILE_IN_MEMORY)
@Component
@Slf4j
public class InMemoryRepository implements WidgetRepository {
    private final ConcurrentMap<UUID, Widget> board = new ConcurrentHashMap<>();
    private int maxZIndex = Integer.MIN_VALUE;

    @Override
    public Optional<Widget> findById(UUID id) {
        return Optional.ofNullable(board.get(id));
    }

    @Override
    public Widget insert(Widget widget) {
        widget.setId(UUID.randomUUID());
        board.put(widget.getId(), widget);

        // update max-z
        if (maxZIndex < widget.getZ())
            maxZIndex = widget.getZ();

        return widget;
    }

    @Override
    public boolean existsById(UUID id) {
        return board.containsKey(id);
    }

    @Override
    public Widget update(Widget widget) {
        board.remove(widget.getId());
        board.put(widget.getId(), widget);

        // update max-z
        if (maxZIndex < widget.getZ())
            maxZIndex = widget.getZ();

        return widget;
    }

    @Override
    public void delete(UUID id) {
        board.remove(id);
    }

    @Override
    public int getMaxZ() {
        return maxZIndex;
    }

    @Override
    public boolean isZExist(final int z) {
        return board.entrySet().stream()
                .anyMatch(w -> w.getValue().getZ() == z);
    }

    @Override
    public List<Widget> getWidgetsWithZGreaterThanOrEqual(final int z) {
        // extract z indexes greater than or equal to z
        List<Widget> greaterThanList =
                board.values().stream()
                        .filter(widget -> widget.getZ() >= z)
                        .sorted(Comparator.comparingInt(Widget::getZ))
                        .collect(Collectors.toList());

        return extractByZInRow(z, greaterThanList);
    }


    @Override
    public List<Widget> findAll(int pageSize, int pageIndex) {
        if (pageIndex * pageSize >= board.size())
            return Collections.emptyList();

        List<Widget> widgetList = board.values().stream()
                .sorted(Comparator.comparingInt(Widget::getZ))
                .collect(Collectors.toList());

        return widgetList.subList(pageIndex * pageSize, Math.min(pageSize * (pageIndex + 1), widgetList.size()));
    }

    @Override
    public List<Widget> findAllInArea(final Area area, int pageSize, int pageIndex) {
        if (pageIndex * pageSize >= board.size())
            return Collections.emptyList();

        List<Widget> widgetList = board.values().stream()
                .filter(w -> area.contains(w.getArea()))
                .sorted(Comparator.comparingInt(Widget::getZ))
                .collect(Collectors.toList());

        return widgetList.subList(pageIndex * pageSize, Math.min(pageSize * (pageIndex + 1), widgetList.size()));
    }

}
