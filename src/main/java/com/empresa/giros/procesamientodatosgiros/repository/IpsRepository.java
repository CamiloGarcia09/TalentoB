package com.empresa.giros.procesamientodatosgiros.repository;

import com.empresa.giros.procesamientodatosgiros.entity.IpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IpsRepository extends JpaRepository<IpsEntity, UUID> {
    Optional<IpsEntity> findByNitIps(String nitIps);
}
