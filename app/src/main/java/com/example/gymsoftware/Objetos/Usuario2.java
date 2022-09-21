package com.example.gymsoftware.Objetos;

public class Usuario2 {

    private String Nombre;
    private String Apellido;
    private Integer DNI;
    private String fechaInscripcion;
    private String fechaPago;
    private String fechaVencimiento;
    private String Condicion="";
    private boolean vencido;
    private boolean proxAVencer;
    private String pathfoto;
    private String telefono;

    public Usuario2(String nombre, String apellido, Integer DNI, String fechaInscripcion, String fechaPago, String fechaVencimiento, String condicion, boolean vencido, boolean proxAVencer, String pathfoto, String telefono) {
        Nombre = nombre;
        Apellido = apellido;
        this.DNI = DNI;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaPago = fechaPago;
        this.fechaVencimiento = fechaVencimiento;
        Condicion = condicion;
        this.vencido = vencido;
        this.proxAVencer = proxAVencer;
        this.pathfoto = pathfoto;
        this.telefono = telefono;
    }

    public Usuario2(){

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

    public Integer getDNI() {
        return DNI;
    }

    public void setDNI(Integer DNI) {
        this.DNI = DNI;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCondicion() {
        return Condicion;
    }

    public void setCondicion(String condicion) {
        Condicion = condicion;
    }

    public boolean isVencido() {
        return vencido;
    }

    public void setVencido(boolean vencido) {
        this.vencido = vencido;
    }

    public boolean isProxAVencer() {
        return proxAVencer;
    }

    public void setProxAVencer(boolean proxAVencer) {
        this.proxAVencer = proxAVencer;
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
}
