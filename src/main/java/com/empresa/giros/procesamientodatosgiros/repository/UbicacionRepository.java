package com.empresa.giros.procesamientodatosgiros.repository;

import com.empresa.giros.procesamientodatosgiros.entity.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, UUID> {
    Optional<UbicacionEntity> findByDane(String dane);
}
