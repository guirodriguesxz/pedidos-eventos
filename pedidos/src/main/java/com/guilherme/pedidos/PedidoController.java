package com.guilherme.pedidos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoPublisher publisher;

    public PedidoController(PedidoPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<PedidoCriado> criar(@RequestBody NovoPedido novo) {
        String id = UUID.randomUUID().toString();
        PedidoCriado evento = new PedidoCriado(id, novo.valorTotal(), novo.cliente());
        publisher.publicar(evento);
        return ResponseEntity.status(201).body(evento);
    }
}
