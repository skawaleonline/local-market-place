package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

public enum PriceGroup {

  PRICE_GROUP_ONE("Under $15",0, 0, 15),
  PRICE_GROUP_TWO("$15 to $50", 1, 15, 50),
  PRICE_GROUP_THREE("$50 to $100", 2, 50, 100),
  PRICE_GROUP_FOUR("$100 & Above",3, 100, 0);

  public static final Map<Integer, PriceGroup> orderMap;
  public static final Map<String, PriceGroup> nameMap;
  static {
    orderMap = new HashMap<>();
    orderMap.put(0, PRICE_GROUP_ONE);
    orderMap.put(1, PRICE_GROUP_TWO);
    orderMap.put(2, PRICE_GROUP_THREE);
    orderMap.put(3, PRICE_GROUP_FOUR);
    nameMap = new HashMap<>();
    nameMap.put("under $15", PRICE_GROUP_ONE);
    nameMap.put("$15 to $50", PRICE_GROUP_TWO);
    nameMap.put("$50 to $100", PRICE_GROUP_THREE);
    nameMap.put("$100 & above", PRICE_GROUP_FOUR);
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

  public static List<String> getAllPriceGroups() {
    List<String> list = new ArrayList<>();
    for(int i = 0; i < orderMap.size(); i++) {
      list.add(orderMap.get(i).getName());
    }
    return list;
  }

  public static PriceGroup from(String name) {
    if(Strings.isNullOrEmpty(name)) {
      return null;
    }
    return nameMap.get(name.trim().toLowerCase());
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
