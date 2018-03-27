package com.lmp.solr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.SearchRequest;
import com.lmp.solr.entity.ItemDoc;

@Service
public class SolrSearchService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SolrItemRepository solrRepo;

  @SuppressWarnings("deprecation")
  private Pageable createPageRequest(SearchRequest sRequest) {
    return new PageRequest(sRequest.getPage(), sRequest.getRows());
  }

  private String normalise(String query) {
    if(query == null) {
      return "";
    }
    if(query.startsWith("\"") && query.endsWith("\"")) {
      return query.substring(1, query.length() - 1);
    }
    return query;
  }

  private Page<ItemDoc> searchFields(SearchRequest sRequest) {
    if(sRequest == null) {
      return null;
    }
    logger.debug("querying solr with query {}", sRequest.toString());
    Pageable pageRequest = createPageRequest(sRequest);
    Page<ItemDoc> itemDocs = null;
    String query = normalise(sRequest.getQuery());
    if (query.startsWith("content:")) {
      itemDocs = solrRepo.findByContent(query.substring("content:".length()), pageRequest);
    } else if (query.startsWith("brand:")) {
      itemDocs = solrRepo.findByBrand(query.substring("brand:".length()), pageRequest);
    } else if (query.startsWith("cat:")) {
      itemDocs = solrRepo.findByCategories(query.substring("cat:".length()), pageRequest);
    } else {
      itemDocs = solrRepo.findByContent(sRequest.getQuery(), pageRequest);
    }

    if(itemDocs == null || itemDocs.getContent() == null) {
      logger.error("null response from solr for query: {}", sRequest.toString());
      return null;
    } else {
      logger.debug("solr docs found: {}, for query {}",itemDocs.getContent().size(), sRequest.toString());
    }
    return itemDocs;
  }

  public Page<ItemDoc> search(SearchRequest sRequest) {
    if(sRequest == null || sRequest.getQuery() == null || sRequest.getQuery().isEmpty()) {
      return null;
    }
    return searchFields(sRequest);
  }
}
