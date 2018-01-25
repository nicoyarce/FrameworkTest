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
                //reemplazar nombre de clase
                .withProblemClass(FuncionPrueba.class) //clase donde esta el problema
                .withMaxEvaluations(5) //cantidad de evaluaciones
                .withProperty("populationSize", 5) //numero poblacion, por defecto 100 en NSGAII
                .run();

        /*para 1 variable con 1 objetivo*/
        for (Solution solution : result) {
            System.out.printf("%.5f => %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),
                    solution.getObjective(0));
        }
        /*para 1 variable con 5 objetivo*/
 /*for (Solution solution : result) {
            System.out.printf("%.5f => %.5f\n - %.5f\n - %.5f\n - %.5f\n - %.5f\n ",
                    EncodingUtils.getReal(solution.getVariable(0)),
                    solution.getObjective(0),
                    solution.getObjective(1),
                    solution.getObjective(2),
                    solution.getObjective(3),
                    solution.getObjective(4));
        }*/

 /*para 2 variables con 1 objetivo*/
 /*for (Solution solution : result) {
            System.out.printf("%.5f / %.5f => %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),                    
                    EncodingUtils.getReal(solution.getVariable(1)),
                    solution.getObjective(0));
        }*/
 /*para 2 variables con 2 objetivos*/
 /*for (Solution solution : result) {
            System.out.printf("x=%.5f / y=%.5f => %.5f - %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),
                    EncodingUtils.getReal(solution.getVariable(1)),
                    solution.getObjective(0),
                    solution.getObjective(1));
        }*/

 /*para 2 variables 1 objetivo y con restricciones */
 /*for (Solution solution : result) {
            //se comprueba el cumplimiento de restricciones
            if (!solution.violatesConstraints()) {
                System.out.printf("%.5f / %.5f => %.5f\n",
                    EncodingUtils.getReal(solution.getVariable(0)),                    
                    EncodingUtils.getReal(solution.getVariable(1)),
                    solution.getObjective(0));
            }
        }*/
        //Para graficar frontera de pareto (solo con mas de 1 objetivo)
        new Plot()
                .add("NSGAII", result)
                .show();

    }
}
