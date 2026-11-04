package com.guilherme.estoque;

import java.math.BigDecimal;

public record EstoqueReservado(String idPedido, BigDecimal valorTotal, String cliente) {
}
