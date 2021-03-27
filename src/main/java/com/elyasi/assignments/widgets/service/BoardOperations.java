package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import com.elyasi.assignments.widgets.domain.Widget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Ghasem on 27/03/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BoardOperations {
    private final WidgetRepository repository;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);


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

    public Widget addWidgetWithNoZIndex(Widget widget) {
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
