package com.lmp.app.entity;

public enum FilterField {

  ON_SALE("onsale"), BRAND("brand");

  private String value;

  private FilterField(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
