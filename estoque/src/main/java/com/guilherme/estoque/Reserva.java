package com.guilherme.estoque;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Reserva {

    @Id
    String idPedido;
    String status;

    protected Reserva() { }

    public Reserva(String idPedido) {
        this.idPedido = idPedido;
        this.status = "RESERVADA";
    }

    public boolean liberada() {
        return "LIBERADA".equals(status);
    }

    public void liberar() {
        this.status = "LIBERADA";
    }
}
