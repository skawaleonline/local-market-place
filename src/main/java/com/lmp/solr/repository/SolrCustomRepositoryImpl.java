package com.lmp.solr.repository;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;

import com.lmp.solr.entity.ItemDoc;

public class SolrCustomRepositoryImpl implements SolrCustomRepository {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource
  private SolrTemplate solrTemplate;

  @Override
  public Page<ItemDoc> search(SimpleQuery query) {
    Page<ItemDoc> itemDocs = solrTemplate.query("itemdoc", query, ItemDoc.class);
    if(itemDocs == null || itemDocs.getContent() == null) {
      logger.error("null response from solr for query: {}", query.toString());
      return null;
    }
    logger.debug("solr docs found: {}, for query {}",itemDocs.getContent().size(), query.toString());
    return itemDocs;
  }

}
