package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

//    @Mock
//    OrderService orderService;
//    @Mock
//    ItemService itemService;
//    @Mock
//    BindingResult bindingResult;
//
//    @Test
//    void addOrder_verificationHasErrors_throwsInvalidValues() {
//        OrderController orderController = new OrderController(orderService, itemService);
//        AddOrderDTO addOrderDTO = new AddOrderDTO("");
//
//        when(bindingResult.hasErrors()).thenThrow(new InvalidValuesException());
//
//        Assertions.assertThrows(InvalidValuesException.class,
//                () -> orderController.addOrder(addOrderDTO, bindingResult));
//    }
//
//    @Test
//    void addOrder_validDTO_httpStatusOK(@Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//        AddOrderDTO addOrderDTO = new AddOrderDTO("9 Dewsbury Rd, Leeds LS11 5DD");
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.addOrder(notNull())).thenReturn(Optional.of(order));
//
//        Assertions.assertEquals(HttpStatus.CREATED,
//                orderController.addOrder(addOrderDTO, bindingResult).getStatusCode());
//    }
//
//    @Test
//    void addOrder_saveFailed_throwsUnableToSave() {
//        OrderController orderController = new OrderController(orderService, itemService);
//        AddOrderDTO addOrderDTO = new AddOrderDTO("9 Dewsbury Rd, Leeds LS11 5DD");
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//
//        Assertions.assertThrows(UnableToSaveEntityException.class,
//                () -> orderController.addOrder(addOrderDTO, bindingResult));
//    }
//
//    @Test
//    void getOrder_orderNotFound_throwsNotFound() {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.getOrder(1));
//    }
//
//    @Test
//    void getOrder_orderFound_httpStatusOK(@Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//
//        Assertions.assertEquals(HttpStatus.OK,
//                orderController.getOrder(1).getStatusCode());
//
//    }
//
//    @Test
//    void addItemToBasket_verificationHasErrors_throwsInvalidValues(@Mock ItemToBasketDTO itemToBasketDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenThrow(new InvalidValuesException());
//
//        Assertions.assertThrows(InvalidValuesException.class,
//                () -> orderController.addItemToBasket(1, itemToBasketDTO, bindingResult));
//    }
//
//    @Test
//    void addItemToBasket_orderNotFound_throwsNotFound(@Mock ItemToBasketDTO itemToBasketDTO,
//                                                      @Mock Item item) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.findById(anyInt())).thenReturn(Optional.empty());
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(itemToBasketDTO.getQuantity()).thenReturn(1);
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.addItemToBasket(1, itemToBasketDTO, bindingResult));
//    }
//
//    @Test
//    void addItemToBasket_itemNotFound_throwsNotFound(@Mock ItemToBasketDTO itemToBasketDTO,
//                                                     @Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.empty());
//        when(itemToBasketDTO.getQuantity()).thenReturn(1);
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.addItemToBasket(1, itemToBasketDTO, bindingResult));
//    }
//
//    @Test
//    void addItemToBasket_itemUnavailable_throwsItemUnavailable(@Mock ItemToBasketDTO itemToBasketDTO,
//                                                               @Mock Order order,
//                                                               @Mock Item item) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(itemToBasketDTO.getQuantity()).thenReturn(1);
//        when(item.isAvailable()).thenReturn(false);
//
//        Assertions.assertThrows(ItemUnavailableException.class,
//                () -> orderController.addItemToBasket(1, itemToBasketDTO, bindingResult));
//    }
//
//    @Test
//    void addItemToBasket_itemAlreadyInOrder_throwsConflict(@Mock ItemToBasketDTO itemToBasketDTO,
//                                                           @Mock Order order,
//                                                           @Mock Item item) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(itemToBasketDTO.getQuantity()).thenReturn(1);
//        when(item.isAvailable()).thenReturn(true);
//        when(order.addItemToBasket(item, itemToBasketDTO.getQuantity())).thenReturn(false);
//
//        Assertions.assertThrows(ConflictException.class,
//                () -> orderController.addItemToBasket(1, itemToBasketDTO, bindingResult));
//    }
//
//    @Test
//    void addItemToBasket_newItem_httpStatusOK(@Mock ItemToBasketDTO itemToBasketDTO,
//                                              @Mock Order order,
//                                              @Mock Item item) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(orderService.update(order)).thenReturn(order);
//        when(itemToBasketDTO.getQuantity()).thenReturn(1);
//        when(item.isAvailable()).thenReturn(true);
//        when(order.addItemToBasket(item, itemToBasketDTO.getQuantity())).thenReturn(true);
//        when(order.getId()).thenReturn(1);
//
//        Assertions.assertEquals(HttpStatus.CREATED, orderController.addItemToBasket(1, itemToBasketDTO,
//                bindingResult).getStatusCode());
//    }
//
//    @Test
//    void deleteItemInBasket_itemNotFound_throwsNotFound(@Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.deleteItemInBasket(1, 1).getStatusCode());
//
//    }
//
//    @Test
//    void deleteItemInBasket_orderNotFound_throwsNotFound(@Mock Item item) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.empty());
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.deleteItemInBasket(1, 1).getStatusCode());
//
//    }
//
//    @Test
//    void deleteItemInBasket_itemNotInBasket_throwsNotFound(@Mock Item item, @Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(order.basketHasItem(item)).thenReturn(false);
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.deleteItemInBasket(1, 1).getStatusCode());
//
//    }
//
//    @Test
//    void deleteItemInBasket_validItem_httpStatusOK(@Mock Item item, @Mock Order order) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(order.basketHasItem(item)).thenReturn(true);
//        when(orderService.update(order)).thenReturn(order);
//
//        Assertions.assertEquals(HttpStatus.OK,
//                orderController.deleteItemInBasket(1, 1).getStatusCode());
//
//    }
//
//    @Test
//    void updateItemQuantity_validationErrors_throwsInvalidValues(@Mock ItemQuantityDTO itemQuantityDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(bindingResult.hasErrors()).thenThrow(new InvalidValuesException());
//
//        Assertions.assertThrows(InvalidValuesException.class,
//                () -> orderController.updateItemQuantity(1, 2, itemQuantityDTO, bindingResult));
//    }
//
//    @Test
//    void updateItemQuantity_orderNotFound_throwsNotFound(@Mock Item item, @Mock ItemQuantityDTO itemQuantityDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.empty());
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.updateItemQuantity(1, 2, itemQuantityDTO, bindingResult));
//
//
//    }
//
//    @Test
//    void updateItemQuantity_itemNotFound_throwsNotFound(@Mock Order order, @Mock ItemQuantityDTO itemQuantityDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.updateItemQuantity(1, 2, itemQuantityDTO, bindingResult));
//
//    }
//
//    @Test
//    void updateItemQuantity_itemNotInBasket_throwsNotFound(@Mock Order order,
//                                                           @Mock Item item,
//                                                           @Mock ItemQuantityDTO itemQuantityDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(order.basketHasItem(item)).thenReturn(false);
//
//        Assertions.assertThrows(NotFoundException.class,
//                () -> orderController.updateItemQuantity(1, 2, itemQuantityDTO, bindingResult));
//    }
//
//    @Test
//    void updateItemQuantity_valid_HttpStatusOK(@Mock Order order,
//                                                           @Mock Item item,
//                                                           @Mock ItemQuantityDTO itemQuantityDTO) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.findById(anyInt())).thenReturn(Optional.of(order));
//        when(itemService.findById(anyInt())).thenReturn(Optional.of(item));
//        when(order.basketHasItem(item)).thenReturn(true);
//        when(orderService.update(order)).thenReturn(order);
//
//        Assertions.assertEquals(HttpStatus.OK,
//                orderController.updateItemQuantity(1, 2, itemQuantityDTO, bindingResult).getStatusCode());
//    }
//
//    @Test
//    void getAllOrders_ordersPresent_httpStatusOK(@Mock List<Order> orders) {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.getAll()).thenReturn(Optional.of(orders));
//
//        Assertions.assertEquals(HttpStatus.OK, orderController.getAllOrders().getStatusCode());
//    }
//
//    @Test
//    void getAllOrders_noOrders_httpStatusNoContent() {
//        OrderController orderController = new OrderController(orderService, itemService);
//
//        when(orderService.getAll()).thenReturn(Optional.empty());
//
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, orderController.getAllOrders().getStatusCode());
//    }
}
