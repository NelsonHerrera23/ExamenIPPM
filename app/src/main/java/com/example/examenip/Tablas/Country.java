package com.example.examenip.Tablas;

public class Country {

    private Integer idPais;
    private String nombrePais;
    private int codigoMarcado;

    public Country() { }

    public Country(String nombrePais, int codigoMarcado, Integer idPais) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
        this.codigoMarcado = codigoMarcado;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public int getCodigoMarcado() {
        return codigoMarcado;
    }

    public void setCodigoMarcado(int codigoMarcado) {
        this.codigoMarcado = codigoMarcado;
    }
}
