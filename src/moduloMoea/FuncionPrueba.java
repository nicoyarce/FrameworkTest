package moduloMoea;

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
//Problema con dos variables y con dos objetivos
public class FuncionPrueba extends AbstractProblem {

    public CargaIDF moduloDatos = new CargaIDF();
    public ArrayList<String> salidaAbrirIDF = new ArrayList<>();
    public ArrayList<ArrayList> salidaExtraccionDatos = new ArrayList<>();
    public ArrayList<String> modificaciones = new ArrayList<>();

    public FuncionPrueba() throws IOException {
        super(1, 1); //n variables totales , n de variables objetivo
        salidaAbrirIDF = moduloDatos.abrirIDF();
        moduloDatos.simularIDF();
        salidaExtraccionDatos = moduloDatos.extraerDatosReporte();
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives()); //n variables totales , n de variables objetivo        
        // se fija dominio de busqueda por cada variable
        // dominio de busqueda basado en valores de idf
        // primer caso: thickness        
        double numero = Double.parseDouble(salidaAbrirIDF.get(0));
        double delta = numero * 1.5;
        solution.setVariable(0, EncodingUtils.newReal(delta - numero, delta + numero));
        return solution;
    }

    @Override
    public void evaluate(Solution sltn) {
        /* numerar variables de todo el problema */
        double x = EncodingUtils.getReal(sltn.getVariable(0));
        modificaciones.add(String.valueOf(x));
        try {
            moduloDatos.modificarIDF(modificaciones);
            //salidaAbrirIDF = moduloDatos.abrirIDF();
            moduloDatos.simularIDF();
            salidaExtraccionDatos = moduloDatos.extraerDatosReporte();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        double objetivo = Double.parseDouble((String) salidaExtraccionDatos.get(1).get(0));

        /*numerar funciones objetivo de todo el problema*/
        sltn.setObjective(0, objetivo);  //indice de fx objetivo , fx a optimizar

    }

}
