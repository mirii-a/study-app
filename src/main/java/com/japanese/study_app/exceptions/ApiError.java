package com.japanese.study_app.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApiError(
        @JsonProperty(value="errors", required = true) List<ErrorResponse> errors
) {
}
