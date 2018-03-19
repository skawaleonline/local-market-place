package com.lmp.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lmp.app")
public class ConfigProperties {

  private List<String> dataSeedFile;
  private boolean dataSeedEnabled;
  private String solrUrl;

  public List<String> getDataSeedFile() {
    return dataSeedFile;
  }

  public void setDataSeedFile(List<String> dataSeedFile) {
    this.dataSeedFile = dataSeedFile;
  }

  public boolean isDataSeedEnabled() {
    return dataSeedEnabled;
  }

  public void setDataSeedEnabled(boolean dataSeed) {
    this.dataSeedEnabled = dataSeed;
  }

  public String getSolrUrl() {
    return solrUrl;
  }

  public void setSolrUrl(String solrUrl) {
    this.solrUrl = solrUrl;
  }
}