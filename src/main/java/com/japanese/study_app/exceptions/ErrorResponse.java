package com.japanese.study_app.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty(value = "code", required = true) String code,
        @JsonProperty(value = "message", required = true) String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty("errorMessage") String errorMessage
) {

    public ErrorResponse(String code, String title) {this(code, title, null);}
}
