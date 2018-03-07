package moduloMoea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import moduloEP.CargaEP;
import org.moeaframework.Executor;
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

        for (String arg : args) {
            System.out.println("Argumentos " + arg);
        }
        EjecutarProblemas test = new EjecutarProblemas();
        Scanner entrada = new Scanner(System.in);
        String respuesta = "";
        ArrayList<String> titulosEnergeticos = new ArrayList<>(Arrays.asList(
                "[Total Energy [kWh]/Total Site Energy]",
                "[Total Energy [kWh]/Total Source Energy]",
                "[District Heating [W]/Heating]",
                "[District Cooling [W]/Cooling]",
                "[Facility [Hours]/Time Setpoint Not Met During Occupied Heating]",
                "[District Heating Intensity [kWh/m2]/HVAC]"));
        /*ArrayList<String> caracteristicas = new ArrayList<>(Arrays.asList(
                "Thickness",
                "Conductivity",
                "Density",
                "Specific Heat",
                "Thermal Absorptance",
                "Solar Absorptance",
                "Visible Absorptance"));*/
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
            /*for (String caracteristica : caracteristicas) {
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
            }*/
            eleccionCaracteristicas.add(0, true);
            System.out.println("Se optimizaran valores de Thickness");
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
                .withProperty("populationSize", 5) //numero poblacion, por defecto 100 en NSGAII
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
                variablesFinales.add(solution.getObjective(i));
            }
            int j = 0;
            for (int i = 0; i < cargaIDF.salidaExtraccionDatos.size(); i++) {
                if (cargaIDF.salidaExtraccionDatos.get(i).isSeleccionado()) {
                    cargaIDF.salidaExtraccionDatos.get(i).setSeleccion(false);
                    System.out.print(cargaIDF.salidaExtraccionDatos.get(i).getTitulo() + ": ");
                    System.out.println(variablesFinales.get(j));
                    j++;
                }
            }
            System.out.println("---Variables---");
            for (int i = 0; i < solution.getNumberOfVariables(); i++) {
                //System.out.printf("%.2f\n", EncodingUtils.getReal(solution.getVariable(i)));
                double var = cargaIDF.truncar(EncodingUtils.getReal(solution.getVariable(i)));
                objetivosFinales.add(var);
            }
            j = 0;
            for (int i = 0; i < cargaIDF.salidaAbrirIDF.size(); i++) {
                if (cargaIDF.salidaAbrirIDF.get(i).isSeleccionado()) {
                    cargaIDF.salidaAbrirIDF.get(i).setSeleccionado(false);
                    System.out.print(cargaIDF.salidaAbrirIDF.get(i).getCaracteristica() + ": ");
                    System.out.println(objetivosFinales.get(j));
                    j++;
                }
            }
            System.out.println("---------------");
        }

        //Para graficar frontera de pareto (solo con mas de 1 objetivo)
        /*new Plot()
                .add("NSGAII", result)
                .show();*/
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
}
