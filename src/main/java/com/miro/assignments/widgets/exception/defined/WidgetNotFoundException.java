package com.miro.assignments.widgets.exception.defined;

import java.util.UUID;

/**
 * Created by taaelgh1 on 27/03/2021
 */
public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException(UUID uuid) {
        super(String.format("widget [%s] not found", uuid.toString()));
    }
}
