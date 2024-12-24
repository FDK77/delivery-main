package com.example.delivery2.repositories;

import com.example.delivery2.models.Deliverer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DelivererRepository extends JpaRepository<Deliverer, UUID> {
}
