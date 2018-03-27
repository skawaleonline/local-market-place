package com.lmp.app.entity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Lists;
import com.lmp.db.pojo.Item;
import com.lmp.db.pojo.StoreInventory;
import com.lmp.solr.entity.ItemDoc;

public class SearchResponse<T> extends BaseResponse {

  private List<T> results;

  public static SearchResponse<Item> buildItemResponse(Page<ItemDoc> result, Iterable<Item> items) {
    SearchResponse<Item> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = result.getTotalElements();
    response.start = result.getPageable().getPageNumber();
    response.rows = result.getPageable().getPageSize();
    response.results = Lists.newArrayList(items);
    return response;
  }

  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<ItemDoc> result
      , Iterable<StoreInventory> items) {
    SearchResponse<StoreInventory> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = result.getTotalElements();
    response.start = result.getPageable().getPageNumber();
    response.rows = result.getPageable().getPageSize();
    response.results = Lists.newArrayList(items);
    return response;
  }

  public static SearchResponse<StoreInventory> buildStoreInventoryResponse(Page<StoreInventory> items) {
    SearchResponse<StoreInventory> response = new SearchResponse<>();
    response.statusCode = HttpStatus.OK.value();
    response.found = items.getTotalElements();
    response.start = items.getPageable().getPageNumber();
    response.rows = items.getPageable().getPageSize();
    response.results = Lists.newArrayList(items.getContent());
    return response;
  }
}
