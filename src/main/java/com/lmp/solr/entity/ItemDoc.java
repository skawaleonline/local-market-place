package com.lmp.solr.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.lmp.db.pojo.ItemEntity;

@SolrDocument(solrCoreName = "itemdoc")
public class ItemDoc {

  @Id
  @Field("id")
  private String id;
  @Field("brand")
  private String brand;
  @Field("categories")
  private String categories;
  @Field("content")
  private String content;
  @Field("upc")
  private String upc;
  @Field("stores")
  private String stores;
  @Field("price")
  private float salePrice;


  public static ItemDoc fromItem(ItemEntity item, String storeids) {
    if(item == null) {
      return null;
    }
    ItemDoc itemDoc = new ItemDoc();
    Joiner joiner = Joiner.on(" ").skipNulls();
    itemDoc.id = item.getId();
    itemDoc.upc = Strings.nullToEmpty(Long.toString(item.getUpc()));
    itemDoc.brand = Strings.nullToEmpty(item.getBrand());
    itemDoc.salePrice = item.getOffer_price();
    itemDoc.setStores(storeids);
    if(item.getCategories() != null) {
      itemDoc.categories = joiner.join(item.getCategories());
    }
    itemDoc.content = itemDoc.brand + " " + itemDoc.categories 
        + " " + item.getTitle();
    return itemDoc;
  }

  private ItemDoc() {
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getBrand() {
    return brand;
  }
  public void setBrand(String brand) {
    this.brand = brand;
  }
  public String getCategories() {
    return categories;
  }
  public void setCategories(String categories) {
    this.categories = categories;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  public float getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(float salePrice) {
    this.salePrice = salePrice;
  }

  public String getUpc() {
    return upc;
  }

  public void setUpc(String upc) {
    this.upc = upc;
  }

  public String getStores() {
    return stores;
  }

  public void setStores(String stores) {
    this.stores = stores;
  }
}
