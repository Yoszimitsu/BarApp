package com.bar.orderitems.entity;

import com.bar.order.entity.Order;
import com.bar.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "order_item_id", nullable = false)
    @NotNull
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull
    private Order order;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    private Product product;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private int quantity;
}
