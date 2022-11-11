package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.custom.ProductUnavailableException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.mapper.OrderMapper;
import com.answerdigital.academy.answerking.model.LineItem;
import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.model.Order;
import com.answerdigital.academy.answerking.repository.OrderRepository;
import com.answerdigital.academy.answerking.request.OrderRequest;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final OrderMapper orderMapper =
            Mappers.getMapper(OrderMapper.class);

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order addOrder(final OrderRequest orderRequest) {
        final Order newOrder = orderMapper.addRequestToOrder(orderRequest);
        return orderRepository.save(newOrder);
    }

    public Order findById(final Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    @Transactional
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Order updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order orderToUpdate = findById(orderId);
        final Order updatedOrder = orderMapper.updateOrderRequest(orderToUpdate, orderRequest);

        return orderRepository.save(updatedOrder);
    }

    @Transactional
    public Order addProductToBasket(final Long orderId, final Long productId, final Integer quantity) {
        final  Order order = findById(orderId);
        final Product product = productService.findById(productId);

        if (!product.getAvailable()) {
            throw new ProductUnavailableException(String.format("The product with ID %d is not available.", product.getId()));
        }

        final Optional<LineItem> existingOrderProduct = order.getLineItems()
                .stream()
                .filter(lineItem -> lineItem.getProduct() == product)
                .findFirst();

        if (existingOrderProduct.isPresent()) {
            throw new ConflictException(String.format("Product id %s is already in the basket", product.getId()));
        }

        final LineItem lineItem = new LineItem(order, product, quantity);
        order.getLineItems().add(lineItem);

        return orderRepository.save(order);
    }

    public Order updateProductQuantity(final Long orderId, final Long productId, final Integer productQuantity) {
        final Order order = findById(orderId);
        final Product product = productService.findById(productId);

        final Optional<LineItem> existingLineItem = order.getLineItems()
                .stream()
                .filter(lineItem -> lineItem.getProduct() == product)
                .findFirst();

        if (existingLineItem.isEmpty()) {
            throw new NotFoundException(
                    String.format("Product id = %d is not in the basket of order id = %d", orderId, productId)
            );
        }

        existingLineItem.get().setQuantity(productQuantity);
        return orderRepository.save(order);
    }

    public Order deleteProductInBasket(final Long orderId, final Long productId) {
        final Order order = findById(orderId);
        final Product product = productService.findById(productId);

        final Optional<LineItem> existingOrderProduct = order.getLineItems()
                .stream()
                .filter(lineItem -> lineItem.getProduct() == product)
                .findFirst();

        if (existingOrderProduct.isEmpty()) {
            throw new NotFoundException(
                    String.format("Product id = %s is not in the basket of order id = %s", productId, orderId)
            );
        }

        order.getLineItems().remove(existingOrderProduct.get());
        return orderRepository.save(order);
    }
}
