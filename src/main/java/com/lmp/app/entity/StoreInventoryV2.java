package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.List;

public class StoreInventoryV2 implements Inventory{

  private Item item;
  private List<StoreItem> storeItems = new ArrayList<>();
  
  public StoreInventoryV2(Item item, List<StoreItem> stores) {
    super();
    this.item = item;
    this.storeItems = stores;
  }
  
  public StoreInventoryV2(Item item, StoreItem storeItem) {
    super();
    this.item = item;
    this.storeItems.add(storeItem);
  }
  public Item getItem() {
    return item;
  }
  public void setItem(Item item) {
    this.item = item;
  }

  public List<StoreItem> getStoreItems() {
    return storeItems;
  }

  public void setStoreItems(List<StoreItem> storeItems) {
    this.storeItems = storeItems;
  }
  
  public void add(StoreItem item) {
    this.storeItems.add(item);
  }
}
