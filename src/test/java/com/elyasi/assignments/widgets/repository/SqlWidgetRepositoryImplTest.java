package com.elyasi.assignments.widgets.repository;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.repository.db.H2WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SqlWidgetRepositoryImplTest {

    @Mock
    private H2WidgetRepository h2WidgetRepository;

    private SqlWidgetRepositoryImpl sqlWidgetRepositoryImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sqlWidgetRepositoryImpl = new SqlWidgetRepositoryImpl(h2WidgetRepository);
    }

    @Test
    void givenId_thenFindById_assertResult() {
        // given
        UUID id = UUID.randomUUID();
        Widget widget = TestHelper.getWidget();

        // when
        when(h2WidgetRepository.findById(id)).thenReturn(Optional.of(widget));

        // then
        Optional<Widget> result = sqlWidgetRepositoryImpl.findById(id);

        // assert
        assertTrue(result.isPresent());
        assertEquals(widget, result.get());

        // verify
        verify(h2WidgetRepository).findById(id);
    }

    @Test
    void givenNewWidget_thenInsert_assertResult() {
        // given
        Widget widget = TestHelper.getWidget();
        widget.setId(null);

        // when
        when(h2WidgetRepository.save(widget)).thenReturn(widget);

        // then
        Widget result = sqlWidgetRepositoryImpl.insert(widget);

        // assert
        assertNotNull(result);
        assertNotNull(result.getId());

        // verify
        verify(h2WidgetRepository).save(widget);
    }

    @Test
    void givenId_thenExistsById_assertResult() {
        // given
        UUID id = UUID.randomUUID();

        // when
        when(h2WidgetRepository.existsById(id)).thenReturn(true);

        // then
        boolean result = sqlWidgetRepositoryImpl.existsById(id);

        // assert
        assertTrue(result);

        // verify
        verify(h2WidgetRepository).existsById(id);
    }

    @Test
    void givenWidget_thenUpdate_assertResult() {
        // given
        Widget widget = TestHelper.getWidget();

        // when
        when(h2WidgetRepository.save(widget)).thenReturn(widget);

        // then
        Widget result = sqlWidgetRepositoryImpl.update(widget);

        // assert
        assertNotNull(result);

        // verify
        verify(h2WidgetRepository).save(widget);
    }

    @Test
    void givenId_thenDelete_assertResult() {
        // given
        UUID id = UUID.randomUUID();

        // when
        doNothing().when(h2WidgetRepository).deleteById(id);

        // then
        sqlWidgetRepositoryImpl.delete(id);

        // verify
        verify(h2WidgetRepository).deleteById(id);
    }

    @Test
    void givenMaxZ_thenGetMaxZ_assertResult() {
        //given
        int maxZ = 10;

        // when
        when(h2WidgetRepository.findMaxZ()).thenReturn(maxZ);

        // then
        int result = sqlWidgetRepositoryImpl.getMaxZ();

        // assert
        assertEquals(maxZ, result);

        // verify
        verify(h2WidgetRepository).findMaxZ();
    }

    @Test
    void givenZ_thenIsZExist_assertResult() {
        //given
        final int Z = 10;

        // when
        when(h2WidgetRepository.existsWidgetByZ(Z)).thenReturn(true);

        // then
        boolean result = sqlWidgetRepositoryImpl.isZExist(Z);

        // assert
        assertTrue(result);

        // verify
        verify(h2WidgetRepository).existsWidgetByZ(Z);
    }

    @Test
    void givenThreeWidgets_whenNotInRow_thenGetWidgetsWithZGreaterThanOrEqual_assertEmptyResult() {
        // given
        int Z2 = 20;
        int Z3 = 30;
        int Z_FILTER = 11;
        List<Widget> widgets = List.of(TestHelper.getWidget(Z2), TestHelper.getWidget(Z3));

        // given
        when(h2WidgetRepository.findAllByZGreaterThanEqualOrderByZAsc(Z_FILTER)).thenReturn(widgets);

        // then
        List<Widget> result = sqlWidgetRepositoryImpl.getWidgetsWithZGreaterThanOrEqual(Z_FILTER);

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // since there is no Zs in row
    }

    @Test
    void givenListOfWidgets_whenInRow_thenGetWidgetsWithZGreaterThanOrEqual_assertResultForFilteredWidgets() {
        // given
        int Z1 = 20;
        int Z2 = 21;
        int Z3 = 25; // shouldn't contain this one
        int Z_FILTER = 19;
        List<Widget> filteredList = List.of(TestHelper.getWidget(Z3), TestHelper.getWidget(Z1), TestHelper.getWidget(Z2));

        // given
        when(h2WidgetRepository.findAllByZGreaterThanEqualOrderByZAsc(Z_FILTER)).thenReturn(filteredList);

        // then
        List<Widget> result = sqlWidgetRepositoryImpl.getWidgetsWithZGreaterThanOrEqual(Z_FILTER);

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Z1, result.get(0).getZ());
        assertEquals(Z2, result.get(1).getZ());
    }

    // region findAll
    @Test
    void givenPageSizeAndPageIndex_whenCountLessThanPageSizeAndPageIndex_thenFindAll_assertEmptyResult() {
        // given
        int pageIndex = 1; // means 2nd page
        int pageSize = 10;

        // when
        when(h2WidgetRepository.count()).thenReturn(8L); // 1 * 10 > 8

        // then
        List<Widget> widgets = sqlWidgetRepositoryImpl.findAll(pageSize, pageIndex);

        // assert
        assertNotNull(widgets);
        assertTrue(widgets.isEmpty());
    }

    @Test
    void givenPageSizeAndPageIndex_thenFindAll_assertResult() {
        // given
        int pageIndex = 0;
        int pageSize = 10;
        int Z1 = 10;
        int Z2 = 20;
        int Z3 = 30;
        List<Widget> widgetList = List.of(TestHelper.getWidget(Z3), TestHelper.getWidget(Z2), TestHelper.getWidget(Z1));

        // when
        when(h2WidgetRepository.count()).thenReturn(15L); // 1st * 10 < 15
        when(h2WidgetRepository.findAllWithPagination(PageRequest.of(pageIndex, pageSize))).thenReturn(new PageImpl<>(widgetList));

        // then
        List<Widget> widgets = sqlWidgetRepositoryImpl.findAll(pageSize, pageIndex);

        // assert
        assertNotNull(widgets);
        assertEquals(3, widgets.size());
    }
    // endregion findAll

    // region findAllInArea
    @Test
    void givenAreaAndPageSizeAndPageIndex_whenCountLessThanPageSizeAndPageIndex_thenFindAllInArea_assertEmptyResult() {
        // given
        int pageIndex = 1; // means 2nd page
        int pageSize = 10;
        Area area = mock(Area.class);

        // when
        when(h2WidgetRepository.count()).thenReturn(8L); // 1 * 10 > 8

        // then
        List<Widget> widgets = sqlWidgetRepositoryImpl.findAllInArea(area, pageSize, pageIndex);

        // assert
        assertNotNull(widgets);
        assertTrue(widgets.isEmpty());
    }

    @Test
    void givenAreaAndPageSizeAndPageIndex_thenFindAllInArea_assertResult() {
        // given
        final int pageIndex = 0;
        final int pageSize = 10;
        final int startX = 10;
        final int startY = 20;
        final int endX = 30;
        final int endY = 40;
        Area area = mock(Area.class);
        List<Widget> widgetList = List.of(TestHelper.getWidget(), TestHelper.getWidget(), TestHelper.getWidget());

        // when
        when(area.getStart()).thenReturn(new Point(startX, startY));
        when(area.getEnd()).thenReturn(new Point(endX, endY));
        when(h2WidgetRepository.count()).thenReturn(15L); // 1st * 10 < 15
        when(h2WidgetRepository.findAllInAreaWithPagination(
                PageRequest.of(pageIndex, pageSize), startX, startY, endX, endY
        )).thenReturn(new PageImpl<>(widgetList));

        // then
        List<Widget> widgets = sqlWidgetRepositoryImpl.findAllInArea(area, pageSize, pageIndex);

        // assert
        assertNotNull(widgets);
        assertEquals(3, widgets.size());
    }
    // endregion findAllInArea
}