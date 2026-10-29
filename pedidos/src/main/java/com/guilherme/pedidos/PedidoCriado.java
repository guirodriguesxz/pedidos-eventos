package com.guilherme.pedidos;

import java.math.BigDecimal;

public record PedidoCriado(String idPedido, BigDecimal valorTotal, String cliente) {
}
