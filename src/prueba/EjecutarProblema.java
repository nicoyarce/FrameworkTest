package prueba;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

/**
 *
 * @author Nicoyarce
 */
public class EjecutarProblema {

    public static void main(String[] args) {
        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")  //algoritmo a utilzar
                .withProblemClass(Funcion.class)  //clase donde esta el problema
                .withMaxEvaluations(10000)    //cantidad de evaluaciones
                .run();

        for (Solution solution : result) {
            System.out.printf("%.5f => %.5f - %.5f => %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),
                    solution.getObjective(0),
                    EncodingUtils.getReal(solution.getVariable(1)),
                    solution.getObjective(0));
        }
        //Para graficar frontera de pareto
        /*new Plot()
                .add("NSGAII",result)
                .show();*/

    }
}
