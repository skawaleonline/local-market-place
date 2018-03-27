package com.lmp.app.bootup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmp.app.utils.FileIOUtil;
import com.lmp.config.ConfigProperties;
import com.lmp.db.pojo.Item;
import com.lmp.db.pojo.Store;
import com.lmp.db.pojo.StoreInventory;
import com.lmp.db.repository.ItemRepository;
import com.lmp.db.repository.StoreInventoryRepository;
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
  private StoreInventoryRepository siRepo;
  @Autowired
  private SolrIndexer indexer;

  private void seedOneCategory(File file, List<Store> stores) throws IOException, SolrServerException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Item> items = objectMapper.readValue(file, new TypeReference<List<Item>>() {});
    logger.info("Seeding data file: {}", file.getName());
    Iterator<Item> it = items.listIterator();
    while(it.hasNext()) {
      Item item = it.next();
      try {
        itemRepo.save(item);
        fillStoreInventory(item, stores);
      } catch (DuplicateKeyException e) {
        logger.debug("found duplicate item upc {}", item.getUpc());
        //it.remove();
      }
    }
    // index documents
    indexer.addToIndex(items);
    logger.info("Added & indexed {} items from file {}", items.size(), file.getName());
    FileIOUtil.writeProgress(prop.getSeededFiles(), file.getName());
  }

  private void fillStoreInventory() {
    List<Item> items = itemRepo.findAll();
    List<Store> stores = storeRepo.findAll();
    for(Item item : items) {
      fillStoreInventory(item, stores);
    }
  }
  
  private void fillStoreInventory(Item item, List<Store> stores) {
    for(Store store : stores) {
      if(item.canGoOnStoreInventory(store)) {
        StoreInventory sItem = new StoreInventory();
        long time = System.currentTimeMillis();
        sItem.setStoreId(store.getId());
        sItem.setItem(item);
        sItem.setAdded(time);
        sItem.setUpdated(time);
        siRepo.save(sItem);
      }
    }
  }
  public void buildItemRepo() throws IOException, SolrServerException {
    if(!prop.isDataSeedEnabled() || prop.getDataSeedDir() == null 
        || prop.getDataSeedDir().isEmpty()) {
      return ;
    }
    storeRepo.deleteAll();
    siRepo.deleteAll();
    logger.info("Seeding store locations: " + prop.getStoreSeedFile());
    ObjectMapper objectMapper = new ObjectMapper();
    List<Store> stores = objectMapper.readValue(
        new File(prop.getStoreSeedFile())
        , new TypeReference<List<Store>>(){});
    storeRepo.saveAll(stores);

    List<File> files = FileIOUtil.getAllFilesInDir(prop.getDataSeedDir());
    if(files == null || files.isEmpty()) {
      return ;
    }
    if(prop.isCleanupAndSeedData()) {
      itemRepo.deleteAll();
      indexer.deleteAll();
      siRepo.deleteAll();
      FileIOUtil.deleteFile(prop.getSeededFiles());
    } else {
      //fillStoreInventory();
    }
    Set<String> processed = FileIOUtil.readProcessed(prop.getSeededFiles());
    for(File file : files) {
      if(processed.contains(file.getName())) {
        logger.info("skipping file: {}", file.getName());
        continue;
      }
      seedOneCategory(file, stores);
    }
  }
}
