package moduloMoea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import moduloEP.CargaEP;
import moduloEP.ValorEnergetico;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
//Funcion de optimizacion de archivos IDF
public class FuncionOptimizar extends AbstractProblem {

    public ArrayList<String> modificaciones = new ArrayList<>();
    public CargaEP cargaIDF;

    public FuncionOptimizar(CargaEP cargaIDF) throws FileNotFoundException, IOException {
        super(CargaEP.nVariables, CargaEP.nObjetivos);
        this.cargaIDF = cargaIDF;
        System.out.println("nVariables: " + CargaEP.nVariables + " - " + "nObjetivos: " + CargaEP.nObjetivos);
        //////Operaciones sobre IDF//////        
        this.cargaIDF.simularIDF();
        this.cargaIDF.extraerDatosReporte();
        System.out.println("Valores originales:");
        for (ValorEnergetico salidaExtraccionDato : cargaIDF.salidaExtraccionDatos) {
            System.out.println(salidaExtraccionDato);
        }
        /*for (ValoresEnergeticos dato : moduloDatos.salidaExtraccionDatos) {
            dato.toString();
        }*/
    }

    @Override
    public synchronized Solution newSolution() {
        Solution solution = new Solution(CargaEP.nVariables, CargaEP.nObjetivos); //n variables totales , n de variables objetivo        
        // se fija dominio de busqueda por cada variable
        // dominio de busqueda basado en valores de idf
        // tambien basados en entrada de script python       
        for (int i = 0; i < CargaEP.nVariables; i++) {
            //usar output de la variable salidaAbrirIDF
            double minimo = cargaIDF.salidaAbrirIDF.get(i).getRangoMin();
            double maximo = cargaIDF.salidaAbrirIDF.get(i).getRangoMax();
            solution.setVariable(i, EncodingUtils.newReal(minimo, maximo));
        }
        return solution;
    }

    @Override
    public synchronized void evaluate(Solution sltn) {
        /*variables de todo el problema */
        double variable;
        for (int i = 0; i < CargaEP.nVariables; i++) {
            variable = EncodingUtils.getReal(sltn.getVariable(i));
            variable = cargaIDF.truncar(variable);
            modificaciones.add(String.valueOf(variable));
        }
        try {
            cargaIDF.modificarIDF(modificaciones);
            cargaIDF.simularIDF();
            cargaIDF.extraerDatosReporte();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        /*funciones objetivo de todo el problema*/
        //arreglar seleccion de objetivos        
        double objetivo;
        for (int i = 0; i < CargaEP.nObjetivos; i++) {
            objetivo = cargaIDF.salidaExtraccionDatos.get(i).getValor();
            sltn.setObjective(i, objetivo);  //indice de fx objetivo , fx a optimizar
        }
        modificaciones.clear();
    }

}
