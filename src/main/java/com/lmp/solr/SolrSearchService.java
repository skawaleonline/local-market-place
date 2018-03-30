package com.lmp.solr;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.SearchRequest;
import com.lmp.db.pojo.Store;
import com.lmp.solr.entity.ItemDoc;

@Service
public class SolrSearchService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SolrItemRepository solrRepo;

  @Resource
  private SolrTemplate solrTemplate;

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

  private Criteria normaliseQuery(String searchTerm, String fName) {
    if(searchTerm == null || searchTerm.isEmpty()) {
      return null;
    }
    Criteria condition = null;
    for(String term : searchTerm.split(" ")) {
      if(condition == null) {
        condition = new Criteria(fName).contains(term); 
      } else {
        condition = condition.or(new Criteria(fName).contains(term));
      }
    }
    return condition;
  }
  private Criteria createSearchConditions(SearchRequest sRequest, List<Store> stores) {
    Criteria conditions1 = normaliseQuery(sRequest.getQuery(), "content");
    Criteria conditions2 = null;
    for (Store store : stores) {
      if(conditions2 == null) {
        conditions2 = new Criteria("stores").is(store.getId());
      } else {
        conditions2 = conditions2.or(new Criteria("stores").is(store.getId()));
      }
    }
    return conditions2 == null ? conditions1 : conditions1.connect().and(conditions2);
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

  public Page<ItemDoc> searchWithinStores(SearchRequest sRequest, List<Store> stores) {
    if(sRequest == null || sRequest.getQuery() == null || sRequest.getQuery().isEmpty()) {
      return null;
    }
    Criteria conditions = createSearchConditions(sRequest, stores);
    logger.info("searching for solr query {}", conditions.toString());
    SimpleQuery search = new SimpleQuery(conditions, createPageRequest(sRequest));
    Page<ItemDoc> itemDocs = solrTemplate.query("itemdoc", search, ItemDoc.class);
    if(itemDocs == null || itemDocs.getContent() == null) {
      logger.error("null response from solr for query: {}", sRequest.toString());
      return null;
    } else {
      logger.debug("solr docs found: {}, for query {}",itemDocs.getContent().size(), sRequest.toString());
    }
    return itemDocs;
  }
}
