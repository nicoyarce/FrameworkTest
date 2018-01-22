package moea;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
//Problema con dos variables y un objetivo
public class Funcion extends AbstractProblem {
    public Funcion() {
        super(2, 1); //n variables totales , n de variables objetivo
    }

    @Override
    public void evaluate(Solution sltn) {
        /* numerar variables de todo el problema */
        double x = EncodingUtils.getReal(sltn.getVariable(0));
        double y = EncodingUtils.getReal(sltn.getVariable(1));
        
        //funcion a optimizar
        double f1 = 0.26*( Math.pow(x,2)+ Math.pow(y,2) ) - 0.48*x*y;
        
        sltn.setObjective(0, f1);  //indice de fx objetivo , fx a optimizar
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution (2,1); //n variables totales , n de variables objetivo
        solution.setVariable(0, EncodingUtils.newReal(5.0,10.0));  // dominio de busqueda por cada variable
        solution.setVariable(1, EncodingUtils.newReal(5.0,10.0));
        return solution;
    }
    
}
