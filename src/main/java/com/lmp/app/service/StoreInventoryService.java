package com.lmp.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.db.pojo.StoreInventory;
import com.lmp.db.repository.StoreInventoryRepository;

@Service
public class StoreInventoryService {

  @Autowired
  StoreInventoryRepository repo;

  @SuppressWarnings("deprecation")
  private Pageable createPageRequest(SearchRequest sRequest) {
    return new PageRequest(sRequest.getPage(), sRequest.getRows());
  }

  public BaseResponse getAllInventoryForStore(SearchRequest sRequest) {
    if(sRequest == null || sRequest.getStoreId() == null || sRequest.getStoreId().isEmpty()) {
      return SearchResponse.invalidSearchRequest("missing store id in the request");
    }
    Page<StoreInventory> items = repo.findAllByStoreId(sRequest.getStoreId(), createPageRequest(sRequest));
    return SearchResponse.buildStoreInventoryResponse(items);
  }
}
