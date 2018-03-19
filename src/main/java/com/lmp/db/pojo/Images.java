package com.lmp.db.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Images {

  @Id
  public String id;
  public String base_url;
  public String primary;
  public List<String> alternate_urls;

}
