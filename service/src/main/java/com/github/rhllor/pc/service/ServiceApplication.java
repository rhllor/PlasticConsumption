package com.github.rhllor.pc.service;

import com.github.rhllor.pc.library.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.github.rhllor.pc")
@RestController
public class ServiceApplication {

  private final MyService myService;

  public ServiceApplication(MyService myService) {
    this.myService = myService;
  }

  @GetMapping("/")
  public String home() {
    return myService.message();
  }

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }
}