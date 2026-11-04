package com.guilherme.pedidos;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;


import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final OutboxRepository outbox;
    private final JsonMapper jsonMapper;

    public PedidoController(PedidoRepository repository, OutboxRepository outbox, JsonMapper jsonMapper) {
        this.repository = repository;
        this.outbox = outbox;
        this.jsonMapper = jsonMapper;


    }

    @PostMapping
    @Transactional

    public ResponseEntity<PedidoCriado> criar(@RequestBody NovoPedido novo) {
        String id = UUID.randomUUID().toString();
        repository.save(new Pedido(id, novo.cliente(), novo.valorTotal()));
        PedidoCriado evento = new PedidoCriado(id, novo.valorTotal(), novo.cliente());
        String json = jsonMapper.writeValueAsString(evento);
        outbox.save(new OutboxEvento(UUID.randomUUID().toString(),"pedidos.criados", id, json));
        return ResponseEntity.status(201).body(evento);


    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
