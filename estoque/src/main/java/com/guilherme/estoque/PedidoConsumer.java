package com.guilherme.estoque;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    @KafkaListener(topics = "pedidos.criados")
    public void receber(PedidoCriado pedido) {
        System.out.println("Reservando estoque para pedido " + pedido.idPedido() + " do cliente " + pedido.cliente() );
    }
}