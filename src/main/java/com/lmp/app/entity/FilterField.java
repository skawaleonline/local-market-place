package com.lmp.app.entity;

public enum FilterField {

  ON_SALE("onsale"), BRAND("brand"), CATEGORY("cat"), MIN_PRICE("min_price"), MAX_PRICE("max_price");

  private String value;

  private FilterField(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
