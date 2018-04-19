package moduloMoea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import moduloEP.CargaEP;
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

    public Executor exec;
    public CargaEP cargaIDF;
    //public static boolean fin = false;

    public static void main(String[] args) {
        EjecutarProblemas test = new EjecutarProblemas();
        if (!test.comprobarPython()) {
            System.err.println("No se encuentra el comando Python en su línea de comandos");            
        }
        for (String arg : args) {
            System.out.println("Ruta elegida" + arg);
        }

        Scanner entrada = new Scanner(System.in);
        String respuesta = "";
        ArrayList<String> titulosEnergeticos = new ArrayList<>(Arrays.asList(
                "[Total Energy [kWh]/Total Site Energy]",
                "[Total Energy [kWh]/Total Source Energy]",
                "[District Heating [W]/Heating]",
                "[District Cooling [W]/Cooling]",
                "[Facility [Hours]/Time Setpoint Not Met During Occupied Heating]",
                "[District Heating Intensity [kWh/m2]/HVAC]"));
        ArrayList<String> caracteristicas = new ArrayList<>(Arrays.asList(
                "Thickness",
                "Conductivity",
                "Density",
                "Specific Heat",
                "Thermal Absorptance",
                "Solar Absorptance",
                "Visible Absorptance"));
        ArrayList<Boolean> objetivos = new ArrayList<>();
        ArrayList<Boolean> eleccionCaracteristicas = new ArrayList<>();
        if (args.length > 3) {
            for (String titulo : titulosEnergeticos) {
                System.out.println("Incluir objetivo: " + titulo + " (S/N)");
                do {
                    respuesta = entrada.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {
                        objetivos.add(true);
                    } else if (respuesta.equalsIgnoreCase("n")) {
                        objetivos.add(false);
                    } else {
                        System.out.println("Respuesta no valida");
                        System.out.println("Incluir objetivo: " + titulo + " (S/N)");
                    }
                } while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n"));
            }
            for (String caracteristica : caracteristicas) {
                System.out.println("Optimizar " + caracteristica + " (S/N)");
                do {
                    respuesta = entrada.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {
                        eleccionCaracteristicas.add(true);
                    } else if (respuesta.equalsIgnoreCase("n")) {
                        eleccionCaracteristicas.add(false);
                    } else {
                        System.out.println("Respuesta no valida");
                        System.out.println("Optimizar " + caracteristica + " (S/N)");
                    }
                } while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n"));
            }
            entrada.close();
            if (objetivos.contains(true)) {
                test.ejecutarOptimizacion(args[0], args[1], args[2], args[3], objetivos, eleccionCaracteristicas);
            } else {
                System.out.println("No se eligieron objetivos");
                System.exit(0);
            }
        } else {
            System.out.println("Debe ejecutarse como: ");
            System.out.println("EjecutarProblemas <rutaIDD> <rutaIDF> <rutaEPW> <rutaMateriales>");
        }
    }

    public void ejecutarOptimizacion(String rutaIDD, String rutaIDF, String rutaEPW, String rutaMateriales, ArrayList<Boolean> objetivos, ArrayList<Boolean> eleccionCaracteristicas) {
        try {
            cargaIDF = new CargaEP(rutaIDD, rutaIDF, rutaEPW, rutaMateriales, objetivos, eleccionCaracteristicas);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        exec = new Executor();
        NondominatedPopulation result = exec
                .withAlgorithm("NSGAII") //algoritmo a utilzar
                /*reemplazar nombre de clase*/
                .withProblemClass(FuncionOptimizar.class, cargaIDF) //clase donde esta el problema
                .withMaxEvaluations(5) //cantidad de evaluaciones
                .withProperty("populationSize", 20) //numero poblacion, por defecto 100 en NSGAII
                //.withInstrumenter(instrumenter)
                .distributeOnAllCores()
                .run();
        /*if (fin==true)    
            return;*/
 /*Resultados optimizacion por salida estandar*/
        int opcion = 0;
        ArrayList<Double> variablesFinales = new ArrayList();
        ArrayList<Double> objetivosFinales = new ArrayList();
        System.out.println("----------------------------");
        System.out.println("---Optimizacion terminada---");
        System.out.println("----------------------------");
        for (Solution solution : result) {
            opcion++;
            System.out.println("------Opcion " + opcion + "------");
            System.out.println("---Objetivos---");
            for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
                //System.out.printf("%.2f\n", solution.getObjective(i));
                objetivosFinales.add(solution.getObjective(i));
            }
            int j = 0;
            for (int i = 0; i < cargaIDF.listaValoresEnergeticos.size(); i++) {
                if (cargaIDF.listaValoresEnergeticos.get(i).isSeleccionado()) {
                    //cargaIDF.listaValoresEnergeticos.get(i).setSeleccion(false);
                    System.out.print(cargaIDF.listaValoresEnergeticos.get(i).getTitulo() + ": ");
                    System.out.println(objetivosFinales.get(j));
                    j++;
                }
            }
            System.out.println("---Variables---");
            for (int i = 0; i < solution.getNumberOfVariables(); i++) {
                //System.out.printf("%.2f\n", EncodingUtils.getReal(solution.getVariable(i)));
                double var = cargaIDF.truncar(EncodingUtils.getReal(solution.getVariable(i)));
                variablesFinales.add(var);
            }
            j = 0;
            for (int i = 0; i < cargaIDF.listaMateriales.size(); i++) {
                for (int k = 0; k < cargaIDF.listaMateriales.get(i).getCaracteristicas().size(); k++) {
                    if (cargaIDF.listaMateriales.get(i).getCaracteristicas(k).isSeleccionado()) {
                        //cargaIDF.listaMateriales.get(i).getCaracteristicas(k).setSeleccionado(false);                        
                        System.out.print("Material " + cargaIDF.listaMateriales.get(i).getName() + " -> " + cargaIDF.listaMateriales.get(i).getCaracteristicas(k).getCaracteristica() + ": ");
                        System.out.println(variablesFinales.get(j));
                        j++;
                    }
                }
            }
            System.out.println("---------------");
            objetivosFinales.clear();
            variablesFinales.clear();
        }

        //Para graficar frontera de pareto (solo con mas de 1 objetivo)
        new Plot()
                .add("NSGAII", result)
                .show();
    }

    /*public void cancelarOptimizacion() {
        Runnable myRun;
        myRun = new Runnable() {
            @Override
            public void run() {                
                exec.cancel(); 
            }
        };
        Thread t = new Thread(myRun);
        t.start();
        fin = true;
    }*/
    /*Comprueba si python esta disponible a traves de la linea de comandos,
     devuelve verdadero cuando es detectado*/
    public boolean comprobarPython(){
        try {
            String s = null;
            String cmd = "python --version";
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                return s.contains("Python");
            }
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
                return false;
            }
            cmd = "py --version";
            p = Runtime.getRuntime().exec(cmd);            
            while ((s = stdInput.readLine()) != null) {
                return s.contains("Python");
            }
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
                return false;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return false;
    }
}
