package com.miro.assignments.widgets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by taaelgh1 on 27/03/2021
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDto implements Serializable {
    private static final long serialVersionUID = 3507115715760061761L;

    private final Date timestamp;
    private int status;
    private String message;
    private List<String> messages;
}
