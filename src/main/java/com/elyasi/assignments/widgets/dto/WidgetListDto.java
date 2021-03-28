package com.elyasi.assignments.widgets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class WidgetListDto {
    int pageSize;
    int pageIndex;
    @Setter(AccessLevel.PRIVATE)
    int count;
    List<WidgetDto> widgets;

    public void setWidgets(List<WidgetDto> widgets) {
        this.widgets = widgets;
        count = widgets != null ? widgets.size() : 0;
    }
}
