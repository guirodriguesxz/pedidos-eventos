package com.guilherme.pagamento;

import java.math.BigDecimal;

public class AnalisePagamento {

    static final BigDecimal LIMITE = new BigDecimal("1000");

    public boolean aprova(BigDecimal valorTotal) {
        return valorTotal.compareTo(LIMITE) <= 0;
    }
}
