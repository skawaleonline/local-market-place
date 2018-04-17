package com.lmp.app.entity;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.http.HttpStatus;

public class BaseResponse {

  protected int statusCode;
  protected String message;
  protected long found;
  protected int page;
  protected int rows;

  public static BaseResponse invalidSearchRequest(String message) {
    BaseResponse response = new BaseResponse();
    response.statusCode = HttpStatus.BAD_REQUEST.value();
    response.message = message;
    return response;
  }

  public static BaseResponse solrErrorResponse(QueryResponse queryResponse) {
    BaseResponse response = new BaseResponse();
    response.statusCode = queryResponse.getStatus();
    return response;
  }

  public static BaseResponse empty() {
    BaseResponse response = new BaseResponse();
    response.statusCode = HttpStatus.OK.value();
    return response;
  }

  public int getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  public String getMessage() {
    return message;
  }
  public void setErrorMessage(String message) {
    this.message = message;
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
