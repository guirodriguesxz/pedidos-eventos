package com.guilherme.estoque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PedidoConsumerTest {

    private static final String MENSAGEM = "{\"idPedido\":\"p1\",\"valorTotal\":50.00,\"cliente\":\"Ana\"}";

    private MensagemProcessadaRepository processadas;
    private ReservaRepository reservas;
    private OutboxRepository outbox;
    private PedidoConsumer consumer;

    @BeforeEach
    void preparar() {
        processadas = mock(MensagemProcessadaRepository.class);
        reservas = mock(ReservaRepository.class);
        outbox = mock(OutboxRepository.class);
        consumer = new PedidoConsumer(processadas, reservas, outbox, JsonMapper.builder().build());
    }

    @Test
    void pedidoNovoGeraReservaEEventoNoOutbox() {
        when(processadas.existsById("p1")).thenReturn(false);

        consumer.receber(MENSAGEM);

        verify(reservas).save(any(Reserva.class));
        verify(outbox).save(any(OutboxEvento.class));
        verify(processadas).save(any(MensagemProcessada.class));
    }

    @Test
    void pedidoJaProcessadoEIgnorado() {
        when(processadas.existsById("p1")).thenReturn(true);

        consumer.receber(MENSAGEM);

        verify(reservas, never()).save(any(Reserva.class));
        verify(outbox, never()).save(any(OutboxEvento.class));
    }
}
