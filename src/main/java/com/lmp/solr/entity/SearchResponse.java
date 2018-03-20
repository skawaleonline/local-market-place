package com.lmp.solr.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Lists;
import com.lmp.db.pojo.Item;

public class SearchResponse {

  private int statusCode;
  private String errorMessage;
  private long found;
  private List<Item> results;
  private int start;
  private int rows;

  public int getStatusCode() {
    return statusCode;
  }

  public static SearchResponse invalidSearchRequest() {
    SearchResponse response = new SearchResponse();
    response.statusCode = HttpStatus.BAD_REQUEST.value();
    response.errorMessage = HttpStatus.BAD_REQUEST.toString();
    return response;
  }

  public static SearchResponse solrErrorResponse(QueryResponse queryResponse) {
    SearchResponse response = new SearchResponse();
    response.statusCode = queryResponse.getStatus();
    return response;
  }

  public static SearchResponse build(Page<ItemDoc> result, Iterable<Item> items) {
    SearchResponse response = new SearchResponse();
    response.statusCode = HttpStatus.OK.value();
    response.found = result.getTotalElements();
    response.start = result.getPageable().getPageNumber();
    response.rows = result.getPageable().getPageSize();
    response.results = Lists.newArrayList(items);
    return response;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
  public long getFound() {
    return found;
  }
  public void setFound(long found) {
    this.found = found;
  }
  public List<Item> getResults() {
    return results;
  }
  public void setResults(List<Item> results) {
    this.results = results;
  }
  public int getStart() {
    return start;
  }
  public void setStart(int start) {
    this.start = start;
  }
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    this.rows = rows;
  }

  
}
