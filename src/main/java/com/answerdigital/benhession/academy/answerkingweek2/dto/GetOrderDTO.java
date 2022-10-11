package com.answerdigital.benhession.academy.answerkingweek2.dto;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetOrderDTO {

    private int id;

    private String address;

    private OrderStatus orderStatus;

    private List<ItemInBasketDTO> basket;

    private BigDecimal basketTotal;

    public GetOrderDTO() {
    }

    public GetOrderDTO(Order order) {
        this.id = order.getId();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus();
        this.basket = order.getBasket().entrySet().stream()
                .map(itemIntegerEntry -> new ItemInBasketDTO(itemIntegerEntry.getKey(), itemIntegerEntry.getValue()))
                .collect(Collectors.toList());
        this.basketTotal = order.getTotalValueOfBasket();
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getBasketTotal() {
        return basketTotal;
    }

    public List<ItemInBasketDTO> getBasket() {
        return basket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetOrderDTO that = (GetOrderDTO) o;
        return id == that.id && address.equals(that.address) && orderStatus.equals(that.orderStatus) && Objects.equals(basket, that.basket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, orderStatus, basket);
    }

    @Override
    public String toString() {
        return "GetOrderDTO{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                ", basket=" + basket +
                '}';
    }
}

