package com.elyasi.assignments.widgets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetDto implements Serializable {
    private static final long serialVersionUID = -2507554021211870226L;

    @Setter(AccessLevel.PRIVATE)
    private UUID id;

    private int x;
    private int y;
    private Integer z;

    @Positive(message = "width should be greater than zero")
    private int width;

    @Positive(message = "height should be greater than zero")
    private int height;

    public static final String ID_NAME = "id";
}
