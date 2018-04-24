package com.lmp.app.entity;

import org.springframework.stereotype.Component;

@Component
public class CheckoutRequest {

  private String userId;

  public CheckoutRequest() {
  }

  public CheckoutRequest(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
}
