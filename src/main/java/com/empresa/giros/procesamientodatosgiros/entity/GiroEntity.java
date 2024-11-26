package com.empresa.giros.procesamientodatosgiros.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "giros")
public final class GiroEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "eps_id", nullable = false)
    private EpsEntity epsEntity;

    @ManyToOne
    @JoinColumn(name = "ips_id", nullable = false)
    private IpsEntity ipsEntity;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private UbicacionEntity ubicacionEntity;

    @Column(name = "forma_contratacion")
    private String formaContratacion;

    @Column(name = "total_giro")
    private Double totalGiro;

    @Column(name = "observaciones", nullable = false)
    private String observaciones;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    public EpsEntity getEpsEntity() {
        return epsEntity;
    }

    public void setEpsEntity(final EpsEntity epsEntity) {
        this.epsEntity = epsEntity;
    }

    public IpsEntity getIpsEntity() {
        return ipsEntity;
    }

    public void setIpsEntity(final IpsEntity ipsEntity) {
        this.ipsEntity = ipsEntity;
    }

    public UbicacionEntity getUbicacionEntity() {
        return ubicacionEntity;
    }

    public void setUbicacionEntity(final UbicacionEntity ubicacionEntity) {
        this.ubicacionEntity = ubicacionEntity;
    }

    public String getFormaContratacion() {
        return formaContratacion;
    }

    public void setFormaContratacion(final String formaContratacion) {
        this.formaContratacion = formaContratacion;
    }

    public Double getTotalGiro() {
        return totalGiro;
    }

    public void setTotalGiro(final Double totalGiro) {
        this.totalGiro = totalGiro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(final String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(final String fecha) {
        this.fecha = fecha;
    }
}

