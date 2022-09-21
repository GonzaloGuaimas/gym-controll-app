package com.example.gymsoftware.Objetos;

public class Pago {
    private Integer importe;
    private Integer debe;
    private String formaPago;

    public Pago() {
        //public no-arg constructor needed
    }
    public Pago (Integer importe, Integer debe,String formaPago){
        setImporte(importe);
        setDebe(debe);
        setFormaPago(formaPago);
    }

    public Integer getImporte() {
        return importe;
    }

    public void setImporte(Integer importe) {
        this.importe = importe;
    }

    public Integer getDebe() {
        return this.debe;
    }

    public void setDebe(Integer debe) { this.debe = debe;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
}
