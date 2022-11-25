package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.request.OrderRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/orders")
@Tag(name = "Orders", description = "The Orders API")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the orders have been returned.",
            content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class)))})
    })
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the order has been created.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Order> createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    @Operation(summary = "Get a single order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order with the provided id has been found.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Order> getOrderById(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an existing order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order has been updated.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Order> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{orderId}")
    @Operation(summary = "Cancel an existing order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the order has been cancelled.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "When the order with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Order> cancelOrder(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }
}
