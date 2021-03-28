package com.elyasi.assignments.widgets.web.controller;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.config.Config;
import com.elyasi.assignments.widgets.constant.ControllerConstant;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import com.elyasi.assignments.widgets.repository.WidgetRepository;
import com.elyasi.assignments.widgets.validator.AreaTag;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static com.elyasi.assignments.widgets.TestHelper.asJsonString;
import static com.elyasi.assignments.widgets.TestHelper.asWidgetObj;
import static com.elyasi.assignments.widgets.constant.ControllerConstant.WIDGET_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class WidgetControllerITest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Config config;

    @Autowired // to clean up before each test
    protected WidgetRepository repository;

    private WidgetDto createdWidget;

    @AfterEach
    void tearDown() {
        // clean up repository
        List<Widget> all = repository.findAll(2000, 0);
        all.forEach(w -> {
            repository.delete(w.getId());
        });
    }


    @Test
    @Order(1)
    void givenValidWidget_thenPost_assertCreated() throws Exception {
        // given
        WidgetDto widgetDto = TestHelper.getWidgetDto();

        // then
        MvcResult mvcResult = mockMvc.perform(
                post(WIDGET_ENDPOINT)
                        .content(asJsonString(widgetDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )

                // assert
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.x").value(widgetDto.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.y").value(widgetDto.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.z").value(widgetDto.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.width").value(widgetDto.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value(widgetDto.getHeight()))
                .andReturn();

        assertDoesNotThrow(() -> {
            WidgetDto widgetObj = asWidgetObj(mvcResult.getResponse().getContentAsString());
            assertNotNull(widgetObj.getId());
            createdWidget = widgetObj;
        });
    }

    @Test
    @Order(2)
    void givenWidgetWithId_thenPost_assertBadRequest() throws Exception {
        // given
        WidgetDto widgetDto = TestHelper.getWidgetDtoWithId();

        // then
        mockMvc.perform(
                post(WIDGET_ENDPOINT)
                        .content(asJsonString(widgetDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )

                // assert
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(new MutabilityException(WidgetDto.ID_NAME).getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @Order(3)
    void givenNotExistId_thenGetById_assertNotFound() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT + "/{id}", id).accept(MediaType.APPLICATION_JSON))

                // assert
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(new WidgetNotFoundException(id).getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @Order(4)
    void givenExistId_thenGetById_assertOK() throws Exception {
        // given
        givenValidWidget_thenPost_assertCreated();
        UUID id = createdWidget.getId();

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT + "/{id}", id).accept(MediaType.APPLICATION_JSON))

                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdWidget.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.x").value(createdWidget.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.y").value(createdWidget.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.z").value(createdWidget.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.width").value(createdWidget.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value(createdWidget.getHeight()));
    }

    @Test
    @Order(5)
    void givenNotExistWidget_thenUpdate_assertNotFound() throws Exception {
        // given
        WidgetDto widgetDto = TestHelper.getWidgetDtoWithId();

        // then
        mockMvc.perform(put(WIDGET_ENDPOINT + "/{id}", widgetDto.getId())
                .content(asJsonString(widgetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

                // assert
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(new WidgetNotFoundException(widgetDto.getId()).getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @Order(6)
    void givenExistWidget_thenUpdate_assertOK() throws Exception {
        // given
        givenValidWidget_thenPost_assertCreated();

        createdWidget.setWidth(TestHelper.getRandomNumGreaterThanZero());
        createdWidget.setHeight(TestHelper.getRandomNumGreaterThanZero());
        createdWidget.setX(TestHelper.getRandomNum());
        createdWidget.setY(TestHelper.getRandomNum());
        createdWidget.setZ(TestHelper.getRandomNum());


        // then
        mockMvc.perform(put(WIDGET_ENDPOINT + "/{id}", createdWidget.getId())
                .content(asJsonString(createdWidget))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdWidget.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.x").value(createdWidget.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.y").value(createdWidget.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.z").value(createdWidget.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.width").value(createdWidget.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value(createdWidget.getHeight()));
    }

    @Test
    @Order(7)
    void givenNotExistWidget_thenDelete_assertNotFound() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // then
        mockMvc.perform(delete(WIDGET_ENDPOINT + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
                // assert
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(new WidgetNotFoundException(id).getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @Order(8)
    void givenExistWidget_thenDelete_assertOK() throws Exception {
        // given
        givenValidWidget_thenPost_assertCreated();


        // then
        mockMvc.perform(delete(WIDGET_ENDPOINT + "/{id}", createdWidget.getId())
                .accept(MediaType.APPLICATION_JSON)
        )
                // assert
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void givenEmptyDataSet_thenGetAll_assertEmptyList() throws Exception {
        // given
        // empty dataset

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
        )
                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(config.getDefaultPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets").exists());
    }

    @Test
    @Order(10)
    void givenTwoWidgets_thenGetAll_assertListWithSortByZ() throws Exception {
        // given
        givenValidWidget_thenPost_assertCreated();
        WidgetDto widgetGreater = createdWidget;


        givenValidWidget_thenPost_assertCreated();
        WidgetDto widgetLessZ = createdWidget;

        // widgetWithSmallerZIndex
        if (widgetGreater.getZ() < widgetLessZ.getZ()) {
            WidgetDto tmp = widgetLessZ;
            widgetLessZ = widgetGreater;
            widgetGreater = tmp;
        }

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
        )
                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(config.getDefaultPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets").exists())

                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].id").value(widgetLessZ.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].x").value(widgetLessZ.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].y").value(widgetLessZ.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].z").value(widgetLessZ.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].width").value(widgetLessZ.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].height").value(widgetLessZ.getHeight()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].id").value(widgetGreater.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].x").value(widgetGreater.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].y").value(widgetGreater.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].z").value(widgetGreater.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].width").value(widgetGreater.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[1].height").value(widgetGreater.getHeight()));
    }

    @Test
    @Order(11)
    void givenInvalidAreaFilter_thenGetAllWithFilter_assertBadRequest() throws Exception {
        // given
        String invalidAreaFilter = "3123,4534:64645,8667";

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam(ControllerConstant.AREA_FILTER, invalidAreaFilter)
        )
                // assert
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("getWidgetsList.areaFilter: " + AreaTag.MSG))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    @Test
    @Order(12)
    void givenAreaFilterOutsideWidgetsArea_thenGetAllWithFilter_assertCorrectResult() throws Exception {
        // given
        String areaFilter = "3123:4534,64645:8667";
        givenValidWidget_thenPost_assertCreated();
        givenValidWidget_thenPost_assertCreated();


        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam(ControllerConstant.AREA_FILTER, areaFilter)
        )
                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(config.getDefaultPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets").isEmpty());
    }

    @Test
    @Order(13)
    void givenAreaFilterContainsWidgetArea_thenGetAllWithFilter_assertCorrectResult() throws Exception {
        // given
        String areaFilter = "-2000:-2000,2000:2000";
        givenValidWidget_thenPost_assertCreated();
        givenValidWidget_thenPost_assertCreated();


        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam(ControllerConstant.AREA_FILTER, areaFilter)
        )
                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(config.getDefaultPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets").isNotEmpty());
    }

    @Test
    @Order(14)
    void givenPageSizeAndPageIndex_thenGetAll_assertCorrectPage() throws Exception {
        // given
        int pageSize = 1;
        int pageIndex = 2;

        givenValidWidget_thenPost_assertCreated();
        WidgetDto widgetGreater = createdWidget;

        givenValidWidget_thenPost_assertCreated();
        WidgetDto widgetLessZ = createdWidget;

        // widget with greater z-index
        if (widgetGreater.getZ() < widgetLessZ.getZ()) {
            widgetGreater = widgetLessZ;
        }

        // then
        mockMvc.perform(get(WIDGET_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam(ControllerConstant.PAGE_SIZE, String.valueOf(pageSize))
                .queryParam(ControllerConstant.PAGE_INDEX, String.valueOf(pageIndex))
        )
                // assert
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(pageSize))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(pageIndex))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets").exists())

                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].id").value(widgetGreater.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].x").value(widgetGreater.getX()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].y").value(widgetGreater.getY()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].z").value(widgetGreater.getZ()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].width").value(widgetGreater.getWidth()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.widgets[0].height").value(widgetGreater.getHeight()));
    }
}
