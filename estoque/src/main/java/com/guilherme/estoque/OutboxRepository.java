package com.guilherme.estoque;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvento, String> {
    List<OutboxEvento> findByEnviadoFalse();
}
