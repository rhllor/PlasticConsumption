package com.github.rhllor.pc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.github.rhllor.pc.library", "com.github.rhllor.pc.service", })
@EntityScan("com.github.rhllor.pc.library.entity")
@EnableJpaRepositories("com.github.rhllor.pc.library")
public class ServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }
}