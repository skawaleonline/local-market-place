package com.lmp.solr.indexer;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.lmp.config.ConfigProperties;
import com.lmp.db.pojo.Item;

@Component
public class SolrIndexer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ConfigProperties prop;
  private HttpSolrClient solr;

  public SolrIndexer() {
  }

  public boolean indexItems(List<Item> items) throws SolrServerException, IOException {
    if(solr == null) {
      logger.info("conntecting solr at {}", prop.getSolrUrl());
      solr = new HttpSolrClient.Builder(prop.getSolrUrl()).build();
    }
    if(items == null || items.isEmpty()) {
      return false;
    }
    for (Item item : items) {
      indexItem(item);
    }
    return true;
  }

  public boolean indexItem(Item item) throws SolrServerException, IOException {
    if (item == null) {
      return false;
    }
    Joiner joiner = Joiner.on(" ").skipNulls();
    String brand = Strings.nullToEmpty(item.getBrand());
    String categories = joiner.join(item.getCategory());
    String title = Strings.nullToEmpty(item.getTitle());
    String content = brand + " " + categories + " " + title;
    SolrInputDocument document = new SolrInputDocument();
    document.addField("id", item.getId());
    document.addField("upc", Strings.nullToEmpty(Long.toString(item.getUpc())));
    document.addField("brand", brand);
    document.addField("categories", categories);
    document.addField("content", content);
    solr.add(document);
    solr.commit();
    return true;
  }
}
