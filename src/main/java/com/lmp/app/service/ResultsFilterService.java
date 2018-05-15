package com.lmp.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lmp.app.entity.PriceGroup;
import com.lmp.app.model.ResponseFilter;
import com.lmp.app.model.SearchRequest;
import com.lmp.db.pojo.PriceGroupCount;
import com.lmp.db.pojo.StoreEntity;
import com.lmp.solr.SolrSearchService;
import com.lmp.solr.entity.ItemField;

@Service
public class ResultsFilterService {

  @Autowired
  private SolrSearchService solrService;
  @Autowired
  private StoreService storeService;
  @Autowired
  private CategoryService categoryService;

  public List<ResponseFilter> getFiltersFor(SearchRequest sRequest) {
    List<ResponseFilter> facets = new ArrayList<>();
    List<StoreEntity> stores = null;
    // Search for query across all the stores
    if (Strings.isNullOrEmpty(sRequest.getStoreId())) {
      stores = storeService.getStoresAround(sRequest);
    } else {
      // get all items in the store
      stores = Lists.asList(storeService.getStoreById(sRequest.getStoreId()), new StoreEntity[] {});
      // search for query in store
    }
    List<String> storeIds = new ArrayList<>();
    stores.forEach(store -> {
      storeIds.add(store.getId());
    });
    List<ItemField> facetFields = new ArrayList<>();
    facetFields.add(ItemField.BRAND);
    //facetFields.add(ItemField.PRICE);
    // brand facet
    facets = ResponseFilter.buildResultFilter(facetFields, solrService.facetSearch(sRequest, storeIds, facetFields));
    // store facet
    facets.add(ResponseFilter.buildStoreFilter("store", stores));
    // category facet
    facets.add(ResponseFilter.fromList("category", categoryService.getCategories(sRequest.categoryFilter(), stores)));
    // price facet
    facets.add(ResponseFilter.fromList("price", PriceGroup.getAllPriceGroups()));
    return facets;
  }
}
