package com.empresa.giros.procesamientodatosgiros.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "eps")
public class EpsEntity extends BaseEntity {

    @Column(name = "cod_eps", nullable = false, unique = true)
    private String codEps;

    @Column(name = "nombre_eps", nullable = false)
    private String nombreEps;

    public String getCodEps() {
        return codEps;
    }

    public void setCodEps(String codEps) {
        this.codEps = codEps;
    }

    public String getNombreEps() {
        return nombreEps;
    }

    public void setNombreEps(String nombreEps) {
        this.nombreEps = nombreEps;
    }
}

