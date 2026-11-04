package com.guilherme.pagamento;

import java.math.BigDecimal;

public record EstoqueReservado(String idPedido, BigDecimal valorTotal, String cliente) {
}
