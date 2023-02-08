package com.answerdigital.answerking.service;

import com.answerdigital.answerking.mapper.OrderMapper;
import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.OrderStatus;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.OrderRepository;
import com.answerdigital.answerking.request.LineItemRequest;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.OrderResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.ORDERS_ALREADY_CANCELLED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.ORDERS_DO_NOT_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_ARE_RETIRED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_DO_NOT_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

/**
 * The class OrderService is the service layer for Orders {@link com.answerdigital.answerking.model.Order}.
 * It services requests from
 * OrderController {@link com.answerdigital.answerking.controller.OrderController} and
 * interacts with OrderRepository {@link com.answerdigital.answerking.repository.OrderRepository}
 * to connect with the database.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    private final ProductService productService;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    /**
     * Creates an Order {@link com.answerdigital.answerking.model.Order}.
     * @param orderRequest The OrderRequest {@link com.answerdigital.answerking.request.OrderRequest} object.
     * @return The newly persisted Order {@link com.answerdigital.answerking.model.Order},
     * in the form of an OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    @Transactional
    public OrderResponse addOrder(final OrderRequest orderRequest) {
        final Order order = addLineItemsToOrder(new Order(), orderRequest.lineItemRequests());
        return convertToResponse(orderRepository.save(order));
    }

    /**
     * Finds an Order {@link com.answerdigital.answerking.model.Order} by a given ID
     * and maps it to an OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
     * @param orderId The Order {@link com.answerdigital.answerking.model.Order} ID.
     * @return The found OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    public OrderResponse getOrderResponseById(final Long orderId) {
        return convertToResponse(getOrderById(orderId));
    }

    /**
     * Finds all the Orders {@link com.answerdigital.answerking.model.Order} within the database.
     * @return A List of all found Orders {@link com.answerdigital.answerking.model.Order}.
     */
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
            .map(orderMapper::orderToOrderResponse)
            .toList();
    }

    /**
     * Updates an Order {@link com.answerdigital.answerking.model.Order}.
     * @param orderId The ID of the Order {@link com.answerdigital.answerking.model.Order}.
     * @param orderRequest The OrderRequest {@link com.answerdigital.answerking.request.OrderRequest} object
     * @return The updated Order {@link com.answerdigital.answerking.model.Order}.
     */
    @Transactional
    public OrderResponse updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order order = getOrderById(orderId);

        if (OrderStatus.CANCELLED.equals(order.getOrderStatus())) {
            throw getCustomException(ORDERS_ALREADY_CANCELLED, orderId);
        }

        addLineItemsToOrder(order, orderRequest.lineItemRequests());
        return convertToResponse(orderRepository.save(order));
    }

    /**
     * Adds a List of LineItem {@link com.answerdigital.answerking.model.LineItem}
     * to an Order {@link com.answerdigital.answerking.model.Order}.
     * @param order The Order {@link com.answerdigital.answerking.model.Order}
     * to add a List of LineItems {@link com.answerdigital.answerking.model.LineItem} to.
     * @param lineItemRequests A List of
     * LineItemRequest {@link com.answerdigital.answerking.request.LineItemRequest} objects.
     * @return The updated Order {@link com.answerdigital.answerking.model.Order}
     * with added LineItems {@link com.answerdigital.answerking.model.LineItem}.
     */
    private Order addLineItemsToOrder(final Order order, final List<LineItemRequest> lineItemRequests) {
        // get line items id list
        final List<Long> lineItemProductIds = lineItemRequests.stream()
                .map(LineItemRequest::productId)
                .toList();

        final List<Product> products = getUnRetiredProductsListFromDatabase(lineItemProductIds);

        final Map<Product, Integer> lineItems = convertProductsListToMapWithQuantity(products, lineItemRequests);

        // clear existing order line items
        order.clearLineItems();

        // add line items to the order.
        lineItems.forEach((k, v) -> order.addLineItem(new LineItem(order, k, v)));
        return order;
    }

    /**
     * Gets a List of all unretired Products {@link com.answerdigital.answerking.model.Product}
     * from the database.
     * @param productIdsList The List of ProductIDs.
     * @return The Products {@link com.answerdigital.answerking.model.Product} found.
     */
    private List<Product> getUnRetiredProductsListFromDatabase(final List<Long> productIdsList){
        // get all products from line Item list from database
        final List<Product> products = productService.findAllProductsInListOfIds(
                productIdsList
        );

        // get product id list from database
        final List<Long> foundProductIdsList = products.stream().map(Product::getId).toList();

        // check if any of products did not exist in database, and if so throw Not Found exception
        final List<Long> notFoundProducts = new ArrayList<>(productIdsList);
        notFoundProducts.removeAll(foundProductIdsList);

        if(!notFoundProducts.isEmpty()){
            throw getCustomException(PRODUCTS_DO_NOT_EXIST, notFoundProducts);
        }

        // check if any of products are retired and throw exception
        final List<Long> retiredProducts = products
                .stream()
                .filter(Product::isRetired)
                .map(Product::getId)
                .toList();

        if (!retiredProducts.isEmpty()) {
            throw getCustomException(PRODUCTS_ARE_RETIRED, retiredProducts);
        }
        return products;
    }

    /**
     * Converts the List of Products {@link com.answerdigital.answerking.model.Product}
     * to map with quantity of Products {@link com.answerdigital.answerking.model.Product}.
     * @param products The List of Products {@link com.answerdigital.answerking.model.Product}.
     * @param lineItemRequests A list of
     * LineItemRequest {@link com.answerdigital.answerking.request.LineItemRequest} objects.
     * @return A Map of Product {@link com.answerdigital.answerking.model.Product} IDs
     * and Product {@link com.answerdigital.answerking.model.Product} quantities.
     */
    private Map<Product, Integer> convertProductsListToMapWithQuantity(final List<Product> products,
                                                                       final List<LineItemRequest> lineItemRequests) {
        // create helper map of product ids and products
        final Map<Long, Product> helper = products
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // create hashmap of product object and line item quantity
        return lineItemRequests
                .stream()
                .collect(Collectors.toMap(lineItemRequest -> helper.get(lineItemRequest.productId()),
                        LineItemRequest::quantity));
    }

    /**
     * Marks an Order {@link com.answerdigital.answerking.model.Order} as cancelled.
     * @param orderId The ID of the Order {@link com.answerdigital.answerking.model.Order} to cancel.
     * @return The Order {@link com.answerdigital.answerking.model.Order} with the status as cancelled.
     */
    public void cancelOrder(final Long orderId) {
        final Order order = getOrderById(orderId);

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw getCustomException(ORDERS_ALREADY_CANCELLED, order.getId());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    /**
     * Helper method which converts an Order {@link com.answerdigital.answerking.model.Order}
     * to an OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
     * @param order The Order {@link com.answerdigital.answerking.model.Order} object to convert.
     * @return The mapped OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    private OrderResponse convertToResponse(final Order order) {
        return orderMapper.orderToOrderResponse(order);
    }

    /**
     * Helper method which gets a raw Order {@link com.answerdigital.answerking.model.Order} by an ID.
     * @param orderId The ID of the Order {@link com.answerdigital.answerking.model.Order}.
     * @return The found Order {@link com.answerdigital.answerking.model.Order}.
     */
    private Order getOrderById(final Long orderId) {
        return this.orderRepository
            .findById(orderId)
            .orElseThrow(() -> getCustomException(ORDERS_DO_NOT_EXIST, orderId));
    }
}
