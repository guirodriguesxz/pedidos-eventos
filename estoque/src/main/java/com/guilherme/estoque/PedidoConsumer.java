package com.guilherme.estoque;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

@Component
public class PedidoConsumer {

    private final MensagemProcessadaRepository processadas;
    private final ReservaRepository reservas;
    private final OutboxRepository outbox;
    private final JsonMapper jsonMapper;

    public PedidoConsumer(MensagemProcessadaRepository processadas, ReservaRepository reservas,
                          OutboxRepository outbox, JsonMapper jsonMapper) {
        this.processadas = processadas;
        this.reservas = reservas;
        this.outbox = outbox;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "pedidos.criados")
    @Transactional
    public void receber(String mensagem) {
        PedidoCriado pedido = jsonMapper.readValue(mensagem, PedidoCriado.class);

        if (processadas.existsById(pedido.idPedido())) {
            return;
        }
        System.out.println("Reservando estoque para pedido " + pedido.idPedido() + " do cliente " + pedido.cliente());
        reservas.save(new Reserva(pedido.idPedido()));

        EstoqueReservado evento = new EstoqueReservado(pedido.idPedido(), pedido.valorTotal(), pedido.cliente());
        outbox.save(new OutboxEvento(UUID.randomUUID().toString(), "estoque.reservado",
                pedido.idPedido(), jsonMapper.writeValueAsString(evento)));

        processadas.save(new MensagemProcessada(pedido.idPedido()));
    }
}
