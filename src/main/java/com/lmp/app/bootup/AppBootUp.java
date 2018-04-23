package com.lmp.app.bootup;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import com.lmp.app.utils.FileIOUtil;
import com.lmp.config.ConfigProperties;
import com.lmp.db.pojo.ItemEntity;
import com.lmp.db.pojo.StoreEntity;
import com.lmp.db.pojo.StoreItemEntity;
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
  private DecimalFormat df = new DecimalFormat("###.##");

  private void seedOneCategory(File file, List<StoreEntity> stores) throws IOException, SolrServerException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<ItemEntity> items = objectMapper.readValue(file, new TypeReference<List<ItemEntity>>() {});
    logger.info("Seeding data file: {}", file.getName());
    Iterator<ItemEntity> it = items.listIterator();
    while(it.hasNext()) {
      ItemEntity item = it.next();
      try {
        itemRepo.save(item);
        indexer.addToIndex(item, fillStoreInventory(item, stores));
      } catch (DuplicateKeyException e) {
        logger.info("found duplicate item upc {}", item.getUpc());
        logger.error(e.getMessage(), e);
        it.remove();
      }
    }
    // index documents
    //indexer.addToIndex(items);
    logger.info("Added & indexed {} items from file {}", items.size(), file.getName());
    FileIOUtil.writeProgress(prop.getSeededFiles(), file.getName());
  }

  private String fillStoreInventory(ItemEntity item, List<StoreEntity> stores) {
    List<String> storeIdsToIndex = new ArrayList<>();
    Random random = new Random();
    for(StoreEntity store : stores) {
      if(item.canGoOnStoreInventory(store)) {
        StoreItemEntity sItem = new StoreItemEntity();
        long time = System.currentTimeMillis();
        sItem.setStoreId(store.getId());
        sItem.getItem().setId(item.getId());
        sItem.setAdded(time);
        sItem.setUpdated(time);
        sItem.setListPrice(item.getList_price() * (0.6f + random.nextFloat())); // min 0.6 factor for price
        storeIdsToIndex.add(store.getId());
        if(random.nextInt(100) < 20) {
          // put 20% inventory on sale 
          sItem.setOnSale(true);
          sItem.setSalePrice(sItem.getListPrice() * (1 - ((10.0f +random.nextInt(30)))/100)); // min 10 percent discount
        } else {
          sItem.setSalePrice(sItem.getListPrice());
        }
        siRepo.save(sItem);
      }
    }
    return Joiner.on(" ").join(storeIdsToIndex);
  }

  private void seedStores() throws IOException{
    logger.info("Seeding stores : " + prop.getStoreSeedFile());
    ObjectMapper objectMapper = new ObjectMapper();
    List<StoreEntity> stores = objectMapper.readValue(
        new File(prop.getStoreSeedFile())
        , new TypeReference<List<StoreEntity>>(){});
    storeRepo.saveAll(stores);
  }

  public void buildItemRepo() throws IOException, SolrServerException {
    if(!prop.isDataSeedEnabled() || prop.getDataSeedDir() == null 
        || prop.getDataSeedDir().isEmpty()) {
      return ;
    }
    List<File> files = FileIOUtil.getAllFilesInDir(prop.getDataSeedDir());
    if(files == null || files.isEmpty()) {
      return ;
    }
    if(prop.isCleanupAndSeedData()) {
      itemRepo.deleteAll();
      indexer.deleteAll();
      siRepo.deleteAll();
      storeRepo.deleteAll();
      seedStores();
      FileIOUtil.deleteFile(prop.getSeededFiles());
    }
    Set<String> processed = FileIOUtil.readProcessed(prop.getSeededFiles());
    List<StoreEntity> stores = storeRepo.findAll();
    for(File file : files) {
      if(processed.contains(file.getName())) {
        logger.info("skipping file: {}", file.getName());
        continue;
      }
      seedOneCategory(file, stores);
    }
  }
}
