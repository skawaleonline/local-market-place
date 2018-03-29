package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.db.pojo.Store;
import com.lmp.db.pojo.StoreInventory;
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

  @SuppressWarnings("deprecation")
  private Pageable createPageRequest(SearchRequest sRequest) {
    return new PageRequest(sRequest.getPage(), sRequest.getRows());
  }

  private BaseResponse searchAllStoresFor(SearchRequest sRequest) {
    // get stores around
    List<Store> stores = storeService.getStoresAround(sRequest);
    List<String> storeids = new ArrayList<>();
    stores.forEach(store -> {
      storeids.add(store.getId());
    });
    Page<ItemDoc> results = solrService.searchWithinStores(sRequest, stores);
    if(results == null || results.getContent() == null) {
      return new BaseResponse();
    }
    List<String> ids = new ArrayList<>();
    results.getContent().forEach(itemDoc -> {
      ids.add(itemDoc.getId());
    });
    Page<StoreInventory> items = repo.findAllByStoreIdInAndItemIdIn(storeids, ids, createPageRequest(sRequest));
  
    return SearchResponse.buildStoreInventoryResponse(items);
  }

  @Cacheable("store-items")
  public BaseResponse searchStoreInventoryFor(SearchRequest sRequest) {
    Page<StoreInventory> items = null;
    // Search for query across all the stores
    if(sRequest.getStoreId() == null || sRequest.getStoreId().isEmpty()) {
      return searchAllStoresFor(sRequest);
    } else {
      // get all items in the store
      if(sRequest.getQuery() == null || sRequest.getQuery().isEmpty()) {
        items = repo.findAllByStoreId(sRequest.getStoreId(), createPageRequest(sRequest));
      } else {
        // search for query in in store
        Page<ItemDoc> results = solrService.search(sRequest);
        List<String> ids = new ArrayList<>();
        results.getContent().forEach(itemDoc -> {
          ids.add(itemDoc.getId());
        });
        items = repo.findAllByStoreIdAndItemIdIn(sRequest.getStoreId(), ids, createPageRequest(sRequest));
      }
    }
    return SearchResponse.buildStoreInventoryResponse(items);
  }
}
