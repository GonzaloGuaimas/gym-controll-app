package com.example.gymsoftware.Objetos;

public class Arqueo {

    private long id;
    private long idAut;
    private int efectivo_sistema;
    private int efectivo_real;
    private int debito;
    private int credito;
    private int transferencia;
    private int gastos;
    private int total;
    private String comentario;

    public Arqueo(long id, int efectivo_sistema, int efectivo_real, int debito, int credito, int transferencia, int gastos, int total, String comentario) {
        this.id = id;
        this.efectivo_sistema = efectivo_sistema;
        this.efectivo_real = efectivo_real;
        this.debito = debito;
        this.credito = credito;
        this.transferencia = transferencia;
        this.gastos = gastos;
        this.total = total;
        this.comentario = comentario;
    }
    public Arqueo(long id, int efectivo_sistema, int efectivo_real, int debito, int credito, int transferencia, int gastos, int total, String comentario,long idAut) {
        this.id = id;
        this.efectivo_sistema = efectivo_sistema;
        this.efectivo_real = efectivo_real;
        this.debito = debito;
        this.credito = credito;
        this.transferencia = transferencia;
        this.gastos = gastos;
        this.total = total;
        this.comentario = comentario;
        this.idAut = idAut;
    }

    public Arqueo(){

    }

    public long getIdAut() {
        return idAut;
    }

    public void setIdAut(long idAut) {
        this.idAut = idAut;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEfectivo_sistema() {
        return efectivo_sistema;
    }

    public void setEfectivo_sistema(int efectivo_sistema) {
        this.efectivo_sistema = efectivo_sistema;
    }

    public int getEfectivo_real() {
        return efectivo_real;
    }

    public void setEfectivo_real(int efectivo_real) {
        this.efectivo_real = efectivo_real;
    }

    public int getDebito() {
        return debito;
    }

    public void setDebito(int debito) {
        this.debito = debito;
    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }

    public int getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(int transferencia) {
        this.transferencia = transferencia;
    }

    public int getGastos() {
        return gastos;
    }

    public void setGastos(int gastos) {
        this.gastos = gastos;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
