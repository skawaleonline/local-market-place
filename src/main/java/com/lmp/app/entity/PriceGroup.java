package com.lmp.app.entity;

import java.util.HashMap;
import java.util.Map;

public enum PriceGroup {

  ZERO_TO_25("Under $25",0, 0, 25),
  _25_TO_50("$25 to $50", 1, 25, 50),
  _50_TO_100("$50 to $100", 2, 50, 100),
  _100_AND_ABOVE("$100 & Above",3, 100, 0);

  public static final Map<Integer, PriceGroup> orderMap;
  static {
    orderMap = new HashMap<>();
    orderMap.put(0, ZERO_TO_25);
    orderMap.put(1, _25_TO_50);
    orderMap.put(2, _50_TO_100);
    orderMap.put(3, _100_AND_ABOVE);
  }
  private String name;
  private int order;
  private int min;
  private int max;

  private PriceGroup(String name, int order, int min, int max) {
    this.name = name;
    this.order = order;
    this.min = min;
    this.max = max;
  }

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public static Map<Integer, PriceGroup> getOrdermap() {
    return orderMap;
  }


  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }
  
}