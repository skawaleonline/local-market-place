package com.lmp.app.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Category Tree data structure
 * @author skawale
 *
 */
public class CategoryNode {

  private String name;
  private List<CategoryNode> subCategories;

  public CategoryNode(String name) {
    this.name = name;
    this.subCategories = new ArrayList<>();
  }

  public void addChild(CategoryNode cat) {
    subCategories.add(cat);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CategoryNode> getSubCategories() {
    return subCategories;
  }

  public void setSubCategories(List<CategoryNode> subCategories) {
    this.subCategories = subCategories;
  }
}
