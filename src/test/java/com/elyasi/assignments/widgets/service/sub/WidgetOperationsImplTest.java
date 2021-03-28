package com.elyasi.assignments.widgets.service.sub;

import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WidgetOperationsImplTest {
    @Mock
    private WidgetRepository repository;

    private WidgetOperationsImpl widgetOperations;

    @Spy
    private ReadWriteLock lockSpy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        widgetOperations = new WidgetOperationsImpl(repository);
        widgetOperations.setLock(lockSpy);
    }

    @Test
    void givenId_thenReadWidget_assertLock() {
        // given
        UUID id = UUID.randomUUID();

        // when
        when(repository.findById(id)).thenReturn(Optional.empty());
        //when(lockSpy.readLock()).thenReturn(new ReentrantReadWriteLock.ReadLock());

        // then
        Optional<Widget> widget = widgetOperations.readWidget(id);

        // assert
        assertTrue(widget.isEmpty());

        // verify
        verify(lockSpy).readLock();
    }
}