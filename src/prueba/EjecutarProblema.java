package prueba;

import org.moeaframework.Executor;
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
                .withAlgorithm("NSGAII")
                .withProblemClass(Funcion.class)        
                .withMaxEvaluations(1000000000)
                .run();

        for (Solution solution : result) {
            System.out.printf("%.5f => %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),
                    solution.getObjective(0));
        }

    }
}
