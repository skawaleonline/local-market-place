package com.lmp.app.bootup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.lmp.config.ConfigProperties;
import com.lmp.db.pojo.Item;
import com.lmp.db.pojo.Store;
import com.lmp.db.repository.ItemRepository;
import com.lmp.db.repository.StoreRepository;
import com.lmp.solr.indexer.SolrIndexer;

@Component
public class AppBootUp {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ConfigProperties prop;
  @Autowired
  private ItemRepository itemRepo;
  @Autowired
  private StoreRepository storeRepo;
  @Autowired
  private SolrIndexer indexer;

  public void buildItemRepo() throws IOException, SolrServerException {
    ObjectMapper objectMapper = new ObjectMapper();
    if(!prop.isDataSeedEnabled() || prop.getDataSeedFile() == null 
        || prop.getDataSeedFile().isEmpty()) {
      return ;
    }

    itemRepo.deleteAll();
    indexer.deleteAll();
    storeRepo.deleteAll();
    for(String file : prop.getDataSeedFile()) {
      List<Item> items = objectMapper.readValue(
          new File(file)
          , new TypeReference<List<Item>>(){});
      List<String> categories = getCategoriesFromFileName(file);
      logger.info("Seeding data file: " + file);
      logger.info("categories for file: " + file + " categories: " + categories.toString());
      for (Item item : items) {
        item.setCategory(categories);
        itemRepo.save(item);
      }
      // index documents
      indexer.addToIndex(items);
      logger.info("Added & indexed " + items.size() + " items for categories: " + categories.toString());
    }
    logger.info("Seeding store locations: src/main/data/store_locations.json");
    List<Store> stores = objectMapper.readValue(
        new File("src/main/data/store_locations.json")
        , new TypeReference<List<Store>>(){});
    for (Store store : stores) {
      storeRepo.save(store);
    }
    
  }

  private List<String> getCategoriesFromFileName(String fPath) {
    List<String> categories = new ArrayList<>(); 
    if(fPath == null || fPath.isEmpty()) {
      return categories;
    }
    String tokens[] = fPath.split("/"); // get the fName;
    String fName = tokens[tokens.length - 1].split("\\.")[0];
    Joiner joiner = Joiner.on(" ").skipNulls();
    // categories are separated by "_"
    for(String str : fName.split("_")) {
      // words with white space are seperated by "-"
      categories.add(joiner.join(Splitter.on('-').split(str)));
    }
    return categories;
  }
}
