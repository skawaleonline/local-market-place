package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmp.db.setup.Item;
import com.lmp.db.setup.ItemRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication(scanBasePackages = { "com.lmp.db.setup" })
@EnableMongoRepositories(basePackages = {"com.lmp.db.setup"})
public class Application implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ItemRepository itemRepo;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  public void buildItemRepo() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Item> items = objectMapper.readValue(
        new File("src/main/data/beers_scrapped.json")
        , new TypeReference<List<Item>>(){});
    logger.info("Items: " + items.size());
    for (Item item : items) {
      itemRepo.save(item);
    }
  }

  @Override
  public void run(String... args) throws Exception {

    buildItemRepo();
    System.out.println("Items found with findAll():");
    for (Item item : itemRepo.findAll()) {
      System.out.println(item);
    }

  }

}
