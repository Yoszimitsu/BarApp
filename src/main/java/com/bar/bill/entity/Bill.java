package com.bar.bill.entity;

import com.bar.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id", nullable = false)
    @NotNull
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @NotBlank
    private Order order;

    @Column(name = "company", nullable = false)
    @NotBlank
    private String company;

    @Column(name = "bill_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "price_net", nullable = false)
    @NotNull
    private double priceNet;

    @Column(name = "nip")
    private String nipNumber;

    @Column(name = "customer_name")
    private String customerName;
}
