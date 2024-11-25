package com.empresa.giros.procesamientodatosgiros.repository;

import com.empresa.giros.procesamientodatosgiros.entity.EpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EpsRepository extends JpaRepository<EpsEntity, UUID> {
    Optional<EpsEntity> findByCodEps(String codEps);
}
