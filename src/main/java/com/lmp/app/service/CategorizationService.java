package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.CategoryTree;
import com.lmp.db.pojo.ItemEntity;

@Service
public class CategorizationService {

  @Autowired
  private ItemService itemService;

  public CategoryTree buildProductCategorization() {
    Iterable<ItemEntity> docs = itemService.getAllDocs(0, 50);
    List<List<String>> categories = new ArrayList<>();
    /*docs.forEach(doc -> {
      categories.add(doc.)
    });*/
    return null;
  }
}
