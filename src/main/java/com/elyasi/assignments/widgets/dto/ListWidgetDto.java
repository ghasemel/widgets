package com.elyasi.assignments.widgets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ListWidgetDto {
    int pageSize;
    int pageIndex;
    int count;
    List<WidgetDto> widgets;
}
