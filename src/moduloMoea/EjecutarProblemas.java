package moduloMoea;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

/**
 *
 * @author Nicoyarce
 */

public class EjecutarProblemas {

    public static void main(String[] args) {
        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII") //algoritmo a utilzar
                /*reemplazar nombre de clase*/
                .withProblemClass(FuncionPrueba.class) //clase donde esta el problema
                .withMaxEvaluations(5) //cantidad de evaluaciones
                .withProperty("populationSize", 5) //numero poblacion, por defecto 100 en NSGAII
                //.withInstrumenter(instrumenter)
                .distributeOnAllCores()
                .run();

        /*Resultados optimizacion por salida estandar*/
        int opcion=0;
        for (Solution solution : result) {
            opcion++;
            System.out.println("Opcion "+opcion);
            System.out.println("Objetivos: ");
            for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
                System.out.printf("%.5f\n", solution.getObjective(i));
            }
            System.out.println("Variables: ");
            for (int i = 0; i < solution.getNumberOfVariables(); i++) {
                System.out.printf("%.5f\n",
                        EncodingUtils.getReal(solution.getVariable(i)));
            }
            System.out.println("------");
        }

        //Para graficar frontera de pareto (solo con mas de 1 objetivo)
        new Plot()
                .add("NSGAII", result)
                .show();

    }
}
