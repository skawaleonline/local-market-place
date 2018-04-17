  package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.db.pojo.StoreInventoryEntity;
import com.lmp.db.repository.StoreInventoryRepository;
import com.lmp.solr.SolrSearchService;
import com.lmp.solr.entity.ItemDoc;

@Service
public class StoreInventoryService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  StoreInventoryRepository repo;
  @Autowired
  SolrSearchService solrService;
  @Autowired
  StoreService storeService;

  private Page<ItemDoc> searchSolr(SearchRequest sRequest, List<String> storeIds) {
    // check if we need solr search for the request
    Page<ItemDoc> results = solrService.search(sRequest, storeIds);
    if (results == null || results.getTotalElements() <= 0 || results.getTotalElements() <= sRequest.fetchedCount()) {
      logger.info("no solr results results");
      return Page.empty();
    }
    return results;
  }

  private BaseResponse searchDBForDocs(SearchRequest sRequest, List<String> storeIds, Page<ItemDoc> docs) {
    Page<StoreInventoryEntity> items = null;
    if (docs != null) {
      List<String> ids = new ArrayList<>();
      docs.getContent().forEach(itemDoc -> {
        ids.add(itemDoc.getId());
      });
      // search items in solr first then retrieve those from MongoDB
      logger.info("going for mongo with {}", ids.toString());
      if (sRequest.isOnSaleRequest()) {
        items = repo.findAllByStoreIdInAndItemIdInAndOnSaleTrue(storeIds, ids,
            new PageRequest(0, sRequest.getRows()));
      } else {
        items = repo.findAllByStoreIdInAndItemIdIn(storeIds, ids, new PageRequest(0, sRequest.getRows()));
      }
      return SearchResponse.buildStoreInventoryResponse(items, docs.getTotalElements(), sRequest.getPage());
    } else {
      // search for all within store
      if (sRequest.isOnSaleRequest()) {
        items = repo.findAllByStoreIdInAndOnSaleTrue(storeIds, sRequest.pageRequesst());
      } else {
        items = repo.findAllByStoreIdIn(storeIds, sRequest.pageRequesst());
      }
    }
    return SearchResponse.buildStoreInventoryResponse(items);
  }

  @Cacheable("store-items")
  public BaseResponse search(SearchRequest sRequest) {
    List<String> storeIdsToSearch = new ArrayList<>();
    if (Strings.isNullOrEmpty(sRequest.getStoreId())) {
      // get all store around the user location
      storeIdsToSearch = storeService.getStoresIdsAround(sRequest);
    } else {
      // just search in store id selected
      storeIdsToSearch.add(sRequest.getStoreId());
    }
    Page<ItemDoc> docs = null;
    if (sRequest.isSolrSearchNeeded()) {
      // search solr and then mongo for this request
      docs = searchSolr(sRequest, storeIdsToSearch);
    }
    // dont search solr, directly search in mongo
    return searchDBForDocs(sRequest, storeIdsToSearch, docs);
  }

}
