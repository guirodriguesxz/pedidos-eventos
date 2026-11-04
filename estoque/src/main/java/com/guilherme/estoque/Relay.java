package com.guilherme.estoque;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Component
public class Relay {

    private final OutboxRepository outbox;
    private final KafkaTemplate<String, String> kafka;

    public Relay(OutboxRepository outbox, KafkaTemplate<String, String> kafka) {
        this.outbox = outbox;
        this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publicarPendentes() throws ExecutionException, InterruptedException {
        for (OutboxEvento evento : outbox.findByEnviadoFalse()) {
            kafka.send(evento.topic, evento.chave, evento.payload).get();
            evento.marcarEnviado();
            outbox.save(evento);
        }
    }
}
