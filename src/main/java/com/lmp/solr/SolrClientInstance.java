package com.lmp.solr;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lmp.config.ConfigProperties;

@Component
public class SolrClientInstance {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ConfigProperties prop;
  private HttpSolrClient solr;

  public HttpSolrClient solrClient() {
    if(solr == null) {
      logger.info("conntecting solr at {}", prop.getSolrUrl());
      solr = new HttpSolrClient.Builder(prop.getSolrUrl()).build();
    }
    return solr;
  }
}
