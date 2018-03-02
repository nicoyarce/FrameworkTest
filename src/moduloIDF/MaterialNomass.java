/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloIDF;

/**
 *
 * @author Nicoyarce
 */
public class MaterialNomass {
    private String name;
    private String roughness;
    private double thermalResistance;
    private double thermalAbsorptance;
    private double visibleAbsorptance;

    public MaterialNomass(String name, String roughness, double thermalResistance, double thermalAbsorptance, double visibleAbsorptance) {
        this.name = name;
        this.roughness = roughness;
        this.thermalResistance = thermalResistance;
        this.thermalAbsorptance = thermalAbsorptance;
        this.visibleAbsorptance = visibleAbsorptance;
    }
    
    
}
