package com.lmp.solr.indexer;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lmp.db.pojo.Item;
import com.lmp.solr.SolrItemRepository;
import com.lmp.solr.entity.ItemDoc;

@Component
public class SolrIndexer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource
  private SolrItemRepository repository;

  public void addToIndex(List<Item> items) throws SolrServerException, IOException {
    if(items == null || items.isEmpty()) {
      return ;
    }
    for (Item item : items) {
      addToIndex(item);
    }
  }

  @Transactional
  public void addToIndex(Item item) throws SolrServerException, IOException {
    if (item == null) {
      return ;
    }
    repository.save(ItemDoc.fromItem(item));
  }

  @Transactional
  public void deleteFromIndex(String id) {
    repository.deleteById(id);
  }

  @Transactional
  public void deleteAll() {
    repository.deleteAll();
  }

}
