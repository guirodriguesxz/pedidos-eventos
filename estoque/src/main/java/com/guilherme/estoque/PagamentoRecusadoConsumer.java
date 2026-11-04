package com.guilherme.estoque;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Component
public class PagamentoRecusadoConsumer {

    private final ReservaRepository reservas;
    private final JsonMapper jsonMapper;

    public PagamentoRecusadoConsumer(ReservaRepository reservas, JsonMapper jsonMapper) {
        this.reservas = reservas;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "pagamentos.recusados")
    @Transactional
    public void receber(String mensagem) {
        PagamentoRecusado recusado = jsonMapper.readValue(mensagem, PagamentoRecusado.class);

        reservas.findById(recusado.idPedido()).ifPresent(reserva -> {
            if (reserva.liberada()) {
                return;
            }
            System.out.println("Liberando estoque do pedido " + recusado.idPedido() + ": " + recusado.motivo());
            reserva.liberar();
            reservas.save(reserva);
        });
    }
}
