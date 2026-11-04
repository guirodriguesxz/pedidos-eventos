package com.guilherme.pedidos;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Component
public class PagamentoConsumer {

    private final PedidoRepository repository;
    private final JsonMapper jsonMapper;

    public PagamentoConsumer(PedidoRepository repository, JsonMapper jsonMapper) {
        this.repository = repository;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "pagamentos.aprovados")
    @Transactional
    public void aprovado(String mensagem) {
        PagamentoAprovado evento = jsonMapper.readValue(mensagem, PagamentoAprovado.class);
        repository.findById(evento.idPedido()).ifPresent(pedido -> {
            pedido.confirmar();
            repository.save(pedido);
        });
    }

    @KafkaListener(topics = "pagamentos.recusados")
    @Transactional
    public void recusado(String mensagem) {
        PagamentoRecusado evento = jsonMapper.readValue(mensagem, PagamentoRecusado.class);
        repository.findById(evento.idPedido()).ifPresent(pedido -> {
            pedido.cancelar();
            repository.save(pedido);
        });
    }
}
