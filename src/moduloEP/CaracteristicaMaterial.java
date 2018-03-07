/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloEP;

/**
 *
 * @author Nicoyarce
 */
public class CaracteristicaMaterial {

    private String caracteristica;
    private double valor;
    private double rangoMin;
    private double rangoMax;
    private boolean seleccionado;

    public CaracteristicaMaterial(String caracteristica, double valor, double rangoMin, double rangoMax, boolean seleccionado) {
        this.caracteristica = caracteristica;
        this.valor = valor;
        this.rangoMin = rangoMin;
        this.rangoMax = rangoMax;
        this.seleccionado = seleccionado;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getRangoMin() {
        return rangoMin;
    }

    public void setRangoMin(double rangoMin) {
        this.rangoMin = rangoMin;
    }

    public double getRangoMax() {
        return rangoMax;
    }

    public void setRangoMax(double rangoMax) {
        this.rangoMax = rangoMax;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

}
