package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import com.lmp.solr.entity.ItemDoc;

public class ResultFilter {

  private String fieldName;
  private List<CountPair> values = new ArrayList<>();

  public static ResultFilter buildResultFilter(String fieldName, FacetPage<ItemDoc> docs) {
    ResultFilter response = new ResultFilter();
    response.fieldName = fieldName;
    if(docs == null || docs.getFacetResultPage(fieldName) != null) {
      Page<FacetFieldEntry> page = docs.getFacetResultPage(fieldName);
      page.getContent().forEach(entry -> {
        response.values.add(new CountPair(entry.getValue(), entry.getValueCount()));
      });
    }
    return response;
  }
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public List<CountPair> getValue() {
    return values;
  }
  public void setValue(List<CountPair> value) {
    this.values = value;
  }
}

class CountPair {
  private String name;
  private Long count;

  public CountPair(String name, Long count) {
    this.name = name;
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }
}
