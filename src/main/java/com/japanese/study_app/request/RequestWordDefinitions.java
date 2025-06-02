package com.japanese.study_app.request;

import java.util.Set;

public record RequestWordDefinitions(
        Set<String> english,
        Set<String> japanese
) {
}
