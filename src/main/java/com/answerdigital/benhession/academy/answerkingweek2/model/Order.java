package com.answerdigital.benhession.academy.answerkingweek2.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ElementCollection
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name="order_id", referencedColumnName = "id"))
    @MapKeyJoinColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<Item, Integer> basket;

    public Order() {
    }

    public Order(String address) {
        this.address = address;
        this.orderStatus = OrderStatus.IN_PROGRESS;
        this.basket = new HashMap<>();
    }

    public void addItemToBasket(Item item, Integer quantity) {
        basket.put(item, quantity);
    }

    public void updateItemInBasket(Item item, Integer quantity) {
        basket.put(item, quantity);
    }

    public void removeItemFromBasket(Item item) {
        basket.remove(item);
    }

    public boolean basketHasItem(Item item) {
        return basket.containsKey(item);
    }

    public BigDecimal getTotalValueOfBasket() {
        return basket.keySet().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(basket.get(item))))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<Item, Integer> getBasket() {
        return new HashMap<>(basket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
