package com.guilherme.pedidos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;


@Entity
public class Pedido {
    @Id
    String id;
    String cliente;
    BigDecimal total;
    protected Pedido() { }
    public Pedido(String id, String cliente, BigDecimal total) {
        this.id = id;
        this.cliente = cliente;
        this.total = total;

    }
}
