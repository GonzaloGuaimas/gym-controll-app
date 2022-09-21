package com.example.gymsoftware.Objetos;

import com.example.gymsoftware.MainActivity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Usuario {

    private String Nombre;
    private String Apellido;
    private String DNI;
    private String pathfoto;
    private String telefono;
    private String condicion;   //renov
    private String estado;
    private long fechaInscripcion;
    private long fechaVencimiento;
    private long idAut;
    private List<Actividad> actividades;
    private Integer saldo;

    public Usuario(String nombre, String apellido, String DNI, String pathfoto, String telefono, String condicion, String estado, long fechaInscripcion, long fechaVencimiento,Integer saldo) {
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.DNI = DNI;
        this.pathfoto = pathfoto;
        this.telefono = telefono;
        this.condicion = condicion;
        this.estado = estado;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaVencimiento = fechaVencimiento;

        this.saldo = saldo;
    }
    public Usuario(String nombre, String apellido, String DNI, String pathfoto, String telefono, String condicion, String estado, long fechaInscripcion, long fechaVencimiento,Integer saldo,long idAut) {
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.DNI = DNI;
        this.pathfoto = pathfoto;
        this.telefono = telefono;
        this.condicion = condicion;
        this.estado = estado;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaVencimiento = fechaVencimiento;

        this.saldo = saldo;
        this.idAut = idAut;
    }

    public long getIdAut() {
        return idAut;
    }

    public void setIdAut(long idAut) {
        this.idAut = idAut;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public Usuario(){

    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getPathfoto() {
        return pathfoto;
    }

    public void setPathfoto(String pathfoto) {
        this.pathfoto = pathfoto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getFechaInscripcionDD() {
        return MainActivity.formatoddMMyyyy.format(new Date(this.fechaInscripcion));
    }

    public long getFechaInscripcion() {
        return this.fechaInscripcion;
    }

    public void setFechaInscripcion(long fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getFechaVencimientoDD() {
        return MainActivity.formatoddMMyyyy.format(new Date(this.fechaVencimiento));
    }


    public long getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public void setFechaVencimiento(long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }


    public long aumentarMes(long timeStamp){
        Date fechaInicial = new Date(timeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicial);
        cal.add(Calendar.MONTH, 1);
        return cal.getTimeInMillis();
    }
}
