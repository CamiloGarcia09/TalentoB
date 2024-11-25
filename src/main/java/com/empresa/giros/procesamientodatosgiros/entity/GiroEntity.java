package com.empresa.giros.procesamientodatosgiros.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "giros")
public class GiroEntity extends BaseEntity {

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

    @Column(name = "valor_ordenado")
    private Double valorOrdenado;

    @Column(name = "total_giro")
    private Double totalGiro;

    @Column(name = "observaciones", nullable = false)
    private String observaciones;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    public EpsEntity getEpsEntity() {
        return epsEntity;
    }

    public void setEpsEntity(EpsEntity epsEntity) {
        this.epsEntity = epsEntity;
    }

    public IpsEntity getIpsEntity() {
        return ipsEntity;
    }

    public void setIpsEntity(IpsEntity ipsEntity) {
        this.ipsEntity = ipsEntity;
    }

    public UbicacionEntity getUbicacionEntity() {
        return ubicacionEntity;
    }

    public void setUbicacionEntity(UbicacionEntity ubicacionEntity) {
        this.ubicacionEntity = ubicacionEntity;
    }

    public String getFormaContratacion() {
        return formaContratacion;
    }

    public void setFormaContratacion(String formaContratacion) {
        this.formaContratacion = formaContratacion;
    }

    public Double getValorOrdenado() {
        return valorOrdenado;
    }

    public void setValorOrdenado(Double valorOrdenado) {
        this.valorOrdenado = valorOrdenado;
    }

    public Double getTotalGiro() {
        return totalGiro;
    }

    public void setTotalGiro(Double totalGiro) {
        this.totalGiro = totalGiro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // Getters y setters
}

