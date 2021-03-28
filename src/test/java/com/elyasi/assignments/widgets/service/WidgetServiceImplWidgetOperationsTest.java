package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.config.Config;
import com.elyasi.assignments.widgets.config.WidgetConfig;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidValueException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.elyasi.assignments.widgets.constant.GlobalConstant.DEFAULT_PAGE_INDEX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WidgetServiceImplWidgetOperationsTest {

    @Mock
    private WidgetRepository repository;

    @Spy
    private ReentrantReadWriteLock lockSpy;

    private WidgetServiceImpl.WidgetOperations widgetOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        widgetOperations = new WidgetServiceImpl.WidgetOperations(repository, lockSpy);
    }

    @Test
    void givenId_thenReadWidget_assertLock() {
        // given
        UUID id = UUID.randomUUID();

        // when
        when(repository.findById(id)).thenReturn(Optional.empty());

        // then
        Optional<Widget> widget = widgetOperations.readWidget(id);

        // assert
        assertTrue(widget.isEmpty());

        // verify
        verify(lockSpy).readLock();
        verify(repository).findById(id);
    }


    // region add
    @Test
    void givenWidgetWithoutZ_thenAddWidgetWithoutZIndex_assertLockAndResult() {
        // given
        Widget widget = TestHelper.getWidget();
        widget.setZ(null);
        int maxZ = 100;

        // when
        when(repository.insert(widget)).thenReturn(widget);
        when(repository.getMaxZ()).thenReturn(maxZ);

        // then
        Widget resultWidget = widgetOperations.addWidgetWithoutZIndex(widget);

        // assert
        assertNotNull(resultWidget);
        assertEquals(maxZ, resultWidget.getZ());

        // verify
        verify(lockSpy).writeLock();
        verify(repository).insert(widget);
        verify(repository).getMaxZ();
    }

    @Test
    void givenWidgetWithExistZ_thenAddWidgetWithZIndex_assertLockAndResultAndShift() {
        // given
        final Widget widget = TestHelper.getWidget();

        final List<Widget> widgetsWithEqualOrHigherZ = new ArrayList<>();
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());

        // when
        when(repository.insert(widget)).thenReturn(widget);
        when(repository.isZExist(widget.getZ())).thenReturn(true); // means shift should happen
        when(repository.getWidgetsWithZGreaterThanOrEqual(widget.getZ())).thenReturn(widgetsWithEqualOrHigherZ);
        when(repository.update(any(Widget.class))).thenReturn(null); // result doesn't matter

        // then
        Widget resultWidget = widgetOperations.addWidgetWithZIndex(widget);

        // assert
        assertNotNull(resultWidget);

        // verify
        verify(lockSpy).writeLock();
        verify(repository).isZExist(widget.getZ());
        verify(repository).getWidgetsWithZGreaterThanOrEqual(widget.getZ());
        verify(repository, times(widgetsWithEqualOrHigherZ.size())).update(any(Widget.class));
        verify(repository).insert(widget);
    }

    @Test
    void givenWidgetWithNotExistZ_thenAddWidgetWithZIndex_assertLockAndResultAndWithoutShift() {
        // given
        final Widget widget = TestHelper.getWidget();

        // when
        when(repository.insert(widget)).thenReturn(widget);
        when(repository.isZExist(widget.getZ())).thenReturn(false); // means shift should happen

        // then
        Widget resultWidget = widgetOperations.addWidgetWithZIndex(widget);

        // assert
        assertNotNull(resultWidget);

        // verify
        verify(lockSpy).writeLock();
        verify(repository).isZExist(widget.getZ());
        verify(repository, never()).getWidgetsWithZGreaterThanOrEqual(widget.getZ());
        verify(repository, never()).update(any(Widget.class));
        verify(repository).insert(widget);
    }
    // endregion add

    // region update
    @Test
    void givenNotExistWidgetWithoutZ_thenUpdateWidgetWithoutZIndex_assertLockAndWidgetNotFoundException() {
        // given
        Widget widget = TestHelper.getWidget();
        widget.setZ(null);

        // when
        when(repository.existsById(widget.getId())).thenReturn(false);

        // assert
        WidgetNotFoundException exception = assertThrows(WidgetNotFoundException.class, () -> {
            // then
            widgetOperations.updateWidgetWithoutZIndex(widget);
        });
        assertEquals(new WidgetNotFoundException(widget.getId()).getMessage(), exception.getMessage());

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(widget.getId());
        verify(repository, never()).getMaxZ();
        verify(repository, never()).update(any());
    }

    @Test
    void givenWidgetWithoutZ_thenUpdateWidgetWithoutZIndex_assertLockAndResult() {
        // given
        Widget widget = TestHelper.getWidget();
        widget.setZ(null);
        int maxZ = 100;

        // when
        when(repository.existsById(widget.getId())).thenReturn(true);
        when(repository.getMaxZ()).thenReturn(maxZ);
        when(repository.update(widget)).thenReturn(widget);

        // then
        Widget resultWidget = widgetOperations.updateWidgetWithoutZIndex(widget);

        // assert
        assertNotNull(resultWidget);
        assertEquals(maxZ, resultWidget.getZ());

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(widget.getId());
        verify(repository).getMaxZ();
        verify(repository).update(widget);
    }

    @Test
    void givenNotExistWidgetWithZ_thenUpdateWidgetWithZIndex_assertLockAndWidgetNotFoundException() {
        // given
        Widget widget = TestHelper.getWidget();

        // when
        when(repository.existsById(widget.getId())).thenReturn(false);

        // assert
        WidgetNotFoundException exception = assertThrows(WidgetNotFoundException.class, () -> {
            // then
            widgetOperations.updateWidgetWithZIndex(widget);
        });
        assertEquals(new WidgetNotFoundException(widget.getId()).getMessage(), exception.getMessage());

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(widget.getId());
        verify(repository, never()).getMaxZ();
        verify(repository, never()).update(any());
    }

    @Test
    void givenWidgetWithExistZ_thenUpdateWidgetWithZIndex_assertLockAndResultAndShift() {
        // given
        final Widget widget = TestHelper.getWidget();

        final List<Widget> widgetsWithEqualOrHigherZ = new ArrayList<>();
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());
        widgetsWithEqualOrHigherZ.add(TestHelper.getWidget());

        // when
        when(repository.existsById(widget.getId())).thenReturn(true);
        when(repository.isZExist(widget.getZ())).thenReturn(true); // means shift should happen
        when(repository.getWidgetsWithZGreaterThanOrEqual(widget.getZ())).thenReturn(widgetsWithEqualOrHigherZ);
        when(repository.update(any(Widget.class))).thenReturn(widget); // result doesn't matter


        // then
        Widget resultWidget = widgetOperations.updateWidgetWithZIndex(widget);

        // assert
        assertNotNull(resultWidget);

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(widget.getId());
        verify(repository).isZExist(widget.getZ());
        verify(repository).getWidgetsWithZGreaterThanOrEqual(widget.getZ());
        verify(repository, times(widgetsWithEqualOrHigherZ.size() + 1)).update(any(Widget.class));
    }

    @Test
    void givenWidgetWithNotExistZ_thenUpdateWidgetWithZIndex_assertLockAndResultAndWithoutShift() {
        // given
        final Widget widget = TestHelper.getWidget();

        // when
        when(repository.existsById(widget.getId())).thenReturn(true);
        when(repository.isZExist(widget.getZ())).thenReturn(false); // means shift should happen
        when(repository.update(widget)).thenReturn(widget);

        // then
        Widget resultWidget = widgetOperations.updateWidgetWithZIndex(widget);

        // assert
        assertNotNull(resultWidget);

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(widget.getId());
        verify(repository).isZExist(widget.getZ());
        verify(repository, never()).getWidgetsWithZGreaterThanOrEqual(widget.getZ());
        verify(repository).update(widget);
    }
    // endregion update

    // region delete
    @Test
    void givenNotExistWidgetId_thenDeleteWidget_assertLockAndWidgetNotFoundException() {
        // given
        final UUID id = UUID.randomUUID();

        // when
        when(repository.existsById(id)).thenReturn(false);

        // assert
        WidgetNotFoundException exception = assertThrows(WidgetNotFoundException.class, () -> {
            // then
            widgetOperations.deleteWidget(id);
        });
        assertEquals(new WidgetNotFoundException(id).getMessage(), exception.getMessage());

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(id);
        verify(repository, never()).delete(any());
    }

    @Test
    void givenExistWidgetId_thenDeleteWidget_assertLockAndResult() {
        // given
        final UUID id = UUID.randomUUID();

        // when
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).delete(id);

        // assert
        assertDoesNotThrow(() -> {
            // then
            widgetOperations.deleteWidget(id);
        });

        // verify
        verify(lockSpy).writeLock();
        verify(repository).existsById(id);
        verify(repository).delete(id);
    }
    // endregion delete

    // region allWidget
    @Test
    void givenPageSizeAndPageIndex_thenAllWidget_assertLockAndResult() {
        // given
        final int pageSize = 20;
        final int pageIndex = 5;

        final List<Widget> widgets = new ArrayList<>();
        widgets.add(TestHelper.getWidget());
        widgets.add(TestHelper.getWidget());
        widgets.add(TestHelper.getWidget());

        // when
        when(repository.findAll(pageSize, pageIndex - 1)).thenReturn(widgets);

        // then
        List<Widget> result = widgetOperations.allWidget(pageSize, pageIndex);

        // assert
        assertNotNull(result);
        assertEquals(widgets, result);

        // verify
        verify(lockSpy).readLock();
        verify(repository).findAll(anyInt(), anyInt());
        verify(repository, never()).findAllInArea(any(), anyInt(), anyInt());
    }

    @Test
    void givenPageSizeAndPageIndex_thenAllWidgetArea_assertLockAndResult() {
        // given
        final int pageSize = 20;
        final int pageIndex = 5;
        final Area area = Area.builder().build();

        final List<Widget> widgets = new ArrayList<>();
        widgets.add(TestHelper.getWidget());
        widgets.add(TestHelper.getWidget());
        widgets.add(TestHelper.getWidget());

        // when
        when(repository.findAllInArea(area, pageSize, pageIndex - 1)).thenReturn(widgets);

        // then
        List<Widget> result = widgetOperations.allWidgetInArea(area, pageSize, pageIndex);

        // assert
        assertNotNull(result);
        assertEquals(widgets, result);

        // verify
        verify(lockSpy).readLock();
        verify(repository, never()).findAll(anyInt(), anyInt());
        verify(repository).findAllInArea(any(Area.class), anyInt(), anyInt());
    }
    // endregion allWidget
}