package com.guilherme.estoque;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OutboxEvento {

    @Id
    String id;
    String topic;
    String chave;
    @Column(length = 2000)
    String payload;
    boolean enviado = false;

    protected OutboxEvento() { }

    public OutboxEvento(String id, String topic, String chave, String payload) {
        this.id = id;
        this.topic = topic;
        this.chave = chave;
        this.payload = payload;
    }

    public void marcarEnviado() {
        this.enviado = true;
    }
}
