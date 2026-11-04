package com.guilherme.pedidos;

import java.math.BigDecimal;

public record NovoPedido(String cliente, BigDecimal valorTotal) {
}
