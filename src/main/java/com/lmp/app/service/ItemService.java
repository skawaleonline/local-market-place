package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lmp.db.pojo.Item;
import com.lmp.db.repository.ItemRepository;
import com.lmp.solr.SolrSearchService;
import com.lmp.solr.entity.ItemDoc;
import com.lmp.solr.entity.SearchRequest;
import com.lmp.solr.entity.SearchResponse;

@Service
public class ItemService {

  @Autowired
  private ItemRepository itemRepo;
  @Autowired
  private SolrSearchService solrService;

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

  public SearchResponse searchByText(SearchRequest sRequest) {
    if(sRequest == null) {
      return null;
    }
    Page<ItemDoc> results = solrService.search(sRequest);
    List<String> ids = new ArrayList<>();
    results.getContent().forEach(itemDoc -> {
      ids.add(itemDoc.getId());
    });
    Iterable<Item> items = itemRepo.findAllById(ids);
    return SearchResponse.build(results, items);
  }
}
