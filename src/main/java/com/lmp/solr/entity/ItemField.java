package com.lmp.solr.entity;

public enum ItemField {

  ID("id"), CONTENT("content"), TITLE("title"), BRAND("brand"), CATEGORIES("categories"), UPC("upc"),
  LIST_PRICE("list_price"), SELL_PRICE("sell_price"), STORES("stores");

  private String value;

  ItemField(String inValue) {
    value = inValue;
  }

  public String getValue() {
    return value;
  }

}
