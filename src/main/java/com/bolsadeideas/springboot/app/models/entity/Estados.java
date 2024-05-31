package com.bolsadeideas.springboot.app.models.entity;

public enum Estados {
    PENDIENTE,
    REALIZANDO,
    FINALIZADO;

    public String getEstados() {
        return this.name();
    }

}
