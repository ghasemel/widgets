package com.elyasi.assignments.widgets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetListDto implements Serializable {
    private static final long serialVersionUID = -6393001528051371935L;

    private int pageSize;
    private int pageIndex;
    private int count;
    private List<WidgetDto> widgets;

    public void setWidgets(List<WidgetDto> widgets) {
        this.widgets = widgets;
        count = widgets != null ? widgets.size() : 0;
    }
}
