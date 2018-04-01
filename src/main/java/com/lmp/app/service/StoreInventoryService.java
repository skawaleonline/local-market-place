package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
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

  private Page<StoreInventory> searchInStores(SearchRequest sRequest, List<String> storeIds) {
    Page<ItemDoc> results = solrService.search(sRequest, storeIds);
    List<String> ids = new ArrayList<>();
    results.getContent().forEach(itemDoc -> {
      ids.add(itemDoc.getId());
    });
    Page<StoreInventory> items = null;
    if (sRequest.isOnSaleRequest()) {
      items = repo.findAllByStoreIdInAndItemIdInAndOnSale(storeIds, ids, true,
          createPageRequest(sRequest));
    } else {
      items = repo.findAllByStoreIdInAndItemIdIn(storeIds, ids, createPageRequest(sRequest));
    }
    return items;
  }

  private BaseResponse searchAllStoresAround(SearchRequest sRequest) {
    // get stores around
    List<Store> stores = storeService.getStoresAround(sRequest);
    List<String> storeIds = new ArrayList<>();
    stores.forEach(store -> {
      storeIds.add(store.getId());
    });
    Page<StoreInventory> items = searchInStores(sRequest, storeIds);
    return SearchResponse.buildStoreInventoryResponse(items);
  }

  private Page<StoreInventory> getAllInventoryForStore(SearchRequest sRequest) {
    Page<StoreInventory> items = null;
    // if brand filter is set then do solr search for documents
    if (sRequest.brandFromFilter() != null) {
      // has brand filter, we need to search in store inventory
      items = searchInStores(sRequest, Lists.asList(sRequest.getStoreId(), new String[] {}));
    } else {
      // search for all within store
      if (sRequest.isOnSaleRequest()) {
        items = repo.findAllByStoreIdAndOnSale(sRequest.getStoreId(), true, createPageRequest(sRequest));
      } else {
        items = repo.findAllByStoreId(sRequest.getStoreId(), createPageRequest(sRequest));
      }
    }
    return items;
  }

  @Cacheable("store-items")
  public BaseResponse search(SearchRequest sRequest) {
    Page<StoreInventory> items = null;
    // Search for query across all the stores
    if(sRequest.getStoreId() == null || sRequest.getStoreId().isEmpty()) {
      return searchAllStoresAround(sRequest);
    } else {
      // get all items in the store
      if(sRequest.getQuery() == null || sRequest.getQuery().isEmpty()) {
        items = getAllInventoryForStore(sRequest);
      } else {
        // search for query in store
        items = searchInStores(sRequest, Lists.asList(sRequest.getStoreId(), new String[] {}));
      }
    }
    return SearchResponse.buildStoreInventoryResponse(items);
  }

}
