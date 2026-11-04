package com.guilherme.pedidos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoTest {

    @Test
    void pedidoNovoComecaComoCriado() {
        Pedido pedido = new Pedido("1", "Ana", new BigDecimal("10"));
        assertEquals("CRIADO", pedido.getStatus());
    }

    @Test
    void confirmarMudaOStatus() {
        Pedido pedido = new Pedido("1", "Ana", new BigDecimal("10"));
        pedido.confirmar();
        assertEquals("CONFIRMADO", pedido.getStatus());
    }

    @Test
    void cancelarMudaOStatus() {
        Pedido pedido = new Pedido("1", "Ana", new BigDecimal("10"));
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getStatus());
    }
}
