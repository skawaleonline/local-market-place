package com.lmp.db.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="store-inventory")
public class StoreInventory {

  @Id
  private String id;
  private Store store;
  @Indexed(unique = true)
  private Item item;
  private long listPrice;
  private long noOfItems;
  private long added;
  private long updated;
  private long expiration;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Store getStore() {
    return store;
  }
  public void setStore(Store store) {
    this.store = store;
  }
  public Item getItem() {
    return item;
  }
  public void setItem(Item item) {
    this.item = item;
  }
  public long getNoOfItems() {
    return noOfItems;
  }
  public void setNoOfItems(long noOfItems) {
    this.noOfItems = noOfItems;
  }
  public long getAdded() {
    return added;
  }
  public void setAdded(long added) {
    this.added = added;
  }
  public long getUpdated() {
    return updated;
  }
  public void setUpdated(long updated) {
    this.updated = updated;
  }
  public long getExpiration() {
    return expiration;
  }
  public void setExpiration(long expiration) {
    this.expiration = expiration;
  }
}
