package ru.demyan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.demyan.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
