package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.config.Config;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidValueException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import com.elyasi.assignments.widgets.service.sub.WidgetOperations;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.elyasi.assignments.widgets.constant.GlobalConstant.DEFAULT_PAGE_INDEX;

/**
 * Created by Ghasem on 27/03/2021
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WidgetServiceImpl implements WidgetService {

    private final WidgetOperations widgetOperations;
    private final Config config;


    @Override
    public Widget getById(UUID id) {
        log.debug("call getById(): {}", id);

        if (id == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // retrieve widget
        Optional<Widget> widgetOptional = widgetOperations.readWidget(id);
        if (widgetOptional.isEmpty())
            throw new WidgetNotFoundException(id);

        log.debug("getById(): retrieved widget: {}", widgetOptional.get());

        return widgetOptional.get();
    }


    @Override
    public Widget create(Widget widget) {
        log.debug("call create(): {}", widget);

        if (widget == null)
            throw new InvalidRequestBodyException(GlobalConstant.NULL_VALUE);

        if (widget.getId() != null)
            throw new MutabilityException(WidgetDto.ID_NAME);


        // if z-index is null
        if (widget.getZ() == null) {
            widget = widgetOperations.addWidgetWithoutZIndex(widget);
        } else {
            widget = widgetOperations.addWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        log.debug("create(): inserted widget: {}", widget);

        return widget;
    }

    @Override
    public Widget update(Widget widget) {
        log.debug("call update(): {}", widget);

        if (widget == null)
            throw new InvalidRequestBodyException(GlobalConstant.NULL_VALUE);

        if (widget.getId() == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // if z-index is null
        if (widget.getZ() == null) {
            widget = widgetOperations.updateWidgetWithoutZIndex(widget);
        } else {
            widget = widgetOperations.updateWidgetWithZIndex(widget);
        }

        // convert to widgetDto
        log.debug("update(): updated widget: {}", widget);

        return widget;
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("call deleteById(): {}", id);

        if (id == null)
            throw new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE);

        // delete widget
        widgetOperations.deleteWidget(id);

        log.debug("deleteById(): widget {} has been deleted", id);
    }

    @Override
    public WidgetList getList(Area area, Integer pageSize, Integer pageIndex) {
        log.debug("call getList(): area: {}, pageSize: {}, pageIndex: {}", area, pageSize, pageIndex);

        if (pageSize == null || pageSize <= 0) {
            pageSize = config.getDefaultPageSize();
        }

        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = DEFAULT_PAGE_INDEX;
        }

        List<Widget> widgets;
        if (area == null) {
            // retrieve all
            widgets = widgetOperations.allWidget(pageSize, pageIndex);
        } else {
            // retrieve by filter
            widgets = widgetOperations.allWidgetInArea(area, pageSize, pageIndex);
        }

        // prepare result
        return WidgetList.builder()
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .widgets(widgets)
                .build();
    }


    @Slf4j
    @Service
    @RequiredArgsConstructor
    protected static class WidgetOperations {
        private final WidgetRepository repository;

        @Setter(AccessLevel.PROTECTED)
        private ReadWriteLock lock = new ReentrantReadWriteLock(true);

        //@Override
        public Optional<Widget> readWidget(UUID id) {
            Lock readLock = lock.readLock();
            try {
                readLock.lock();

                // retrieve
                return repository.findById(id);
            } finally {
                readLock.unlock();
            }
        }

        //@Override
        public Widget addWidgetWithoutZIndex(Widget widget) {
            Lock writeLock = lock.writeLock();
            try {
                writeLock.lock();

                // retrieve max-z
                widget.setZ(repository.getMaxZ());

                // insert widget
                return repository.insert(widget);
            } finally {
                writeLock.unlock();
            }
        }

        public Widget addWidgetWithZIndex(Widget widget) {
            Lock writeLock = lock.writeLock();
            try {
                writeLock.lock();

                doZShiftIfNeeded(widget);

                // insert widget
                return repository.insert(widget);
            } finally {
                writeLock.unlock();
            }
        }

        //@Override
        public Widget updateWidgetWithoutZIndex(Widget widget) {
            Lock writeLock = lock.writeLock();
            try {
                writeLock.lock();

                if (!repository.existsById(widget.getId()))
                    throw new WidgetNotFoundException(widget.getId());

                // retrieve max-z
                widget.setZ(repository.getMaxZ());

                // insert widget
                return repository.update(widget);
            } finally {
                writeLock.unlock();
            }
        }

        //@Override
        public Widget updateWidgetWithZIndex(Widget widget) {
            Lock writeLock = lock.writeLock();
            try {
                writeLock.lock();

                if (!repository.existsById(widget.getId()))
                    throw new WidgetNotFoundException(widget.getId());

                doZShiftIfNeeded(widget);

                // insert widget
                return repository.update(widget);
            } finally {
                writeLock.unlock();
            }
        }

        private void doZShiftIfNeeded(Widget widget) {
            // if z-index exist
            if (repository.isZExist(widget.getZ())) {
                // retrieve widgets >= z
                // update their z-index + 1

                // then retrieve all greater than or equal
                List<Widget> widgetList = repository.getWidgetsWithZGreaterThanOrEqual(widget.getZ());

                // shift widgets by one z-index
                widgetList.forEach(w -> repository.update(w.incrementZ()));
            }
        }

        //@Override
        public void deleteWidget(UUID id) {
            Lock writeLock = lock.writeLock();
            try {
                writeLock.lock();

                if (!repository.existsById(id))
                    throw new WidgetNotFoundException(id);

                // retrieve
                repository.delete(id);
            } finally {
                writeLock.unlock();
            }
        }

        //@Override
        public List<Widget> allWidget(int pageSize, int pageIndex) {
            Lock readLock = lock.readLock();
            try {
                readLock.lock();

                // retrieve
                return repository.findAll(pageSize, pageIndex - 1);
            } finally {
                readLock.unlock();
            }
        }

        //@Override
        public List<Widget> allWidgetInArea(Area area, int pageSize, int pageIndex) {
            Lock readLock = lock.readLock();
            try {
                readLock.lock();

                // retrieve
                return repository.findAllInArea(area, pageSize, pageIndex - 1);
            } finally {
                readLock.unlock();
            }
        }
    }

}
