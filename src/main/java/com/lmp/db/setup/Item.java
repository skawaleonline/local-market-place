package com.lmp.db.setup;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Item {

  @Id
  public String id;
  public List<String> category;
  public String title;
  public String url;
  public String brand;
  public long upc;
  public long tcin;
  public String dpci;
  public long release_date;
  public List<String> bullet_description;
  public SoftBullets soft_bullets;
  public String description;
  public String package_dimensions;
  @JsonIgnore
  public String available_to_purchase_date_time;
  @JsonIgnore
  public List<Images> images;
}

class SoftBullets {
  public String title;
  public List<String> bullets;
  public SoftBullets() { }
  public SoftBullets(String empty) { this.title = empty;}
}

