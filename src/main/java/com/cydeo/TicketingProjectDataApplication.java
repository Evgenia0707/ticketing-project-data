package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication//this includes @Configuration
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    //I am trying to add bean in the container through @Bean annotation
    //Create class annotated with @Configuration (@SpringBootApplication)
    //Write a method which return the object that you're trying to add to the container (public ModelMapper )
    //annot this method with @Bean

    //like creating flyway

    //when using 3 party liberty

    @Bean//mapper - 3 party Library use @Bean
    public ModelMapper mapper(){//for convert DTO , Entity
        return new ModelMapper();
    }

}
