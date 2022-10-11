package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddOrderDTO;

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
    private int id;

    @NotNull
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private OrderStatus orderStatus;

    @ElementCollection
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name="order_id", referencedColumnName = "id"))
    @MapKeyJoinColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<Item, Integer> basket;

    public Order() {
    }

    public Order(AddOrderDTO addOrderDTO) {
        this.address = addOrderDTO.getAddress();
        this.orderStatus = new OrderStatus("Created");
        this.basket = new HashMap<>();
    }

    public boolean addItemToBasket(Item item, int quantity) {
        boolean successful;

        if(basket.containsKey(item)) {
            successful = false;
        } else {
            basket.put(item, quantity);
            successful = true;
        }

        return successful;
    }

    public boolean basketHasItem(Item item) {
        return basket.containsKey(item);
    }

    public void removeItemFromBasket(Item item) {
        basket.remove(item);
    }

    public void updateQuantity(Item item, int quantity) {
        basket.put(item, quantity);
    }

    public BigDecimal getTotalValueOfBasket() {

        return basket.keySet().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(basket.get(item))))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
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
