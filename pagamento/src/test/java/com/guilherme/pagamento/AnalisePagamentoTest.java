package com.guilherme.pagamento;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalisePagamentoTest {

    private final AnalisePagamento analise = new AnalisePagamento();

    @Test
    void aprovaValorAbaixoDoLimite() {
        assertTrue(analise.aprova(new BigDecimal("100.00")));
    }

    @Test
    void aprovaValorIgualAoLimite() {
        assertTrue(analise.aprova(new BigDecimal("1000")));
    }

    @Test
    void recusaValorAcimaDoLimite() {
        assertFalse(analise.aprova(new BigDecimal("1000.01")));
    }
}
