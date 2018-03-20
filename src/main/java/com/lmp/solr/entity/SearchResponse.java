package com.lmp.solr.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.springframework.http.HttpStatus;

public class SearchResponse {

  private int statusCode;
  private String errorMessage;
  private int found;
  private List<ItemDoc> results;
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
    if(queryResponse.getResponse() != null) {
      List<ItemDoc> itemDocs = new ArrayList<>();
      NamedList<Object> docs = queryResponse.getResponse();
      
    }
    
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
  public int getFound() {
    return found;
  }
  public void setFound(int found) {
    this.found = found;
  }
  public List<ItemDoc> getResults() {
    return results;
  }
  public void setResults(List<ItemDoc> results) {
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
