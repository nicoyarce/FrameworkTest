package moduloMoea;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
//Problema con dos variables y un objetivo con restricciones
public class Funcion2 extends AbstractProblem {    
    public Funcion2() {
        super(2, 1, 2); //n variables totales , n de variables objetivo, n de restricciones
    }

    @Override
    public void evaluate(Solution sltn) {
        /* numerar variables de todo el problema */
        double x = EncodingUtils.getReal(sltn.getVariable(0));
        double y = EncodingUtils.getReal(sltn.getVariable(1));
        
        //funciones a optimizar
        double f1 = Math.pow((1-x),2) + 100*Math.pow( y - Math.pow(x,2), 2);
        
        //restricciones
        double c1 = Math.pow((x-1),3) - y + 1;
        double c2 = x + y - 2;
        
        /*numerar funciones objetivo de todo el problema*/        
        sltn.setObjective(0, f1);  //indice de fx objetivo , fx a optimizar  
        
        /*numerar restricciones de todo el problema*/
        sltn.setConstraint(0, c1 < 0 ? 0 : c1);
        sltn.setConstraint(1, c1 < 0 ? 0 : c2);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution (getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints()); //n variables totales , n de variables objetivo, n de restricciones
        
        // se fija dominio de busqueda por cada variable
        solution.setVariable(0, EncodingUtils.newReal(-1.5, 1.5));  
        solution.setVariable(1, EncodingUtils.newReal(-0.5, 2.5));
        return solution;
    }
    
}
