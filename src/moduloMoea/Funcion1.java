package moduloMoea;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Nicoyarce
 */
//Problema con dos variables y con dos objetivos
public class Funcion1 extends AbstractProblem {
    int constructor=0;
    int evaluate=0;
    int newSolution=0;
    public Funcion1() {
        super(2, 2); //n variables totales , n de variables objetivo
        constructor++;
        System.out.println("Constructor"+constructor);
        
    }

    @Override
    public void evaluate(Solution sltn) {       
        
        /* numerar variables de todo el problema */
        double x = EncodingUtils.getReal(sltn.getVariable(0));
        double y = EncodingUtils.getReal(sltn.getVariable(1));
        
        //funciones a optimizar
        double f1 = 4*Math.pow(x,2) + 4*Math.pow(y,2);
        double f2 = Math.pow((x-5),2) + Math.pow((y-5),2);
        
        /*numerar funciones objetivo de todo el problema*/        
        sltn.setObjective(0, f1);  //indice de fx objetivo , fx a optimizar
        sltn.setObjective(1, f2);
        evaluate++;
        System.out.println("Evaluate"+ evaluate);
        
    }

    @Override
    public Solution newSolution() {
        
        Solution solution = new Solution (getNumberOfVariables(),getNumberOfObjectives()); //n variables totales , n de variables objetivo
        
        // se fija dominio de busqueda por cada variable
        solution.setVariable(0, EncodingUtils.newReal(0,5));  
        solution.setVariable(1, EncodingUtils.newReal(0,3));
        newSolution++;
        System.out.println("newSolution"+newSolution);      
        return solution;        
    }
    
}
