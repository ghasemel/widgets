package com.elyasi.assignments.widgets.service.sub;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetOperationsImpl implements WidgetOperations {
    private final WidgetRepository repository;

    @Setter(AccessLevel.PROTECTED)
    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
