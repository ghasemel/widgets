package com.elyasi.assignments.widgets.repository;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.repository.db.H2WidgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Ghasem on 27/03/2021
 */
@Profile(GlobalConstant.PROFILE_H2)
@Component
@Slf4j
@RequiredArgsConstructor
public class SqlWidgetRepositoryImpl implements WidgetRepository {
    private final H2WidgetRepository h2WidgetRepository;

    @Override
    public Optional<Widget> findById(UUID id) {
        return h2WidgetRepository.findById(id);
    }

    @Override
    public Widget insert(Widget widget) {
        widget.setId(UUID.randomUUID());
        return h2WidgetRepository.save(widget);
    }

    @Override
    public boolean existsById(UUID id) {
        return h2WidgetRepository.existsById(id);
    }

    @Override
    public Widget update(Widget widget) {
        return h2WidgetRepository.save(widget);
    }

    @Override
    public void delete(UUID id) {
        h2WidgetRepository.deleteById(id);
    }

    @Override
    public int getMaxZ() {
        return h2WidgetRepository.findMaxZ();
    }

    @Override
    public boolean isZExist(int z) {
        return h2WidgetRepository.existsWidgetByZ(z);
    }

    @Override
    public List<Widget> getWidgetsWithZGreaterThanOrEqual(int z) {
        List<Widget> greaterThanList = h2WidgetRepository.findAllByZGreaterThanEqualOrderByZAsc(z);

        return extractByZInRow(z, greaterThanList);
    }

    @Override
    public List<Widget> findAll(int pageSize, int pageIndex) {
        if ((long) pageIndex * pageSize >= h2WidgetRepository.count())
            return Collections.emptyList();

        return h2WidgetRepository.findAllWithPagination(PageRequest.of(pageIndex, pageSize)).getContent();
    }

    @Override
    public List<Widget> findAllInArea(Area area, int pageSize, int pageIndex) {
        if ((long) pageIndex * pageSize >= h2WidgetRepository.count())
            return Collections.emptyList();

        return h2WidgetRepository.findAllInAreaWithPagination(
                PageRequest.of(pageIndex, pageSize),
                area.getStart().x,
                area.getStart().y,
                area.getEnd().x,
                area.getEnd().y
        ).getContent();
    }
}
