package com.lmp.app.service;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.Item;
import com.lmp.app.entity.ShoppingCart;
import com.lmp.app.entity.ShoppingCart.CartItem;
import com.lmp.app.entity.ShoppingCartRequest;
import com.lmp.db.pojo.ShoppingCartEntity;
import com.lmp.db.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ShoppingCartRepository repo;
  @Autowired
  private StoreInventoryService storeItemService;

  public ShoppingCart getCart(ShoppingCartRequest cartRequest) {
    // user id is cart's id
    Optional<ShoppingCartEntity> cart = repo.findById(cartRequest.getUserId());
    if(!cart.isPresent()) {
      return null;
    }
    return ShoppingCart.fromEntity(cart.get());
  }

  public ShoppingCart add(ShoppingCartRequest cartRequest) {
    ShoppingCart cart = getCart(cartRequest);
    if(cart == null) {
      // create new cart for user
      cart = ShoppingCart.forUser(cartRequest.getUserId());
    }
    CartItem item = cart.get(cartRequest.getItemId());
    if(item == null) {
      item = storeItemService.findById(cartRequest.getItemId());
      if(item == null) {
        logger.info("no store item");
        return cart;
      }
    }
    if(cart.getStoreId() != null && !cart.getStoreId().equals(item.getStoreId())) {
      throw new RuntimeException("cart can not have items from different stores");
    }
    cart.setStoreId(item.getStoreId());
    cart.addToCart(item, cartRequest.getQuantity());
    repo.save(ShoppingCartEntity.toEntity(cart));
    return getCart(cartRequest);
  }

  public ShoppingCart remove(ShoppingCartRequest cartRequest) {
    ShoppingCart cart = getCart(cartRequest);
    if(cart == null) {
      return ShoppingCart.forUser(cartRequest.getUserId());
    }
    cart.remove(cartRequest.getItemId());
    repo.save(ShoppingCartEntity.toEntity(cart));
    return getCart(cartRequest);
  }

  public ShoppingCart update(ShoppingCartRequest cartRequest) {
    ShoppingCart cart = getCart(cartRequest);
    if(cart == null) {
      return ShoppingCart.forUser(cartRequest.getUserId());
    }
    cart.update(cartRequest.getItemId(), cartRequest.getQuantity());
    repo.save(ShoppingCartEntity.toEntity(cart));
    return getCart(cartRequest);
  }

  public double getTotal(String cartId) {
    return 0;
  }
}
