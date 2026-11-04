# pedidos-eventos

Projeto de estudo de mensageria com Kafka e Spring Boot, no domínio de pedidos de e-commerce.
Demonstra **Transactional Outbox**, **idempotência** e **Saga coreografada com compensação**.

## Arquitetura

```
Pedidos --pedidos.criados--> Estoque --estoque.reservado--> Pagamento
   ^                            ^                               |
   |                            +------ pagamentos.recusados ---+  (compensação: libera a reserva)
   +----- pagamentos.aprovados / pagamentos.recusados ----------+  (atualiza o status do pedido)
```

| Serviço | Responsabilidade | Banco |
|---|---|---|
| `pedidos` | API REST, grava o pedido e o evento no outbox, atualiza o status | Postgres (5432) |
| `estoque` | Reserva itens, publica `estoque.reservado`, libera a reserva se o pagamento for recusado | Postgres (5433) |
| `pagamento` | Aprova pedidos até R$ 1.000 e recusa os acima disso | nenhum |

## Padrões aplicados

- **Transactional Outbox** (`pedidos` e `estoque`): o dado de negócio e o evento são gravados na mesma transação. Um `Relay` agendado publica os eventos pendentes e só os marca como enviados depois da confirmação do broker (`send(...).get()`).
- **Idempotência** (`estoque`): a tabela `mensagem_processada` guarda o id dos pedidos já tratados; mensagens repetidas são ignoradas.
- **Saga coreografada**: cada serviço reage ao evento anterior, sem orquestrador. Se o pagamento falha, o Estoque desfaz a reserva e o Pedidos cancela o pedido.

A entrega é *at-least-once*: eventos podem se repetir, e os consumers estão preparados para isso.

## Como rodar

Java 21 e Docker são necessários.

```bash
docker compose up -d
(cd estoque && ./mvnw spring-boot:run) &
(cd pagamento && ./mvnw spring-boot:run) &
(cd pedidos && ./mvnw spring-boot:run) &
```

Pedido aprovado e pedido recusado:

```bash
curl -X POST localhost:8080/pedidos -H 'Content-Type: application/json' \
  -d '{"cliente":"Ana","valorTotal":100.00}'
curl -X POST localhost:8080/pedidos -H 'Content-Type: application/json' \
  -d '{"cliente":"Bruno","valorTotal":5000.00}'
```

Consultar o status final (`CONFIRMADO` ou `CANCELADO`):

```bash
curl localhost:8080/pedidos/<idPedido>
```

## Testes

```bash
(cd pedidos && ./mvnw verify)
(cd estoque && ./mvnw verify)
(cd pagamento && ./mvnw verify)
```

O GitHub Actions roda os três módulos a cada pull request.

## Limites conhecidos

- O par `existsById` + `save` no consumer do Estoque não impede duas cópias simultâneas da mesma mensagem; nesse caso a chave primária faz uma das gravações falhar.
- O `pagamento` não tem banco nem outbox: ele publica direto no Kafka depois de consumir. A decisão é determinística e os consumers seguintes são idempotentes.
- Banco com `ddl-auto=update` e credenciais fixas: apenas para desenvolvimento. Em produção, migrações versionadas (Flyway) e segredos fora do código.
- Não há testes de integração com Kafka real (por exemplo Testcontainers).
