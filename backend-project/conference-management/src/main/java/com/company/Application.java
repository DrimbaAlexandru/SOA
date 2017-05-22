package com.company;

import com.company.domain.TestEntity;
import com.company.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by AlexandruD on 15-May-17.
 */
@SpringBootApplication
public class Application {

    @Autowired
    private static TestEntityRepository repo;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

}
