package com.empresa.giros.procesamientodatosgiros.repository;

import com.empresa.giros.procesamientodatosgiros.entity.GiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GiroRepository extends JpaRepository<GiroEntity, UUID> {
}

