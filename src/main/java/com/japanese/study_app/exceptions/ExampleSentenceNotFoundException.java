package com.japanese.study_app.exceptions;

public class ExampleSentenceNotFoundException extends RuntimeException{
    public ExampleSentenceNotFoundException(String message){
        super(message);
    }
}
