/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloEP;

import java.util.ArrayList;

/**
 *
 * @author Nicoyarce
 */
public class Material {

    private String name;
    private String roughness;
    private ArrayList<CaracteristicaMaterial> caracteristicas;

    public Material(String name, String roughness, ArrayList<CaracteristicaMaterial> caracteristicas) {
        this.name = name;
        this.roughness = roughness;
        this.caracteristicas = caracteristicas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoughness() {
        return roughness;
    }

    public void setRoughness(String roughness) {
        this.roughness = roughness;
    }

    public ArrayList<CaracteristicaMaterial> getCaracteristicas() {
        return caracteristicas;
    }

    public CaracteristicaMaterial getCaracteristicas(int indice) {
        return caracteristicas.get(indice);
    }

    public void setCaracteristicas(ArrayList<CaracteristicaMaterial> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

}
