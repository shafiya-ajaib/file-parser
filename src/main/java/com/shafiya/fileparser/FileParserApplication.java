package com.shafiya.fileparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class FileParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileParserApplication.class, args);
    }

}
