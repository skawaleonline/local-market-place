package com.lmp.solr.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRequest {

  private String query;
  private int page;
  private int rows;
  private Map<ItemField, String> filters = new HashMap<>();
  private List<ItemField> fields;

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

  
}
