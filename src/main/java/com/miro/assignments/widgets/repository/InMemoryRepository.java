package com.miro.assignments.widgets.repository;

import com.miro.assignments.widgets.constant.GlobalConstant;
import com.miro.assignments.widgets.domain.Widget;
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
    public Widget update(Widget widget) {
        //Optional<Widget> widgetOptional = findById(widget.getId());
        //if (widgetOptional.isPresent()) {
        board.remove(widget.getId());
        board.put(widget.getId(), widget);

        // update max-z
        if (maxZIndex < widget.getZ())
            maxZIndex = widget.getZ();

        return widget;
        //}
        //return widgetOptional;
    }

    @Override
    public boolean delete(UUID id) {
        Optional<Widget> widgetOptional = findById(id);
        if (widgetOptional.isPresent()) {
            board.remove(id);
            return true;
        }
        return false;
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

    public List<Widget> getWidgetsWithZGreaterThanOrEqual(final int z) {
        List<Map.Entry<UUID, Widget>> greaterThanList =
                board.entrySet().stream()
                        .filter(w -> w.getValue().getZ() >= z)
                        .sorted((o1, o2) -> o1.getValue().getZ() > o2.getValue().getZ() ? 1 : 0)
                        .collect(Collectors.toList());


        List<Widget> selectedList = new ArrayList<>();

        // select widgets with z-index in a row
        // e.g.: 4,5,6,8,9 -> selected: 4,5,6
        for (int i = 0; i < greaterThanList.size(); i++) {
            Widget widget = greaterThanList.get(i).getValue();
            if (widget.getZ() - z - i <= 1)
                selectedList.add(widget);
        }

        return selectedList;
    }
}
