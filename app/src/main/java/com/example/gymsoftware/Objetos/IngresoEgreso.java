package com.example.gymsoftware.Objetos;

import com.example.gymsoftware.MainActivity;

import java.util.Date;

public class IngresoEgreso {

    private long id;
    private long idAut;
    private Integer importe;
    private String formaPago;
    private String comentario;
    private String DNI;
    private String nombre;


    public IngresoEgreso(long id, Integer importe, String formaPago, String comentario, String DNI, String nombre) {
        this.id = id;
        this.importe = importe;
        this.formaPago = formaPago;
        this.comentario = comentario;
        this.DNI = DNI;
        this.nombre = nombre;
    }
    public IngresoEgreso(long id, Integer importe, String formaPago, String comentario, String DNI, String nombre, long idAut) {
        this.id = id;
        this.importe = importe;
        this.formaPago = formaPago;
        this.comentario = comentario;
        this.DNI = DNI;
        this.nombre = nombre;
        this.idAut = idAut;
    }

    public IngresoEgreso (){

    }

    public long getIdAut() {
        return idAut;
    }

    public void setIdAut(long idAut) {
        this.idAut = idAut;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getImporte() {
        return importe;
    }

    public void setImporte(Integer importe) {
        this.importe = importe;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getHora() {
        return MainActivity.formatoHH.format(new Date(this.id));
    }
}
