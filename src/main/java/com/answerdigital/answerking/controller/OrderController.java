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
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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

@Validated
@Tag(name = "Orders", description = "The Orders API")
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order.")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "When the order has been created.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Order.class)
                    )
                }
            ),
            @ApiResponse(
                responseCode = "400",
                description = "When invalid parameters are provided.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
    })
    public ResponseEntity<Order> createOrder(@Valid @RequestBody final OrderRequest orderRequest, final Errors errors) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest),
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    @Operation(summary = "Get a single order.")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "When the order with the provided id has been found.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Order.class)
                    )
                }
            ),
            @ApiResponse(
                responseCode = "404",
                description = "When the order with the given id does not exist.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
    })
    public ResponseEntity<Order> getOrderById(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all orders.")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "When all the orders have been returned.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Order.class))
                    )
                }
            )
    })
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
    }

    @PutMapping(path = "/{orderId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<Order> updateProductQuantity(@PathVariable @NotNull final Long orderId,
                                                       @PathVariable @NotNull final Long productId,
                                                       @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.updateProductQuantity(orderId, productId, quantity), HttpStatus.OK);
    }

    @PostMapping(path = "/{orderId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<Order> addProductToBasket(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long productId,
                                                    @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.addProductToBasket(orderId, productId, quantity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{orderId}/product/{productId}")
    public ResponseEntity<Order> deleteProductInBasket(@PathVariable @NotNull final Long orderId,
                                                       @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(orderService.deleteProductInBasket(orderId, productId), HttpStatus.OK);
    }
}
