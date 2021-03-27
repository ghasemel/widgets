package com.miro.assignments.widgets.constant;

/**
 * Created by Ghasem on 27/03/2021
 */
public class ControllerConstant {
    private ControllerConstant() { }

    public static final String WIDGET_API_VERSION = "1";
    public static final String WIDGET_ENDPOINT = "/widget";

    public static final String APPLICATION_JSON = "application/json";

    // get widget by id
    public static final String API_OPERATION_GET_WIDGET_BY_ID = "Retrieve widget by id";
    public static final String API_OPERATION_GET_WIDGET_BY_ID_LINK = "https://sampleLinkToDocumentation/widget/v" + WIDGET_API_VERSION;
    public static final String API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_OK = "Requested widget has been founded and retrieved";
    public static final String API_OPERATION_FOUND_WIDGET_BY_ID_MESSAGE_NOT_FOUND = "Requested widget not found";

    // create widget
    public static final String API_OPERATION_CREATE_WIDGET = "Create a widget";
    public static final String API_OPERATION_CREATE_WIDGET_LINK = API_OPERATION_GET_WIDGET_BY_ID_LINK;
    public static final String API_OPERATION_CREATE_WIDGET_MESSAGE_OK = "Widget has been created";
    public static final String API_OPERATION_CREATE_WIDGET_MESSAGE_ERROR = "Error on widget creation";

    // update widget
    public static final String API_OPERATION_UPDATE_WIDGET = "Update a widget";
    public static final String API_OPERATION_UPDATE_WIDGET_LINK = API_OPERATION_GET_WIDGET_BY_ID_LINK;
    public static final String API_OPERATION_UPDATE_WIDGET_MESSAGE_OK = "Widget has been updated";
    public static final String API_OPERATION_UPDATE_WIDGET_MESSAGE_ERROR = "Error on widget update";

    // update widget
    public static final String API_OPERATION_DELETE_WIDGET = "Delete a widget";
    public static final String API_OPERATION_DELETE_WIDGET_LINK = API_OPERATION_GET_WIDGET_BY_ID_LINK;
    public static final String API_OPERATION_DELETE_WIDGET_MESSAGE_OK = "Widget has been delete";
    public static final String API_OPERATION_DELETE_WIDGET_MESSAGE_ERROR = "Error on widget delete";

    // get widgets list
    public static final String API_OPERATION_GET_LIST_WIDGET = "Retrieve list of widgets";
    public static final String API_OPERATION_GET_LIST_WIDGET_LINK = API_OPERATION_GET_WIDGET_BY_ID_LINK;
    public static final String API_OPERATION_GET_LIST_WIDGET_MESSAGE_OK = "List of widgets have been retrieved";
    public static final String API_OPERATION_GET_LIST_WIDGET_MESSAGE_ERROR = "No widget has been found";

    // area filter
    public static final String AREA_FILTER_DESC = "Area filter, acceptable format is X1:Y1,X2:Y2";
    public static final String AREA_FILTER = "areaFilter";

    // start index
    public static final String PAGE_INDEX_DESC = "The 1-based index of pages";
    public static final String PAGE_INDEX = "pageIndex";

    public static final String PAGE_SIZE_DESC = "Specifies the desired maximum number of query results per page";
    public static final String PAGE_SIZE = "pageSize";

    public static final String ID_DESC = "Unique id of widget (UUID)";
    public static final String ID_PATH_VAR = "id";
}
