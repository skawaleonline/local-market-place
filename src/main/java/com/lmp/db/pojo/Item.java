package com.lmp.db.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="item")
public class Item {

  @Id
  private String id;
  private List<String> categories;
  private String title;
  private String url;
  private String brand;
  @Indexed(unique = true)
  private long upc;
  private long tcin;
  private String dpci;
  private long release_date;
  private List<String> bullet_description;
  private SoftBullets soft_bullets;
  private String description;
  private String package_dimensions;
  @JsonIgnore
  private String available_to_purchase_date_time;
  @JsonIgnore
  private List<Images> images;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getBrand() {
    return brand;
  }
  public void setBrand(String brand) {
    this.brand = brand;
  }
  public long getUpc() {
    return upc;
  }
  public void setUpc(long upc) {
    this.upc = upc;
  }
  public long getTcin() {
    return tcin;
  }
  public void setTcin(long tcin) {
    this.tcin = tcin;
  }
  public String getDpci() {
    return dpci;
  }
  public void setDpci(String dpci) {
    this.dpci = dpci;
  }
  public long getRelease_date() {
    return release_date;
  }
  public void setRelease_date(long release_date) {
    this.release_date = release_date;
  }
  public List<String> getBullet_description() {
    return bullet_description;
  }
  public void setBullet_description(List<String> bullet_description) {
    this.bullet_description = bullet_description;
  }
  public SoftBullets getSoft_bullets() {
    return soft_bullets;
  }
  public void setSoft_bullets(SoftBullets soft_bullets) {
    this.soft_bullets = soft_bullets;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getPackage_dimensions() {
    return package_dimensions;
  }
  public void setPackage_dimensions(String package_dimensions) {
    this.package_dimensions = package_dimensions;
  }
  public String getAvailable_to_purchase_date_time() {
    return available_to_purchase_date_time;
  }
  public void setAvailable_to_purchase_date_time(String available_to_purchase_date_time) {
    this.available_to_purchase_date_time = available_to_purchase_date_time;
  }
  public List<Images> getImages() {
    return images;
  }
  public void setImages(List<Images> images) {
    this.images = images;
  }
  public List<String> getCategories() {
    return categories;
  }
  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  
}

class SoftBullets {
  private String title;
  private List<String> bullets;
  public SoftBullets() { }
  public SoftBullets(String empty) { this.title = empty;}
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public List<String> getBullets() {
    return bullets;
  }
  public void setBullets(List<String> bullets) {
    this.bullets = bullets;
  }
}
