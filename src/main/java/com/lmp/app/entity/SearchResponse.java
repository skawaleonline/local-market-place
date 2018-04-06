package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Lists;
import com.lmp.db.pojo.ItemEntity;
import com.lmp.db.pojo.StoreInventoryEntity;
import com.lmp.solr.entity.ItemDoc;

public class SearchResponse<T> extends BaseResponse {

  private Collection<T> results;

  private SearchResponse<T> blank() {
    this.statusCode = HttpStatus.OK.value();
    return this;
  }
  public static SearchResponse<ItemEntity> buildItemResponse(Page<ItemDoc> result, Iterable<ItemEntity> items) {
    SearchResponse<ItemEntity> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = result.getTotalElements();
    response.page = result.getPageable().getPageNumber();
    response.rows = result.getPageable().getPageSize();
    response.results = Lists.newArrayList(items);
    return response;
  }

  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<StoreInventoryEntity> page) {
    if(page == null || !page.hasContent()) {
      SearchResponse<StoreInventory> blank = new SearchResponse<>();
      return blank.blank();
    }
    Map<String, StoreInventory> map = new HashMap<>();
    for(StoreInventoryEntity ie : page.getContent()) {
      Item item = Item.fromItemEntity(ie.getItem()
          , ie.isOnSale(), ie.getStock() > 0, ie.getListPrice(), ie.getSalePrice());
      if(map.containsKey(ie.getStoreId())) {
        map.get(ie.getStoreId()).getItems().add(item);
      } else {
        List<Item> items = new ArrayList<>();
        items.add(item);
        map.put(ie.getStoreId(), new StoreInventory(ie.getStoreId(), items));
      }
    }
    SearchResponse<StoreInventory> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = page.getTotalElements();
    response.page = page.getPageable().getPageNumber();
    response.rows = page.getPageable().getPageSize();
    response.results = map.values();
    return response;
  }

  public Collection<T> getResults() {
    return results;
  }

  public void setResults(List<T> results) {
    this.results = results;
  }
}
