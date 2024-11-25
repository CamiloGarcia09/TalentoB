package com.empresa.giros.procesamientodatosgiros.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ubicacion")
public class UbicacionEntity extends BaseEntity {

    @Column(name = "dane", nullable = false, unique = true)
    private String dane;

    @Column(name = "departamento", nullable = false)
    private String departamento;

    @Column(name = "municipio", nullable = false)
    private String municipio;

    public String getDane() {
        return dane;
    }

    public void setDane(String dane) {
        this.dane = dane;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
