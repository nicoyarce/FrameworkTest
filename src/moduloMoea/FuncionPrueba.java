package moduloMoea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import moduloIDF.CargaIDF;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
//Funcion de optimizacion de archivos IDF
public class FuncionPrueba extends AbstractProblem {

    public CargaIDF moduloDatos = new CargaIDF();
    public ArrayList<String> modificaciones = new ArrayList<>();

    public FuncionPrueba() throws FileNotFoundException, IOException {
        super(CargaIDF.nVariables, CargaIDF.nObjetivos);
        System.out.println("nVariables: " + CargaIDF.nVariables + " - " + "nObjetivos: " + CargaIDF.nObjetivos);
        //////Operaciones sobre IDF//////        
        moduloDatos.simularIDF();
        moduloDatos.extraerDatosReporte();
        System.out.println("Valores originales:");
        System.out.println(moduloDatos.salidaExtraccionDatos.toString());
        /*for (ValoresEnergeticos dato : moduloDatos.salidaExtraccionDatos) {
            dato.toString();
        }*/ 
    }

    @Override
    public synchronized Solution newSolution() {
        Solution solution = new Solution(CargaIDF.nVariables, CargaIDF.nObjetivos); //n variables totales , n de variables objetivo        
        // se fija dominio de busqueda por cada variable
        // dominio de busqueda basado en valores de idf
        // tambien basados en entrada de script python      
        double numero;
        double delta;
        for (int i = 0; i < CargaIDF.nVariables; i++) {
            numero = Double.parseDouble(moduloDatos.salidaAbrirIDF.get(i));
            //capturar output de la variable salidaAbrirIDF, cuando tenga valor rangomin rango max
            delta = numero * 1.5;
            delta = truncar(delta);
            solution.setVariable(i, EncodingUtils.newReal(delta - numero, delta + numero));
        }
        return solution;
    }

    @Override
    public synchronized void evaluate(Solution sltn) {
        /*variables de todo el problema */
        double variable;
        for (int i = 0; i < CargaIDF.nVariables; i++) {
            variable = EncodingUtils.getReal(sltn.getVariable(i));
            variable = truncar(variable);
            modificaciones.add(String.valueOf(variable));
        }        
        try {
            moduloDatos.modificarIDF(modificaciones);
            moduloDatos.simularIDF();
            moduloDatos.extraerDatosReporte();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        /*funciones objetivo de todo el problema*/
        //arreglar seleccion de objetivos        
        double objetivo;
        for (int i = 0; i < CargaIDF.nObjetivos; i++) {
            objetivo = moduloDatos.salidaExtraccionDatos.get(i).getValor();
            sltn.setObjective(i, objetivo);  //indice de fx objetivo , fx a optimizar
        }
        modificaciones.clear();
    }
    private double truncar(double n){
        n = (double)Math.round(n * 1000) / 1000;
        return n;
    }
}
