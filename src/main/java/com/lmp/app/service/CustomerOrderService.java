package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.lmp.app.entity.CustomerOrder;
import com.lmp.app.entity.OrderStatus;
import com.lmp.app.entity.ShoppingCart;
import com.lmp.app.entity.ShoppingCart.CartItem;
import com.lmp.app.exceptions.CartNotFoundException;
import com.lmp.app.exceptions.InvalidOrderStatusException;
import com.lmp.app.exceptions.OrderNotFoundException;
import com.lmp.app.exceptions.ItemNotInStockException;
import com.lmp.app.model.CheckoutRequest;
import com.lmp.app.model.ShoppingCartRequest;
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
  public CustomerOrder placeOrder(CheckoutRequest cRequest) throws CartNotFoundException, ItemNotInStockException {
    if (cRequest == null || Strings.isNullOrEmpty(cRequest.getUserId())) {
      return null;
    }
    // Get the cart for this order
    ShoppingCart cart = cartService.getCart(new ShoppingCartRequest(cRequest.getUserId()));
    if (cart == null) {
      throw new CartNotFoundException();
    }
    // check the if cart items are in stock
    List<String> ids = new ArrayList<>();
    cart.getItems().forEach(item -> {
      ids.add(item.getId());
    });
    Map<String, Integer> map = sItemService.getInStockCount(ids);
    ItemNotInStockException outOfStockException = new ItemNotInStockException();
    for (CartItem item : cart.getItems()) {
      if (item.getQuantity() > map.getOrDefault(item.getId(), 0)) {
        outOfStockException.getOutOfStockItems().add(item.getId());
      }
    }
    if (outOfStockException.getOutOfStockItems().size() > 0) {
      throw outOfStockException;
    }

    // if all good place the order
    CustomerOrderEntity saved = orderRepo.save(CustomerOrderEntity.fromCart(cart));
    // update stock for all items in cart
    sItemService.updateStockCount(cart.getItems(), false);
    return saved.toCustomerOrder();
  }

  @Transactional
  public CustomerOrder confirmOrder(CheckoutRequest cRequest) throws OrderNotFoundException, InvalidOrderStatusException {
    if (cRequest == null || Strings.isNullOrEmpty(cRequest.getUserId()) 
        || Strings.isNullOrEmpty(cRequest.getOrderId())) {
      return null;
    }
    Optional<CustomerOrderEntity> optional = orderRepo.findById(cRequest.getOrderId());
    if(!optional.isPresent()) {
      throw new OrderNotFoundException();
    }
    // check user and order id validation
    CustomerOrderEntity customerOrderEntity = optional.get();
    if(!customerOrderEntity.getCustomer().getId().equals(cRequest.getUserId())) {
      throw new InvalidOrderStatusException();
    }
    // invalid order
    if(!OrderStatus.REVIEW.equals(customerOrderEntity.getStatus())) {
      throw new InvalidOrderStatusException();
    }
    customerOrderEntity.setStatus(OrderStatus.NEW);
    customerOrderEntity.setOrderedOn(System.currentTimeMillis());
    customerOrderEntity = orderRepo.save(customerOrderEntity);

    //finally empty the cart
    cartService.clear(cRequest.getUserId());

    return customerOrderEntity.toCustomerOrder();
  }

  @Transactional
  public CustomerOrder cancelCheckout(CheckoutRequest cRequest) throws OrderNotFoundException, InvalidOrderStatusException {
    if (cRequest == null || Strings.isNullOrEmpty(cRequest.getUserId()) 
        || Strings.isNullOrEmpty(cRequest.getOrderId())) {
      return null;
    }
    Optional<CustomerOrderEntity> optional = orderRepo.findById(cRequest.getOrderId());
    if(!optional.isPresent()) {
      throw new OrderNotFoundException();
    }
    // check user and order id validation
    CustomerOrderEntity customerOrderEntity = optional.get();
    if(!customerOrderEntity.getCustomer().getId().equals(cRequest.getUserId())) {
      throw new InvalidOrderStatusException();
    }
    // invalid order
    if(OrderStatus.REVIEW != customerOrderEntity.getStatus()) {
      throw new InvalidOrderStatusException();
    }
    // update the order status
    customerOrderEntity.setStatus(OrderStatus.CANCELLED);
    customerOrderEntity.setLastUpdatedOn(System.currentTimeMillis());
    customerOrderEntity = orderRepo.save(customerOrderEntity);
    // update the product stocks 
    // update stock for all items in cart
    sItemService.updateStockCount(customerOrderEntity.getItems(), true);

    return customerOrderEntity.toCustomerOrder();
  }
}
