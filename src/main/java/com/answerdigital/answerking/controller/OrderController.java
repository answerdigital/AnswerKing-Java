package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.OrderResponse;
import com.answerdigital.answerking.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * This class is responsible for handling control logic for all incoming requests by
 * exposing a variety of Order related REST endpoints.
 */
@RestController
@RequestMapping(path = "/orders")
@Tag(name = "Orders", description = "Create and manage customer orders.")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Exposes an endpoint which allows API users to get all existing Orders
     * via a GET request. This produces a HTTP status of 200 OK if there are
     * or are not Orders to be returned.
     *
     * @return A ResponseEntity with a list of {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    @GetMapping
    @Operation(summary = "Get all orders.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the orders have been returned.",
                content = {
                    @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)))}
                )
    })
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to create new Orders
     * via a POST request. This produces a HTTP status of 201 CREATED if
     * successful, otherwise a status of 400 BAD REQUEST if invalided parameters
     * are provided.
     *
     * @param orderRequest An instance of {@link com.answerdigital.answerking.request.OrderRequest}.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    @PostMapping
    @Operation(summary = "Create a new order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the order has been created.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest), HttpStatus.CREATED);
    }

    /**
     * Exposes an endpoint which allows API users to get an existing Order via a GET request.
     * This produces a HTTP status of 200 OK if there is an order available to be returned. Otherwise,
     * a status of 404 NOT FOUND is produced when the order does not exist.
     *
     * @param orderId The ID of the Order.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    @GetMapping(path = "/{orderId}")
    @Operation(summary = "Get a single order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order with the provided id has been found.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.getOrderResponseById(orderId), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to update an Order via a PUT request. This produces a HTTP status
     * of 200 OK if the update was successful, 400 BAD REQUEST if invalid parameters were provided and 404 NOT FOUND
     * if the Order could not be found.
     *
     * @param orderId The ID of the Order.
     * @param orderRequest An instance of {@link com.answerdigital.answerking.request.OrderRequest}.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    @PutMapping("/{orderId}")
    @Operation(summary = "Update an existing order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order has been updated.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to cancel an Order via a DELETE request. This produces a HTTP status
     * of 200 OK if the update was successful, 400 BAD REQUEST if invalid parameters were provided, 404 NOT FOUND
     * if the Order could not be found and 410 GONE if the Order is already retired.
     *
     * @param orderId The ID of the Order.
     * @return A ResponseEntity of type {@link java.lang.Void}.
     */
    @DeleteMapping(path = "/{orderId}")
    @Operation(summary = "Cancel an existing order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order has been cancelled.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> cancelOrder(@PathVariable @NotNull final Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
