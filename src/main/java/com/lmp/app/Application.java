package com.lmp.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.lmp.app.bootup.AppBootUp;

@SpringBootApplication(scanBasePackages = { "com.lmp" })
@EnableMongoRepositories(basePackages = {"com.lmp.db.repository"})
public class Application implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private AppBootUp appBootUp;
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }



  @Override
  public void run(String... args) throws Exception {
    appBootUp.buildItemRepo();
  }

}
