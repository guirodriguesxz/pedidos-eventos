package com.guilherme.estoque;

import java.math.BigDecimal;

public record PedidoCriado(String idPedido, BigDecimal valorTotal, String cliente) {
}
