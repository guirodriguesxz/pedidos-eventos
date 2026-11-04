package com.guilherme.estoque;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PedidoConsumer {

    private final MensagemProcessadaRepository repository;

    public PedidoConsumer(MensagemProcessadaRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "pedidos.criados")
    @Transactional
    public void receber(PedidoCriado pedido) {
        if (repository.existsById(pedido.idPedido())) {
            return;
        }
        System.out.println("Reservando estoque para pedido " + pedido.idPedido() + " do cliente " + pedido.cliente());
        repository.save(new MensagemProcessada(pedido.idPedido()));
    }
}
