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
import com.lmp.db.pojo.StoreItemEntity;
import com.lmp.solr.entity.ItemDoc;

public class SearchResponse<T> extends BaseResponse {

  private Collection<T> results;
  private long found;
  private int page;
  private int rows;

  private SearchResponse<T> blank() {
    this.statusCode = HttpStatus.OK.value();
    return this;
  }
  private static Map<String, StoreInventory> buildStoreItemMap(List<StoreItemEntity> items) {
    Map<String, StoreInventory> map = new HashMap<>();
    for(StoreItemEntity ie : items) {
      Item item = Item.fromStoreInventoryEntity(ie);
      if(map.containsKey(ie.getStoreId())) {
        map.get(ie.getStoreId()).getItems().add(item);
      } else {
        List<Item> list = new ArrayList<>();
        list.add(item);
        map.put(ie.getStoreId(), new StoreInventory(ie.getStoreId(), list));
      }
    }
    return map;
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

  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<StoreItemEntity> page) {
    if(page == null || !page.hasContent()) {
      SearchResponse<StoreInventory> blank = new SearchResponse<>();
      return blank.blank();
    }
    SearchResponse<StoreInventory> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = page.getTotalElements();
    response.page = page.getPageable().getPageNumber();
    response.rows = page.getPageable().getPageSize();
    response.results = buildStoreItemMap(page.getContent()).values();
    return response;
  }

  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<StoreItemEntity> page, long count) {
    SearchResponse<StoreInventory> response = buildStoreInventoryResponse(page);
    response.found = count;
    return response;
  }
  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<StoreItemEntity> page, long count, int pageNo) {
    SearchResponse<StoreInventory> response = buildStoreInventoryResponse(page, count);
    response.page = pageNo;
    return response;
  }  

  public Collection<T> getResults() {
    return results;
  }

  public void setResults(List<T> results) {
    this.results = results;
  }

  public long getFound() {
    return found;
  }
  public void setFound(long found) {
    this.found = found;
  }
  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    this.page = page;
  }
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    this.rows = rows;
  }
}
