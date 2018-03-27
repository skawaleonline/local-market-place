package com.lmp.app.entity;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.http.HttpStatus;

public class BaseResponse {

  protected int statusCode;
  protected String errorMessage;
  protected long found;
  protected int start;
  protected int rows;

  public static BaseResponse invalidSearchRequest(String message) {
    BaseResponse response = new BaseResponse();
    response.statusCode = HttpStatus.BAD_REQUEST.value();
    response.errorMessage = message;
    return response;
  }

  public static BaseResponse solrErrorResponse(QueryResponse queryResponse) {
    BaseResponse response = new BaseResponse();
    response.statusCode = queryResponse.getStatus();
    return response;
  }

  public int getStatusCode() {
    return statusCode;
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
