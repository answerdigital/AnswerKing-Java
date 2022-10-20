package com.answerdigital.benhession.academy.answerkingweek2.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

//    @Mock
//    OrderRepository orderRepository;
//
//    @Test
//    void addOrder_saveSuccessful_orderOptional(@Mock Order order) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.save(order)).thenReturn(order);
//
//        Assertions.assertEquals(Optional.of(order), orderService.addOrder(order));
//    }
//
//    @Test
//    void addOrder_unableToSave_throwsUnableToSave(@Mock Order order) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.save(order)).thenThrow(new RuntimeException());
//
//        Assertions.assertThrows(UnableToSaveEntityException.class,
//                () -> orderService.addOrder(order));
//    }
//
//    @Test
//    void update_saveSuccessful_order(@Mock Order order) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.save(order)).thenReturn(order);
//
//        Assertions.assertEquals(order, orderService.update(order));
//    }
//
//    @Test
//    void update_unableToSave_throwsUnableToSave(@Mock Order order) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.save(order)).thenThrow(new RuntimeException());
//
//        Assertions.assertThrows(UnableToSaveEntityException.class,
//                () -> orderService.update(order));
//    }
//
//    @Test
//    void findById_orderFound_orderOptional(@Mock Order order) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
//
//        Assertions.assertEquals(Optional.of(order), orderService.findById(1));
//    }
//
//    @Test
//    void findById_orderNotFound_emptyOptional() {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        Assertions.assertEquals(Optional.empty(), orderService.findById(1));
//    }
//
//    @Test
//    void findAll_ordersPresent_ordersOptional(@Mock List<Order> orders) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orderRepository.findAll()).thenReturn(orders);
//
//        Assertions.assertEquals(Optional.of(orders), orderService.getAll());
//    }
//
//    @Test
//    void findAll_ordersNotPresent_emptyOptional(@Mock List<Order> orders) {
//        OrderService orderService = new OrderService(orderRepository);
//
//        when(orders.isEmpty()).thenReturn(true);
//        when(orderRepository.findAll()).thenReturn(orders);
//
//        Assertions.assertEquals(Optional.empty(), orderService.getAll());
//    }
}
