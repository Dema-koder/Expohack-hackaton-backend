package ru.demyan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.demyan.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}
