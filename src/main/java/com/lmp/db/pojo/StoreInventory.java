package com.lmp.db.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="store-inventory")
@CompoundIndexes( {
  @CompoundIndex(name = "popular_items",
      unique = true,
      def = "{store.id : 1, item.id : 1, popularity : -1}")
})
public class StoreInventory {

  @Id
  private String id;
  @DBRef
  private Store store;
  @DBRef
  private Item item;
  private double listPrice;
  private boolean onSale;
  private double salePrice;
  private int popularity = 1;
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
  public double getListPrice() {
    return listPrice;
  }
  public void setListPrice(double listPrice) {
    this.listPrice = listPrice;
  }
  public boolean isOnSale() {
    return onSale;
  }
  public void setOnSale(boolean onSale) {
    this.onSale = onSale;
  }
  public double getSalePrice() {
    return salePrice;
  }
  public void setSalePrice(double salePrice) {
    this.salePrice = salePrice;
  }
  public int getPopularity() {
    return popularity;
  }
  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }
}
