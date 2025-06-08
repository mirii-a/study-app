package com.japanese.study_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> alreadyExistsExceptionHandler(AlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "GIVEN_ITEM_ALREADY_EXISTS_IN_REPOSITORY",
                "The item given already exists in the repository.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> categoryNotFoundException(CategoryNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "CATEGORY_NOT_FOUND",
                "The category sentence does not exist.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ExampleSentenceNotFoundException.class)
    public ResponseEntity<ApiError> exampleSentenceNotFoundException(ExampleSentenceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "EXAMPLE_SENTENCE_NOT_FOUND",
                "The example sentence does not exist.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WordNotFoundException.class)
    public ResponseEntity<ApiError> wordNotFoundException(WordNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "WORD_NOT_FOUND",
                "The word does not exist.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingExampleSentenceException.class)
    public ResponseEntity<ApiError> missingExampleSentenceException(MissingExampleSentenceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "MISSING_EXAMPLE_SENTENCE_FROM_REQUEST",
                "An example sentence is missing from the request. Please resubmit.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RandomWordRetrievalException.class)
    public ResponseEntity<ApiError> randomWordRetrievalException(RandomWordRetrievalException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "RANDOM_WORD_RETRIEVAL_ERROR",
                "An error has occurred while retrieving random words. Please resubmit.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> ioException(IOException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "ERROR_WITH_INPUT_OR_OUTPUT",
                "An error has occurred with input/output. Please resubmit.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(Collections.singletonList(errorResponse)));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> normalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "EXCEPTION_OCCURRED",
                "An error has occurred. Please resubmit.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(Collections.singletonList(errorResponse)));
    }
}
