# pedidos-eventos

Projeto de estudo de mensageria com Kafka e Spring Boot, no domínio de pedidos de e-commerce.

## Arquitetura atual

```
POST /pedidos -> pedidos -> Kafka (topic pedidos.criados) -> estoque
```

- `pedidos`: API REST que recebe um pedido e publica o evento `PedidoCriado`. A chave da mensagem é o id do pedido, para garantir ordem por pedido.
- `estoque`: consumer do topic `pedidos.criados`, no consumer group `estoque`.
- `docker-compose.yml`: Kafka em modo KRaft (sem Zookeeper), apenas para desenvolvimento.

## Como rodar

Java 21 e Docker são necessários.

```bash
docker compose up -d
(cd estoque && ./mvnw spring-boot:run)
(cd pedidos && ./mvnw spring-boot:run)
curl -X POST localhost:8080/pedidos -H 'Content-Type: application/json' \
  -d '{"cliente":"Ana","valorTotal":59.90}'
```

## Roteiro

- [x] Producer e consumer com Kafka
- [ ] Persistência dos pedidos em banco
- [ ] Transactional Outbox
- [ ] Idempotência no consumer
- [ ] Pagamento e Saga com compensação
