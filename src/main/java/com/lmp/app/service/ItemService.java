package com.lmp.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmp.db.pojo.Item;
import com.lmp.db.repository.ItemRepository;

@Service
public class ItemService {

  @Autowired
  private ItemRepository itemRepo;

  public List<Item> find() {
    
    return null;
  }

  public Item findByUpc(long upc) {
    if(upc <= 0) {
      return null;
    }
    return itemRepo.findByUpc(upc);
  }

  public List<Item> findAllByUpc(long upc) {
    if(upc <= 0) {
      return null;
    }
    return itemRepo.findAllByUpc(upc);
  }
}
