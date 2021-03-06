  package com.lmp.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.lmp.app.entity.ShoppingCart;
import com.lmp.app.entity.ShoppingCart.CartItem;
import com.lmp.app.exceptions.ItemNotInStockException;
import com.lmp.app.model.BaseResponse;
import com.lmp.app.model.SearchRequest;
import com.lmp.app.model.SearchResponse;
import com.lmp.db.pojo.StoreItemEntity;
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

   public Page<ItemDoc> searchSolr(SearchRequest sRequest, List<String> storeIds) {
    // check if we need solr search for the request
    Page<ItemDoc> results = solrService.search(sRequest, storeIds);
    if (results == null || results.getTotalElements() <= 0 || results.getTotalElements() <= sRequest.fetchedCount()) {
      logger.info("no solr results results");
      return Page.empty();
    }
    return results;
  }

  private BaseResponse searchDBForDocs(SearchRequest sRequest, List<String> storeIds, Page<ItemDoc> docs) {
    Page<StoreItemEntity> items = null;
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
      List<String> ids = Splitter.on(",").splitToList(sRequest.getStoreId().trim());
      for (String id : ids) {
        storeIdsToSearch.add(id.trim());
      }
    }
    Page<ItemDoc> docs = null;
    if (sRequest.isSolrSearchNeeded()) {
      // search solr and then mongo for this request
      docs = searchSolr(sRequest, storeIdsToSearch);
      if(docs == null || docs.getTotalElements() == 0) {
        return SearchResponse.empty();
      }
    }
    // dont search solr, directly search in mongo
    return searchDBForDocs(sRequest, storeIdsToSearch, docs);
  }

  public CartItem findById(String id) {
    Optional<StoreItemEntity> sItem = repo.findById(id);
    return sItem.isPresent() ? sItem.get().toCartItem() : null;
  }

  public Map<String, Integer> getInStockCount(List<String> ids) {
    Iterable<StoreItemEntity> items = repo.findAllById(ids);
    Map<String, Integer> map = new HashMap<>();
    if(items == null) {
      return null;
    }
    items.iterator().forEachRemaining(item -> {
      map.put(item.getId(), item.getStock());
    });
    return map;
  }
  
  public void verifyItemStock(List<CartItem> items) {
    if(items == null) {
      return;
    }
    List<String> ids = new ArrayList<>();
    items.forEach(item -> {
      ids.add(item.getId());
    });
    Map<String, Integer> map = getInStockCount(ids);
    ItemNotInStockException outOfStockException = new ItemNotInStockException();
    for (CartItem item : items) {
      if (item.getQuantity() > map.getOrDefault(item.getId(), 0)) {
        outOfStockException.getOutOfStockItems().add(item.getId());
      }
    }
    if (outOfStockException.getOutOfStockItems().size() > 0) {
      throw outOfStockException;
    }
  }

  @Transactional
  public boolean updateStockCount(List<CartItem> cartItems, boolean increment) {
    if(cartItems == null || cartItems.size() == 0) {
      return false;
    }
    Map<String, Integer> map = new HashMap<>();
    cartItems.forEach(item -> {
      map.put(item.getId(), item.getQuantity());
    });
    Iterable<StoreItemEntity> items = repo.findAllById(map.keySet());
    if(items == null) {
      return false;
    }
    items.forEach(item -> {
      item.setStock(increment ? item.getStock() + map.get(item.getId()) : item.getStock() - map.get(item.getId()));
    });
    repo.saveAll(items);
    return true;
  }
}
