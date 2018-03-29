package com.lmp.db.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Images {

  public String id;
  public String base_url;
  public String primary;
  @JsonIgnore
  public List<String> alternate_urls;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getBase_url() {
    return base_url;
  }
  public void setBase_url(String base_url) {
    this.base_url = base_url;
  }
  public String getPrimary() {
    return primary;
  }
  public void setPrimary(String primary) {
    this.primary = primary;
  }
  public List<String> getAlternate_urls() {
    return alternate_urls;
  }
  public void setAlternate_urls(List<String> alternate_urls) {
    this.alternate_urls = alternate_urls;
  }

  
}
