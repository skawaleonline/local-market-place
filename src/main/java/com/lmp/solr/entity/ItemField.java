package com.lmp.solr.entity;

public enum ItemField {

  ID("id"), CONTENT("content"), TITLE("title"), BRAND("brand"), CATEGORIES("categories"), UPC("upc"),
  LIST_PRICE("listPrice"), SELL_PRICE("sellPrice"), STORES("stores");

  private String value;

  ItemField(String inValue) {
    value = inValue;
  }

  public String getValue() {
    return value;
  }

}
