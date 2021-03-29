package com.elyasi.assignments.widgets.repository;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.domain.Widget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepositoryTest {

    private InMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
    }

    @Test
    void givenNotExistId_thenFindById_assertOptionalEmpty() {
        // given
        UUID id = UUID.randomUUID();

        // then
        Optional<Widget> result = repository.findById(id);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenWidget_thenInsert_assertMaxZAndIdAndExist() {
        // given
        Widget widget = TestHelper.getWidget();
        widget.setId(null);

        // then
        Widget result = repository.insert(widget);

        // assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(repository.findById(result.getId()).isPresent());
        assertEquals(widget.getZ(), repository.getMaxZ());
    }

    @Test
    void givenMaxZGreaterThanWidgetZ_thenInsert_assertNoChangeToMaxZ() {
        // given
        // to ensure max z has value
        Widget previousWidget = TestHelper.getWidget();
        previousWidget.setZ(20);
        previousWidget = repository.insert(previousWidget);

        Widget widget = TestHelper.getWidget();
        // any value less than previous one
        widget.setZ(previousWidget.getZ() - 10);

        // then
        Widget insert = repository.insert(widget);

        // assert
        assertNotNull(insert.getId());
        assertTrue(repository.findById(insert.getId()).isPresent());
        assertEquals(previousWidget.getZ(), repository.getMaxZ());
    }

    @Test
    void givenExistWidget_thenExistsById_assertResult() {
        // given
        Widget existWidget = repository.insert(TestHelper.getWidget());

        // then
        boolean result = repository.existsById(existWidget.getId());

        // assert
        assertTrue(result);
    }

    @Test
    void givenNonExistWidget_thenExistsById_assertResult() {
        // given
        UUID id = UUID.randomUUID();

        // then
        boolean result = repository.existsById(id);

        // assert
        assertFalse(result);
    }

    @Test
    void givenWidget_thenUpdate_assertMaxZAndResult() {
        // given
        int NEW_Z = -60;
        Widget givenWidget = repository.insert(TestHelper.getWidget(-50));
        givenWidget.setZ(NEW_Z);

        // then
        Widget update = repository.update(givenWidget);

        // assert
        assertNotNull(update);
        assertNotNull(update.getId());
        assertTrue(repository.findById(givenWidget.getId()).isPresent());
        assertEquals(NEW_Z, repository.findById(givenWidget.getId()).get().getZ());
        assertEquals(NEW_Z, repository.getMaxZ());
    }

    @Test
    void givenMaxZGreaterThanWidgetZ_thenUpdate_assertResultAndNoChangeToMaxZ() {
        // given
        int MAX_Z = -50;
        int NEW_Z = -60;
        repository.insert(TestHelper.getWidget(MAX_Z));
        Widget givenWidget = repository.insert(TestHelper.getWidget(NEW_Z));

        // then
        Widget update = repository.update(givenWidget);

        // assert
        assertNotNull(update);
        assertNotNull(update.getId());
        assertTrue(repository.findById(update.getId()).isPresent());
        assertEquals(NEW_Z, repository.findById(update.getId()).get().getZ());
        assertEquals(MAX_Z, repository.getMaxZ());
    }

    @Test
    void givenWidget_thenDelete_assertRemoved() {
        // given
        Widget givenWidget = repository.insert(TestHelper.getWidget());

        // then
        repository.delete(givenWidget.getId());

        // assert
        assertTrue(repository.findById(givenWidget.getId()).isEmpty());
        assertEquals(0, repository.getMaxZ());
    }

    @Test
    void givenExistZ_thenIsZExist_assertExists() {
        // given
        int z = 10;
        repository.insert(TestHelper.getWidget(z));

        // then
        boolean result = repository.isZExist(z);

        // assert
        assertTrue(result);
    }

    @Test
    void givenNotExistZ_thenIsZExist_assertDoesNotExist() {
        // given
        repository.insert(TestHelper.getWidget(10));
        int notExistZ = 20;

        // then
        boolean result = repository.isZExist(notExistZ);

        // assert
        assertFalse(result);
    }

    @Test
    void givenThreeWidgetsNotInRow_thenGetWidgetsWithZGreaterThanOrEqual_assertEmptyResult() {
        // given
        int Z1 = 10;
        int Z2 = 20;
        int Z3 = 30;
        int Z_FILTER = 11;
        repository.insert(TestHelper.getWidget(Z1));
        repository.insert(TestHelper.getWidget(Z2));
        repository.insert(TestHelper.getWidget(Z3));

        // then
        List<Widget> widgets = repository.getWidgetsWithZGreaterThanOrEqual(Z_FILTER);

        // assert
        assertNotNull(widgets);
        assertTrue(widgets.isEmpty());
    }

    @Test
    void givenThreeWidgetsInRow_thenGetWidgetsWithZGreaterThanOrEqual_assertEmptyResult() {
        // given
        int Z1 = 10;
        int Z2 = 20;
        int Z3 = 21;
        int Z_FILTER = 19;
        repository.insert(TestHelper.getWidget(Z1));
        repository.insert(TestHelper.getWidget(Z2));
        repository.insert(TestHelper.getWidget(Z3));

        // then
        List<Widget> widgets = repository.getWidgetsWithZGreaterThanOrEqual(Z_FILTER);

        // assert
        assertNotNull(widgets);
        assertEquals(2, widgets.size());
        assertEquals(Z2, widgets.get(0).getZ());
        assertEquals(Z3, widgets.get(1).getZ());
    }
}