package com.japanese.study_app.exceptions;

public class MissingExampleSentenceException extends RuntimeException{
    public MissingExampleSentenceException(String message){
        super(message);
    }
}
