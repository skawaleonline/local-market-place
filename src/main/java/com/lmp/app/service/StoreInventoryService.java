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

  @SuppressWarnings("deprecation")
  private Pageable createPageRequest(SearchRequest sRequest) {
    return new PageRequest(sRequest.getPage(), sRequest.getRows());
  }

  public BaseResponse searchStoreInventoryFor(SearchRequest sRequest) {
    if(sRequest == null || sRequest.getStoreId() == null || sRequest.getStoreId().isEmpty()) {
      return SearchResponse.invalidSearchRequest("missing store id in the request");
    }
    Page<StoreInventory> items = null;
    if(sRequest.getQuery() == null || sRequest.getQuery().isEmpty()) {
      items = repo.findAllByStoreId(sRequest.getStoreId(), createPageRequest(sRequest));
    } else {
      Page<ItemDoc> results = solrService.search(sRequest);
      List<String> ids = new ArrayList<>();
      results.getContent().forEach(itemDoc -> {
        ids.add(itemDoc.getId());
      });
      items = repo.findAllByStoreIdAndItemIdIn(sRequest.getStoreId(), ids, createPageRequest(sRequest));
    }
    return SearchResponse.buildStoreInventoryResponse(items);
  }

  public BaseResponse searchAllStoresFor(SearchRequest sRequest) {
      Page<ItemDoc> results = solrService.search(sRequest);
      List<String> ids = new ArrayList<>();
      results.getContent().forEach(itemDoc -> {
        ids.add(itemDoc.getId());
      });
      Page<StoreInventory> items = repo.findAllByItemIdIn(ids, createPageRequest(sRequest));
    
    return SearchResponse.buildStoreInventoryResponse(items);
  }
}
