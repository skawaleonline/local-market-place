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
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
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

  private void writeProgress(String fName) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(prop.getSeededFiles(), true));
    writer.append(fName);
    writer.newLine();
    writer.close();
  }

  private Set<String> readProcessed() throws IOException {
    try(BufferedReader br = new BufferedReader(new FileReader(prop.getSeededFiles()))) {
      Set<String> set = new HashSet<>();
      String st = "";
      while ((st = br.readLine()) != null){
        set.add(st);
      }
      return set;
    } catch(FileNotFoundException e) {
      return new HashSet<>();
    }
  }

  private List<File> getAllFilesInDir(String dPath) {
    File folder = new File(dPath);
    File[] listOfFiles = folder.listFiles();
    List<File> files = new ArrayList<>();
    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        files.add(listOfFiles[i]);
      }
    }
    return files;
  }

  private void deleteFile(String fName) {
    File file = new File(fName);
    if(file.exists()) {
      file.delete();
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

  public void buildItemRepo() throws IOException, SolrServerException {
    ObjectMapper objectMapper = new ObjectMapper();
    if(!prop.isDataSeedEnabled() || prop.getDataSeedDir() == null 
        || prop.getDataSeedDir().isEmpty()) {
      return ;
    }
    List<File> files = getAllFilesInDir(prop.getDataSeedDir());
    if(files == null || files.isEmpty()) {
      return ;
    }
    storeRepo.deleteAll();
    if(prop.isCleanupAndSeedData()) {
      itemRepo.deleteAll();
      indexer.deleteAll();
      deleteFile(prop.getSeededFiles());
    }
    Set<String> processed = readProcessed();
    for(File file : files) {
      if(processed.contains(file.getName())) {
        logger.info("skipping file: {}", file.getName());
        continue;
      }
      List<Item> items = objectMapper.readValue(
          file
          , new TypeReference<List<Item>>(){});
      List<String> categories = getCategoriesFromFileName(file.getName());
      logger.info("Seeding data file: " + file.getName());
      logger.info("categories for file: " + file.getName() + " categories: " + categories.toString());
      for (Item item : items) {
        if(item.getCategories() == null || item.getCategories().isEmpty()) {
          item.setCategories(categories);
        }
        try {
          itemRepo.save(item);
        } catch(DuplicateKeyException e) {
          logger.debug("found duplicate item upc {}", item.getUpc());
        }
      }
      
      // index documents
      indexer.addToIndex(items);
      logger.info("Added & indexed " + items.size() + " items for categories: " + categories.toString());
      writeProgress(file.getName());
    }
    logger.info("Seeding store locations: " + prop.getStoreSeedFile());
    List<Store> stores = objectMapper.readValue(
        new File("src/main/data/store_locations.json")
        , new TypeReference<List<Store>>(){});
    for (Store store : stores) {
      storeRepo.save(store);
    }
    
  }
}
