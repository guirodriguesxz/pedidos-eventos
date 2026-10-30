package com.guilherme.pedidos;

import jakarta.persistence.*;

@Entity



public class OutboxEvento {
    @Id
    String id;
    String topic;
    @Column(length = 2000)
    String payload;
    String chave;
    boolean enviado = false;

    protected OutboxEvento() {}

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
