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
    String status;

    protected Pedido() { }

    public Pedido(String id, String cliente, BigDecimal total) {
        this.id = id;
        this.cliente = cliente;
        this.total = total;
        this.status = "CRIADO";
    }

    public String getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void confirmar() {
        this.status = "CONFIRMADO";
    }

    public void cancelar() {
        this.status = "CANCELADO";
    }
}
