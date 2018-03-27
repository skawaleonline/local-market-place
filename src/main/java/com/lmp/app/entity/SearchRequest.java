package com.lmp.app.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmp.solr.entity.ItemField;

public class SearchRequest {

  private String query;
  private String storeId;
  private int page;
  private int rows;
  private Map<ItemField, String> filters = new HashMap<>();
  private List<ItemField> fields;

  public static SearchRequest createFor(String q, int page, int count) {
    SearchRequest sr = new SearchRequest();
    sr.query = q;
    sr.page = page;
    sr.rows = count;
    return sr;
  }

  public static SearchRequest createSISearch(String storeId, String q, int page, int count) {
    SearchRequest sr = new SearchRequest();
    sr.query = q;
    sr.storeId = storeId;
    sr.page = page;
    sr.rows = count;
    return sr;
  }
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
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
  public Map<ItemField, String> getFilters() {
    return filters;
  }
  public void setFilters(Map<ItemField, String> filters) {
    this.filters = filters;
  }
  public List<ItemField> getFields() {
    return fields;
  }
  public void setFields(List<ItemField> fields) {
    this.fields = fields;
  }
  public String getStoreId() {
    return storeId;
  }
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }
}
