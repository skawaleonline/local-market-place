package com.lmp.db.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StoreInventory {

  @Id
  private String id;
  @Indexed
  private String storeId;
  @DBRef
  private Item item;
  private double listPrice;
  private boolean onSale;
  private double salePrice;
  private int popularity = 1;
  private int stock;
  private long added;
  private long updated;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getStoreId() {
    return storeId;
  }
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }
  public Item getItem() {
    return item;
  }
  public void setItem(Item item) {
    this.item = item;
  }
  public int getStock() {
    return stock;
  }
  public void setStock(int stock) {
    this.stock = stock;
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
