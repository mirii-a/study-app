package com.japanese.study_app.response;


public record ApiResponse(
        String message,
        Object data
) {
}
