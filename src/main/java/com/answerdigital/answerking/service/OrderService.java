package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.OrderCancelledException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.OrderMapper;
import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.OrderStatus;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.repository.OrderRepository;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.OrderResponse;
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

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

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
    public OrderResponse addOrder(final OrderRequest orderRequest) {
        final Order order = new Order();

        orderRequest.lineItemRequests().forEach(lineItemRequest ->
            addLineItemToOrder(order, lineItemRequest.productId(), lineItemRequest.quantity())
        );

        return convertToResponse(orderRepository.save(order));
    }

    /**
     * Finds an Order by a given ID and maps it to an Order Response *
     * @param orderId The Order ID
     * @return The found Order Response
     */
    public OrderResponse findById(final Long orderId) {
        return orderRepository
            .findById(orderId)
            .map(orderMapper::orderToOrderResponse)
            .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    /**
     * Finds all the orders within the database *
     * @return A list of all found Orders
     */
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
            .map(orderMapper::orderToOrderResponse)
            .toList();
    }

    /**
     * Updates an order *
     * @param orderId The ID of the Order
     * @param orderRequest The Order Request
     * @return The updated Order
     */
    @Transactional
    public OrderResponse updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order order = convertFromResponse(findById(orderId));

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new OrderCancelledException(
                String.format("The order with ID %d has been cancelled, not possible to update", orderId)
            );
        }

        order.clearLineItems();

        orderRequest.lineItemRequests().forEach(lineItemRequest ->
            addLineItemToOrder(order, lineItemRequest.productId(), lineItemRequest.quantity())
        );

        return convertToResponse(orderRepository.save(order));
    }

    /**
     * Adds a line item to an order *
     * @param order The Order that the line item should be associated with
     * @param productId The ID of the product
     * @param quantity The quantity of the product
     */
    private void addLineItemToOrder(final Order order, final Long productId, final Integer quantity) {
        final Product product = productService.findById(productId);

        if (OrderStatus.CANCELLED.equals(order.getOrderStatus())) {
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
    public OrderResponse cancelOrder(final Long orderId) {
        final Order order = convertFromResponse(findById(orderId));
        order.setOrderStatus(OrderStatus.CANCELLED);
        return convertToResponse(orderRepository.save(order));
    }

    /**
     * Helper method which converts an Order to an Order Response *
     * @param order The Order instance to convert
     * @return The mapped Order Response
     */
    private OrderResponse convertToResponse(final Order order) {
        return orderMapper.orderToOrderResponse(order);
    }

    /**
     * Helper method which converts an Order from an Order Response *
     * @param orderResponse The Order Response instance to convert
     * @return The mapped Order
     */
    private Order convertFromResponse(final OrderResponse orderResponse) {
        return orderMapper.orderResponseToOrder(orderResponse);
    }
}
