package com.miro.assignments.widgets.controller;

import com.miro.assignments.widgets.dto.WidgetDto;
import com.miro.assignments.widgets.service.WidgetService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.miro.assignments.widgets.constant.ControllerConstant.*;

/**
 * Created by taaelgh1 on 27/03/2021
 */
@Api(value = WIDGET_API_VERSION)
@Slf4j
@RestController
@RequestMapping(WIDGET_ENDPOINT)
@RequiredArgsConstructor
public class WidgetController {
    private final WidgetService widgetService;

    @ApiOperation(
            value = API_OPERATION_GET_WIDGET_BY_ID,
            notes = API_OPERATION_GET_WIDGET_BY_ID_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_OK),
            @ApiResponse(code = 404, message = API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_NOT_FOUND)})
    @GetMapping(path = "/{" + ID_PATH_VAR +  "}", produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> getWidget(@ApiParam(value = ID_DESC, required = true)
                                               @PathVariable(ID_PATH_VAR) UUID id) {
        log.info("get request for widget by id: {}", id);

        WidgetDto widgetDto = widgetService.getById(id);

        return new ResponseEntity<>(widgetDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = API_OPERATION_CREATE_WIDGET,
            notes = API_OPERATION_CREATE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_OPERATION_CREATE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_CREATE_WIDGET_MESSAGE_ERROR)})
    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> create(@RequestBody @Valid WidgetDto widget) {

        log.info("create request for widget: {}", widget);

        WidgetDto widgetDto = widgetService.create(widget);

        return new ResponseEntity<>(widgetDto, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = API_OPERATION_UPDATE_WIDGET,
            notes = API_OPERATION_UPDATE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_OPERATION_UPDATE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_UPDATE_WIDGET_MESSAGE_ERROR)})
    @PutMapping(path = "/{" + ID_PATH_VAR +  "}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<WidgetDto> update(@PathVariable(ID_PATH_VAR) UUID id, @RequestBody @Valid WidgetDto widget) {

        log.info("update request for widget: {}", widget);

        WidgetDto widgetDto = widgetService.update(id, widget);

        return new ResponseEntity<>(widgetDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = API_OPERATION_DELETE_WIDGET,
            notes = API_OPERATION_DELETE_WIDGET_LINK)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = API_OPERATION_DELETE_WIDGET_MESSAGE_OK),
            @ApiResponse(code = 400, message = API_OPERATION_DELETE_WIDGET_MESSAGE_ERROR)})
    @PutMapping(path = "/{" + ID_PATH_VAR +  "}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
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
    public ResponseEntity<List<WidgetDto>> getWidgetsList(
            @ApiParam(value = AREA_FILTER)
            @RequestParam(value = AREA_FILTER, required = false) String areaFilter,
            @ApiParam(value = START_INDEX_DESC)
            @RequestParam(value = START_INDEX, required = false) Integer startIndex,
            @ApiParam(value = PAGE_SIZE_DESC)
            @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize) {
        log.debug("get request for widget list, areaFilter: {}, startIndex: {}, pageSize: {}",
                areaFilter, startIndex, pageSize);

        List<WidgetDto> widgetList = widgetService.getList(areaFilter, pageSize, startIndex);

        return new ResponseEntity<>(widgetList, HttpStatus.OK);
    }
}
