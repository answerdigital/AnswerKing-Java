package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.OrderCancelledException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.OrderStatus;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.repository.OrderRepository;
import com.answerdigital.answerking.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final ProductService productService;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    /**
     * Creates an order *
     * @param orderRequest The Order Request
     * @return The created Order
     */
    @Transactional
    public Order addOrder(final OrderRequest orderRequest) {
        final Order order = new Order();

        orderRequest.lineItemRequests().forEach(lineItemRequest ->
            addLineItemToOrder(order, lineItemRequest.productId(), lineItemRequest.quantity())
        );

        return orderRepository.save(order);
    }

    /**
     * Finds an order by a given ID *
     * @param orderId The Order ID
     * @return The found Order
     */
    public Order findById(final Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    /**
     * Finds all the orders within the database *
     * @return A list of all found Orders
     */
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    /**
     * Updates an order *
     * @param orderId The ID of the Order
     * @param orderRequest The Order Request
     * @return The updated Order
     */
    @Transactional
    public Order updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findById(orderId);
        order.clearLineItems();

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new OrderCancelledException(
                String.format("The order with ID %d has been cancelled, not possible to update", orderId)
            );
        }

        orderRequest.lineItemRequests().forEach(lineItemRequest ->
            addLineItemToOrder(order, lineItemRequest.productId(), lineItemRequest.quantity())
        );

        return orderRepository.save(order);
    }

    /**
     * Adds a line item to an order *
     * @param order The Order that the line item should be associated with
     * @param productId The ID of the product
     * @param quantity The quantity of the product
     */
    private void addLineItemToOrder(final Order order, final Long productId, final Integer quantity) {
        final Product product = productService.findById(productId);

        if (product.isRetired()) {
            throw new RetirementException(String.format("The product with ID %d has been retired", product.getId()));
        }

        final Optional<LineItem> existingLineItem = order.getLineItems()
                .stream()
                .filter(lineItem -> lineItem.getProduct() == product)
                .findFirst();

        if (existingLineItem.isPresent()) {
            throw new ProductAlreadyPresentException(
                String.format("The product with ID %d is already in the order", product.getId())
            );
        }

        final LineItem lineItem = LineItem.builder()
                .quantity(quantity)
                .product(product)
                .order(order)
                .build();

        order.addLineItem(lineItem);
    }

    /**
     * Marks an Order as cancelled *
     * @param orderId The ID of the Order to cancel
     * @return The order with the status as cancelled
     */
    public Order cancelOrder(final Long orderId) {
        final Order order = findById(orderId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return order;
    }
}
