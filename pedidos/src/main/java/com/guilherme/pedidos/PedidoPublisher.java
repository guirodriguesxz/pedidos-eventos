package com.guilherme.pedidos;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoPublisher {

    private final KafkaTemplate<String, PedidoCriado> kafka;

    public PedidoPublisher(KafkaTemplate<String, PedidoCriado> kafka) {
        this.kafka = kafka;
    }

    public void publicar(PedidoCriado evento) {
        kafka.send("pedidos.criados", evento.idPedido(), evento);
    }
}