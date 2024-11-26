package com.empresa.giros.procesamientodatosgiros.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ips")
public final class IpsEntity extends BaseEntity {

    @Column(name = "nit_ips", nullable = false, unique = true)
    private String nitIps;

    @Column(name = "nombre_ips", nullable = false)
    private String nombreIps;

    public String getNitIps() {
        return nitIps;
    }

    public void setNitIps(final String nitIps) {
        this.nitIps = nitIps;
    }

    public String getNombreIps() {
        return nombreIps;
    }

    public void setNombreIps(final String nombreIps) {
        this.nombreIps = nombreIps;
    }
}
