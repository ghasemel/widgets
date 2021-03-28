package com.elyasi.assignments.widgets.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@Builder
public class WidgetList {
    int pageSize;
    int pageIndex;
    @Setter(AccessLevel.PRIVATE)
    int count;
    List<Widget> widgets;
}
