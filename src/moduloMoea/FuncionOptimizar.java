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
        for (ValorEnergetico salidaExtraccionDato : cargaIDF.listaValoresEnergeticos) {
            System.out.println(salidaExtraccionDato);
        }
        /*for (ValoresEnergeticos dato : moduloDatos.listaValoresEnergeticos) {
            dato.toString();
        }*/
    }

    @Override
    public synchronized Solution newSolution() {
        Solution solution = new Solution(CargaEP.nVariables, CargaEP.nObjetivos); //n variables totales , n de variables objetivo        
        // se fija dominio de busqueda por cada variable
        // dominio de busqueda basado en valores de idf
        // tambien basados en entrada de script python  
        int k = 0;
        for (int i = 0; i < cargaIDF.listaMateriales.size(); i++) {
            for (int j = 0; j < cargaIDF.listaMateriales.get(i).getCaracteristicas().size(); j++) {
                if (cargaIDF.listaMateriales.get(i).getCaracteristicas(j).isSeleccionado()) {
                    //usar output de la variable listaMateriales
                    double minimo = cargaIDF.listaMateriales.get(i).getCaracteristicas(j).getRangoMin();
                    double maximo = cargaIDF.listaMateriales.get(i).getCaracteristicas(j).getRangoMax();
                    solution.setVariable(k, EncodingUtils.newReal(minimo, maximo));
                    k++;
                }
            }
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
        double objetivo;
        int j = 0;
        for (int i = 0; i < cargaIDF.listaValoresEnergeticos.size(); i++) {
            if (cargaIDF.listaValoresEnergeticos.get(i).isSeleccionado()) {
                objetivo = cargaIDF.listaValoresEnergeticos.get(i).getValor();
                sltn.setObjective(j, objetivo);  //indice de fx objetivo , fx a optimizar
                j++;
            }
        }
        modificaciones.clear();
    }

}
