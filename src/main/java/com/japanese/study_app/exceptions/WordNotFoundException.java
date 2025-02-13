package com.japanese.study_app.exceptions;

public class WordNotFoundException extends RuntimeException{
    public WordNotFoundException(String message){
        super(message);
    }
}
