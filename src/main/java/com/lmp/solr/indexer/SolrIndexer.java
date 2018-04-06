package com.lmp.solr.indexer;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lmp.db.pojo.ItemEntity;
import com.lmp.solr.entity.ItemDoc;
import com.lmp.solr.repository.SolrSearchRepository;

@Component
public class SolrIndexer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource
  private SolrSearchRepository repository;

  public void addToIndex(List<ItemEntity> items) throws SolrServerException, IOException {
    if(items == null || items.isEmpty()) {
      return ;
    }
    for (ItemEntity item : items) {
      addToIndex(item);
    }
  }

  @Transactional
  public void addToIndex(ItemEntity item, String storeids) throws SolrServerException, IOException {
    if (item == null) {
      return ;
    }
    repository.save(ItemDoc.fromItem(item, storeids));
  }

  @Transactional
  public void addToIndex(ItemEntity item) throws SolrServerException, IOException {
    if (item == null) {
      return ;
    }
    repository.save(ItemDoc.fromItem(item, ""));
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
