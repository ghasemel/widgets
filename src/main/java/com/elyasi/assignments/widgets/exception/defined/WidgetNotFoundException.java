package com.elyasi.assignments.widgets.exception.defined;

import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException(UUID uuid) {
        super(String.format("widget [%s] not found", uuid.toString()));
    }
}
