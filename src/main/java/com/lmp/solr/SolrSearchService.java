package com.lmp.solr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmp.solr.entity.ItemDoc;
import com.lmp.solr.entity.SearchRequest;

@Service
public class SolrSearchService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SolrItemRepository solrRepo;

  @SuppressWarnings("deprecation")
  private Pageable createPageRequest(SearchRequest sRequest) {
    return new PageRequest(sRequest.getPage(), sRequest.getRows());
  }
  private Page<ItemDoc> searchContentField(SearchRequest sRequest) {
    if(sRequest == null) {
      return null;
    }
    logger.debug("querying solr with query {}", sRequest.toString());
    Pageable pageRequest = createPageRequest(sRequest);
    Page<ItemDoc> itemDocs = solrRepo.findByContent(sRequest.getQuery(), pageRequest);

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

    return searchContentField(sRequest);
  }
}
