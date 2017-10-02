package prueba;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
public class Funcion extends AbstractProblem {

    public Funcion() {
        super(1, 1); //variable , objetivo
    }

    @Override
    public void evaluate(Solution sltn) {
        double x = EncodingUtils.getReal(sltn.getVariable(0));
        sltn.setObjective(0, Math.pow(x,2.0));
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution (1,1);
        solution.setVariable(0, EncodingUtils.newReal(-1000.0,1000.0));
        return solution;
    }
    
}
