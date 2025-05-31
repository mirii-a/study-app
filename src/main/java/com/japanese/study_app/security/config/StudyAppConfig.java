package com.japanese.study_app.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudyAppConfig {

    public StudyAppConfig(){}

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
