  package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.db.pojo.StoreEntity;
import com.lmp.db.pojo.StoreInventoryEntity;
import com.lmp.db.repository.StoreInventoryRepository;
import com.lmp.solr.SolrSearchService;
import com.lmp.solr.entity.ItemDoc;

@Service
public class StoreInventoryService {

  @Autowired
  StoreInventoryRepository repo;
  @Autowired
  SolrSearchService solrService;
  @Autowired
  StoreService storeService;

  private BaseResponse searchInStores(SearchRequest sRequest, List<String> storeIds) {
    // check for the count first
    Page<ItemDoc> results = solrService.search(sRequest, storeIds);
    if(results == null || results.getTotalElements() <= 0 
        || results.getTotalElements() <= sRequest.fetchedCount()) {
      return SearchResponse.buildStoreInventoryResponse(Page.empty());
    }
    
    long count = results.getTotalElements();
    List<String> ids = new ArrayList<>();
    results.getContent().forEach(itemDoc -> {
      ids.add(itemDoc.getId());
    });
    //search items in solr first then retrieve those from MongoDB
    Page<StoreInventoryEntity> items = null;
    if (sRequest.isOnSaleRequest()) {
      items = repo.findAllByStoreIdInAndItemIdInAndOnSale(storeIds, ids, sRequest.isOnSaleRequest(),
          new PageRequest(0, sRequest.getRows()));
    } else {
      items = repo.findAllByStoreIdInAndItemIdIn(storeIds, ids, new PageRequest(0, sRequest.getRows()));
    }
    return SearchResponse.buildStoreInventoryResponse(items, count, sRequest.getPage());
  }

  private BaseResponse searchAllStoresAround(SearchRequest sRequest) {
    // get stores around
    List<String> storeIds = storeService.getStoresIdsAround(sRequest);
    return searchInStores(sRequest, storeIds);
  }

  private BaseResponse getAllInventoryForStore(SearchRequest sRequest) {
    Page<StoreInventoryEntity> items = null;
    // if brand filter is set then do solr search for documents
    if (sRequest.brandFromFilter() != null) {
      // has brand filter, we need to search in store inventory
      return searchInStores(sRequest, Lists.asList(sRequest.getStoreId(), new String[] {}));
    } else {
      // search for all within store
      if (sRequest.isOnSaleRequest()) {
        items = repo.findAllByStoreIdAndOnSale(sRequest.getStoreId(), sRequest.isOnSaleRequest(),
            sRequest.pageRequesst());
      } else {
        items = repo.findAllByStoreId(sRequest.getStoreId(), sRequest.pageRequesst());
      }
    }
    return SearchResponse.buildStoreInventoryResponse(items);
  }

  @Cacheable("store-items")
  public BaseResponse search(SearchRequest sRequest) {
    // Search for query across all the stores
    if(Strings.isNullOrEmpty(sRequest.getStoreId())) {
      return searchAllStoresAround(sRequest);
    } else {
      // get all items in the store
      if(Strings.isNullOrEmpty(sRequest.getQuery())) {
        return getAllInventoryForStore(sRequest);
      } else {
        // search for query in store
        return searchInStores(sRequest, Lists.asList(sRequest.getStoreId(), new String[] {}));
      }
    }
  }

}
