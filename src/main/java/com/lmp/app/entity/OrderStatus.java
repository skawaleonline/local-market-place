package com.lmp.app.entity;

public enum OrderStatus {

  REVIEW("Review"), NEW("New"), IN_PROGRESS("In Progress"), READY("Ready"), SHIPPED("Shipped"), DELIVERED(
      "Delivered"), COMPLETED("Completed"), CANCELLED("Cancelled");

  private OrderStatus(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
