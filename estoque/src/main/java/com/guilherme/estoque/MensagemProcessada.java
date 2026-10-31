package com.guilherme.estoque;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MensagemProcessada {
    @Id
    String idPedido;


    public MensagemProcessada(String idPedido) {
        this.idPedido = idPedido;
    }

    protected MensagemProcessada() { }
}