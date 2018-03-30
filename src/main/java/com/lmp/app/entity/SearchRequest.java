package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.lmp.solr.entity.ItemField;

public class SearchRequest {

  private String query;
  private String storeId;
  @Min(0)
  private int page;
  @Min(0)
  @Max(50)
  private int rows;
  private List<RequestFilter> filters = new ArrayList<>();
  private List<ItemField> fields = new ArrayList<>();
  private double lat;
  private double lng;
  private int radius = 5;

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
  public List<RequestFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<RequestFilter> filters) {
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

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  @Override
  public String toString() {
    return "query: " + query 
        + "storeId: " + storeId
        + "page: " + page
        + "size: " + rows
        + "filters: " + filters == null ? "" : filters.toString()
        + "fields: " + fields == null ? "" : fields.toString();
  }
}
