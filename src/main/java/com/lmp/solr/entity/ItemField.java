package com.lmp.solr.entity;

public enum ItemField {

  ID("id"), CONTENTS("contents"), TITLE("title"), BRAND("brand"), CATEGORIES("categories"), UPC("upc"),
  LIST_PRICE("listPrice"), SELL_PRICE("sellPrice");

  private String value;

  ItemField(String inValue) {
    value = inValue;
  }

  public String getValue() {
    return value;
  }

}
