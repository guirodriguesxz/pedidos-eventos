package com.guilherme.pedidos;

import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component

public class Relay {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafka;

    public Relay(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafka) {
        this.outboxRepository = outboxRepository;
        this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publicarPendentes() throws ExecutionException, InterruptedException {
        for (OutboxEvento evento : outboxRepository .findByEnviadoFalse()) {
            kafka.send(evento.topic, evento.chave, evento.payload).get();
            evento.marcarEnviado();
            outboxRepository.save(evento);
        }
    }
}
