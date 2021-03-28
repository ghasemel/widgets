package com.elyasi.assignments.widgets.service;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.config.Config;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidValueException;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static com.elyasi.assignments.widgets.constant.GlobalConstant.DEFAULT_PAGE_INDEX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WidgetServiceImplTest {

    @Mock
    private WidgetServiceImpl.WidgetOperations operations;

    @Mock
    private Config config;

    private WidgetServiceImpl widgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        widgetService = new WidgetServiceImpl(operations, config);
    }

    // region get **********************************************************
    @Test
    void givenIdNull_thenGetById_assertInvalidValueException() {
        // given
        final UUID id = null;

        // assert
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> {
            // then
            widgetService.getById(id);
        });
        assertEquals(new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenNotExistId_thenGetById_assertWidgetNotFoundException() {
        // given
        final UUID id = UUID.randomUUID();

        // when
        when(operations.readWidget(id)).thenReturn(Optional.empty());

        // assert
        WidgetNotFoundException exception = assertThrows(WidgetNotFoundException.class, () -> {
            // then
            widgetService.getById(id);
        });
        assertEquals(new WidgetNotFoundException(id).getMessage(), exception.getMessage());

        // verify
        verify(operations).readWidget(id);
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenExistWidgetId_thenGetById_assertReturnValue() {
        // given
        final Widget existWidget = TestHelper.getWidget();
        final UUID id = existWidget.getId();

        // when
        when(operations.readWidget(id)).thenReturn(Optional.of(existWidget));

        // then
        Widget returnWidget = widgetService.getById(id);

        // assert
        assertNotNull(returnWidget);
        assertEquals(existWidget.getId(), returnWidget.getId());
        assertEquals(existWidget.getWidth(), returnWidget.getWidth());
        assertEquals(existWidget.getHeight(), returnWidget.getHeight());
        assertEquals(existWidget.getX(), returnWidget.getX());
        assertEquals(existWidget.getY(), returnWidget.getY());
        assertEquals(existWidget.getZ(), returnWidget.getZ());

        // verify
        verify(operations).readWidget(id);
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }
    // endregion get **********************************************************

    // region create **********************************************************
    @Test
    void givenWidgetNull_thenCreate_assertInvalidRequestBodyException() {
        // given
        final Widget existWidget = null;

        // assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> {
            // then
            widgetService.create(existWidget);
        });
        assertEquals(new InvalidRequestBodyException(GlobalConstant.NULL_VALUE).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithId_thenCreate_assertMutabilityException() {
        // given
        final Widget existWidget = TestHelper.getWidget();

        // assert
        MutabilityException exception = assertThrows(MutabilityException.class, () -> {
            // then
            widgetService.create(existWidget);
        });
        assertEquals(new MutabilityException(WidgetDto.ID_NAME).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithZNull_thenCreate_assertRightOperation() {
        // given
        final Widget newWidget = TestHelper.getWidget();
        newWidget.setId(null);
        newWidget.setZ(null);

        // when
        when(operations.addWidgetWithoutZIndex(newWidget)).thenReturn(newWidget);

        // then
        Widget created = widgetService.create(newWidget);

        // assert
        assertNotNull(created);

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations).addWidgetWithoutZIndex(newWidget);
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithZ_thenCreate_assertRightOperation() {
        // given
        final Widget newWidget = TestHelper.getWidget();
        newWidget.setId(null);
        newWidget.setZ(TestHelper.getRandomNum());

        // when
        when(operations.addWidgetWithZIndex(newWidget)).thenReturn(newWidget);

        // then
        Widget created = widgetService.create(newWidget);

        // assert
        assertNotNull(created);

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations).addWidgetWithZIndex(newWidget);
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }
    // endregion create **********************************************************

    // region update **********************************************************
    @Test
    void givenWidgetNull_thenUpdate_assertInvalidRequestBodyException() {
        // given
        final Widget existWidget = null;

        // assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> {
            // then
            widgetService.update(existWidget);
        });
        assertEquals(new InvalidRequestBodyException(GlobalConstant.NULL_VALUE).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithIdNull_thenUpdate_assertInvalidValueException() {
        // given
        final Widget existWidget = TestHelper.getWidget();
        existWidget.setId(null);

        // assert
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> {
            // then
            widgetService.update(existWidget);
        });
        assertEquals(new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithZNull_thenUpdate_assertRightOperation() {
        // given
        final Widget existWidget = TestHelper.getWidget();
        existWidget.setZ(null);

        // when
        when(operations.updateWidgetWithoutZIndex(existWidget)).thenReturn(existWidget);

        // then
        Widget updated = widgetService.update(existWidget);

        // assert
        assertNotNull(updated);

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations).updateWidgetWithoutZIndex(existWidget);
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenWidgetWithZ_thenUpdate_assertRightOperation() {
        // given
        final Widget existWidget = TestHelper.getWidget();
        existWidget.setZ(TestHelper.getRandomNum());

        // when
        when(operations.updateWidgetWithZIndex(existWidget)).thenReturn(existWidget);

        // then
        Widget updated = widgetService.update(existWidget);

        // assert
        assertNotNull(updated);

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations).updateWidgetWithZIndex(existWidget);
    }
    // endregion update **********************************************************

    // region get **********************************************************
    @Test
    void givenIdNull_thenDeleteById_assertInvalidValueException() {
        // given
        final UUID id = null;

        // assert
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> {
            // then
            widgetService.deleteById(id);
        });
        assertEquals(new InvalidValueException(WidgetDto.ID_NAME, GlobalConstant.NULL_VALUE).getMessage(), exception.getMessage());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    @Test
    void givenId_thenDeleteById_assertRightCall() {
        // given
        final UUID id = UUID.randomUUID();

        // when
        doNothing().when(operations).deleteWidget(id);


        // assert
        assertDoesNotThrow(() -> {
            // then
            widgetService.deleteById(id);
        });

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations).deleteWidget(id);
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
    }

    // endregion delete **********************************************************

    // region getList **********************************************************
    @Test
    void givenAreaNullAndPageSizeNullAndPageIndexNull_thenGetList_assertDefaultValueForPageSizeAndPageIndex() {
        // given
        final Integer pageSize = null;
        final Integer pageIndex = null;
        final int defaultPageSize = 10;
        final int defaultPageIndex = DEFAULT_PAGE_INDEX;

        // when
        when(config.getDefaultPageSize()).thenReturn(defaultPageSize);
        when(operations.allWidget(anyInt(), anyInt())).thenReturn(null);
        when(operations.allWidgetInArea(any(), anyInt(), anyInt())).thenReturn(null);

        // then
        WidgetList result = widgetService.getList(null, pageSize, pageIndex);

        // assert
        assertNotNull(result);
        assertEquals(defaultPageSize, result.getPageSize());
        assertEquals(defaultPageIndex, result.getPageIndex());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
        verify(operations).allWidget(anyInt(), anyInt());
        verify(operations, never()).allWidgetInArea(any(), anyInt(), anyInt());
        verify(config).getDefaultPageSize();
    }

    @Test
    void givenAreaNullAndPageSizeNotNullAndPageIndexNotNull_thenGetList_assertCallToRightOperation() {
        // given
        final Area area = null;
        final int pageSize = 20;
        final int pageIndex = 5;

        // when
        when(operations.allWidget(pageSize, pageIndex)).thenReturn(null);
        when(operations.allWidgetInArea(any(), anyInt(), anyInt())).thenReturn(null);

        // then
        WidgetList result = widgetService.getList(area, pageSize, pageIndex);

        // assert
        assertNotNull(result);
        assertEquals(pageSize, result.getPageSize());
        assertEquals(pageIndex, result.getPageIndex());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
        verify(operations).allWidget(pageSize, pageIndex);
        verify(operations, never()).allWidgetInArea(any(), anyInt(), anyInt());
        verify(config, never()).getDefaultPageSize();
    }

    @Test
    void givenAreaNotNullAndPageSizeNotNullAndPageIndexNotNull_thenGetList_assertCallToRightOperation() {
        // given
        final Area area = Area.builder().build();
        final int pageSize = 20;
        final int pageIndex = 5;

        // when
        when(operations.allWidget(anyInt(), anyInt())).thenReturn(null);
        when(operations.allWidgetInArea(area, pageSize, pageIndex)).thenReturn(null);

        // then
        WidgetList result = widgetService.getList(area, pageSize, pageIndex);

        // assert
        assertNotNull(result);
        assertEquals(pageSize, result.getPageSize());
        assertEquals(pageIndex, result.getPageIndex());

        // verify
        verify(operations, never()).readWidget(any());
        verify(operations, never()).deleteWidget(any());
        verify(operations, never()).addWidgetWithZIndex(any());
        verify(operations, never()).addWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithoutZIndex(any());
        verify(operations, never()).updateWidgetWithZIndex(any());
        verify(operations, never()).allWidget(anyInt(), anyInt());
        verify(operations).allWidgetInArea(area, pageSize, pageIndex);
        verify(config, never()).getDefaultPageSize();
    }
    // endregion getList **********************************************************
}