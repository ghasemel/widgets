package com.elyasi.assignments.widgets.web.controller;

import com.elyasi.assignments.widgets.domain.Area;
import com.elyasi.assignments.widgets.domain.Widget;
import com.elyasi.assignments.widgets.domain.WidgetList;
import com.elyasi.assignments.widgets.dto.WidgetListDto;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.service.WidgetService;
import com.elyasi.assignments.widgets.validator.AreaTag;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.elyasi.assignments.widgets.constant.ControllerConstant.*;

/**
 * Created by Ghasem on 27/03/2021
 */
@Api(value = WIDGET_API_VERSION_1)
@Slf4j
@RestController
@RequestMapping(WIDGET_ENDPOINT)
@RequiredArgsConstructor
@Validated
public class WidgetController {
    private final WidgetService widgetService;
    private final ConversionService conversion;

    @ApiOperation(
            value = API_OPERATION_GET_WIDGET_BY_ID,
            notes = API_OPERATION_GET_WIDGET_BY_ID_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_OK),
            @ApiResponse(code = 404, message = API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_NOT_FOUND)})
    @GetMapping(path = "/{" + ID_PATH_VAR + "}", produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> getWidget(@ApiParam(value = ID_DESC, required = true)
                                               @PathVariable(ID_PATH_VAR) UUID id) {
        log.info("get request for widget by id: {}", id);

        WidgetDto widgetDto = conversion.convert(widgetService.getById(id), WidgetDto.class);

        return new ResponseEntity<>(widgetDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = API_OPERATION_CREATE_WIDGET,
            notes = API_OPERATION_CREATE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_OPERATION_CREATE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_CREATE_WIDGET_MESSAGE_ERROR)})
    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> create(@RequestBody @Valid WidgetDto widgetDto) {

        log.info("create request for widgetDto: {}", widgetDto);

        // convert
        Widget inputWidget = conversion.convert(widgetDto, Widget.class);

        // add widgetDto
        WidgetDto result = conversion.convert(widgetService.create(inputWidget), WidgetDto.class);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = API_OPERATION_UPDATE_WIDGET,
            notes = API_OPERATION_UPDATE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_OPERATION_UPDATE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_UPDATE_WIDGET_MESSAGE_ERROR)})
    @PutMapping(path = "/{" + ID_PATH_VAR + "}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> update(@PathVariable(ID_PATH_VAR) UUID id, @RequestBody @Valid WidgetDto widgetDto) {

        log.info("update request for widgetDto: {}", widgetDto);

        widgetDto.setId(id);

        // convert
        Widget inputWidget = conversion.convert(widgetDto, Widget.class);

        // update
        WidgetDto result = conversion.convert(widgetService.update(inputWidget), WidgetDto.class);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(
            value = API_OPERATION_DELETE_WIDGET,
            notes = API_OPERATION_DELETE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = API_OPERATION_DELETE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_DELETE_WIDGET_MESSAGE_ERROR)})
    @DeleteMapping(path = "/{" + ID_PATH_VAR + "}", produces = APPLICATION_JSON)
    public ResponseEntity<HttpStatus> delete(@PathVariable(ID_PATH_VAR) UUID id) {

        log.info("delete request for widget: {}", id);

        widgetService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = API_OPERATION_GET_LIST_WIDGET,
            notes = API_OPERATION_GET_LIST_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_OPERATION_GET_LIST_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 404, message = API_OPERATION_GET_LIST_WIDGET_MESSAGE_ERROR)})
    @GetMapping(produces = APPLICATION_JSON)
    public ResponseEntity<WidgetListDto> getWidgetsList(
            @ApiParam(value = AREA_FILTER_DESC)
            @RequestParam(value = AREA_FILTER, required = false) @AreaTag String areaFilter,
            @ApiParam(value = PAGE_INDEX_DESC)
            @RequestParam(value = PAGE_INDEX, required = false) Integer pageIndex,
            @ApiParam(value = PAGE_SIZE_DESC)
            @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize) {

        log.info("get request for widget list, areaFilter: {}, startIndex: {}, pageSize: {}", areaFilter, pageIndex, pageSize);

        // convert area
        Area area = conversion.convert(areaFilter, Area.class);

        // retrieve widgets
        WidgetList widgetList = widgetService.getList(area, pageSize, pageIndex);

        // convert to WidgetListDto
        WidgetListDto widgetDtoList = conversion.convert(widgetList, WidgetListDto.class);

        return new ResponseEntity<>(widgetDtoList, HttpStatus.OK);
    }
}
