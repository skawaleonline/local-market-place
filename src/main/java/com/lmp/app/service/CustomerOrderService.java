package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.lmp.app.entity.CheckoutRequest;
import com.lmp.app.entity.CustomerOrder;
import com.lmp.app.entity.ShoppingCart;
import com.lmp.app.entity.ShoppingCart.CartItem;
import com.lmp.app.entity.ShoppingCartRequest;
import com.lmp.app.exceptions.CartNotFoundException;
import com.lmp.app.exceptions.ProductNotInStockException;
import com.lmp.db.pojo.CustomerOrderEntity;
import com.lmp.db.repository.CustomerOrderRepository;

@Service
public class CustomerOrderService {

  @Autowired
  private ShoppingCartService cartService;
  @Autowired
  private StoreInventoryService sItemService;
  @Autowired
  private CustomerOrderRepository orderRepo;

  @Autowired
  @Transactional
  public CustomerOrder placeOrder(CheckoutRequest cRequest) throws CartNotFoundException, ProductNotInStockException {
    if(cRequest == null || Strings.isNullOrEmpty(cRequest.getUserId())) {
      return null;
    }
    // Get the cart for this order
    ShoppingCart cart = cartService.getCart(new ShoppingCartRequest(cRequest.getUserId()));
    if(cart == null) {
      throw new CartNotFoundException();
    }
    // check the if cart items are in stock
    List<String> ids = new ArrayList<>();
    cart.getItems().forEach(item -> {ids.add(item.getId());});
    Map<String, Integer> map = sItemService.getInStockCount(ids);
    ProductNotInStockException outOfStockException = new ProductNotInStockException();
    for(CartItem item : cart.getItems()) {
      if(item.getQuantity() > map.getOrDefault(item.getId(), 0)) {
        outOfStockException.getOutOfStockItems().add(item.getId());
      }
    }
    if(outOfStockException.getOutOfStockItems().size() > 0) {
      throw outOfStockException;
    }

    // if all good place the order
    CustomerOrderEntity saved = orderRepo.save(CustomerOrderEntity.fromCart(cart));
    // update stock for all items in cart
    sItemService.updateStockCount(map);
    return saved.toCustomerOrder();
  }
}
