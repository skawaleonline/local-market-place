package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.lang.Nullable;

import com.lmp.solr.entity.ItemField;

public class SearchRequest {

  private String query;
  private String storeId;
  @Min(0)
  private int page;
  @Min(0)
  @Max(50)
  private int rows;
  private Map<String, String> filters = new HashMap<>();
  private List<ItemField> fields = new ArrayList<>();
  @Nullable
  private double lat;
  @Nullable
  private double lng;

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
  public Map<String, String> getFilters() {
    return filters;
  }
  public void setFilters(Map<String, String> filters) {
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
  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public String toString() {
    return "query: " + query 
        + "storeId: " + storeId
        + "page: " + page
        + "size: " + rows
        + "filters: " + filters == null ? "" : filters.toString()
        + "fields: " + fields == null ? "" : fields.toString();
  }
}
