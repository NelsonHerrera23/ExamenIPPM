package com.example.examenip.Tablas;

public class Contactos {

    private Integer idContacto;
    private String nombreContacto;
    private Integer telefonoContacto;
    private Integer pais;
    private String nota;


    public  Contactos(){

    }

    public Contactos(Integer idContacto, String nombreContacto, Integer telefonoContacto, Integer pais, String nota){
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.telefonoContacto = telefonoContacto;
        this.pais = pais;
        this.nota = nota;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public Integer getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(Integer telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public Integer getPais() {
        return pais;
    }

    public void setPais(Integer pais) {
        this.pais = pais;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
