package com.guilherme.pagamento;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.ExecutionException;

@Component
public class EstoqueReservadoConsumer {

    private final AnalisePagamento analise = new AnalisePagamento();
    private final KafkaTemplate<String, String> kafka;
    private final JsonMapper jsonMapper;

    public EstoqueReservadoConsumer(KafkaTemplate<String, String> kafka, JsonMapper jsonMapper) {
        this.kafka = kafka;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "estoque.reservado")
    public void receber(String mensagem) throws ExecutionException, InterruptedException {
        EstoqueReservado reserva = jsonMapper.readValue(mensagem, EstoqueReservado.class);

        if (analise.aprova(reserva.valorTotal())) {
            System.out.println("Pagamento aprovado para pedido " + reserva.idPedido());
            publicar("pagamentos.aprovados", reserva.idPedido(), new PagamentoAprovado(reserva.idPedido()));
        } else {
            System.out.println("Pagamento recusado para pedido " + reserva.idPedido());
            publicar("pagamentos.recusados", reserva.idPedido(),
                    new PagamentoRecusado(reserva.idPedido(), "valor acima do limite"));
        }
    }

    private void publicar(String topic, String chave, Object evento) throws ExecutionException, InterruptedException {
        kafka.send(topic, chave, jsonMapper.writeValueAsString(evento)).get();
    }
}
