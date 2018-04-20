package moduloEP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Nicoyarce
 */
/*Esta clase se encarga de la comunicacion con la biblioteca eppy, la que esta
escrita en python. Esta comunicacion desde python a java se hace por salida
estandar, por otro lado la comunicacion desde java a python se hace a traves de
la entrega de parametros al momento de ejecutar el comando python*/
public class CargaEP {

    private String rutaIDD = "";
    private String rutaIDF = "";
    private String rutaEPW = "";
    private String rutaMateriales = "";
    //variables estaticas por constructor del framework moea
    public static int nVariables;
    public static int nObjetivos;
    public ArrayList<Material> listaMateriales = new ArrayList<>();
    public ArrayList<ValorEnergetico> listaValoresEnergeticos = new ArrayList<>();
    public String comando = "py";

    public CargaEP(String rutaIDD, String rutaIDF, String rutaEPW, String rutaMateriales, ArrayList<Boolean> objetivos, ArrayList<Boolean> eleccionCaracteristicas) throws IOException {
        ArrayList<String> titulosEnergeticos = new ArrayList<>(Arrays.asList(
                "[Total Energy [kWh]/Total Site Energy]",
                "[Total Energy [kWh]/Total Source Energy]",
                "[District Heating [W]/Heating]",
                "[District Cooling [W]/Cooling]",
                "[Facility [Hours]/Time Setpoint Not Met During Occupied Heating]",
                "[District Heating Intensity [kWh/m2]/HVAC]"));
        int i = 0;
        for (String titulo : titulosEnergeticos) {
            listaValoresEnergeticos.add(i, new ValorEnergetico(titulo, "-1", objetivos.get(i)));
            i++;
        }
        this.rutaIDD = rutaIDD;
        this.rutaIDF = rutaIDF;
        this.rutaEPW = rutaEPW;
        this.rutaMateriales = rutaMateriales;
        abrirIDF(eleccionCaracteristicas);
        nVariables = 0;
        for (int j = 0; j < listaMateriales.size(); j++) {
            for (int k = 0; k < listaMateriales.get(j).getCaracteristicas().size(); k++) {
                if (listaMateriales.get(j).getCaracteristicas(k).isSeleccionado()) {
                    nVariables++;
                }
            }
        }

        nObjetivos = 0;
        for (ValorEnergetico objetivo : listaValoresEnergeticos) {
            if (objetivo.isSeleccionado()) {
                nObjetivos++;
            }
        }
    }

    /*Abre archivo idf y captura salida estandar del script python que muestra
    las variables a optimizar*/
    private void abrirIDF(ArrayList<Boolean> eleccionCaracteristicas) {
        String s = null;
        listaMateriales = new ArrayList<>();
        String rutaScript = "lib/python/abrirIDF.py";

        try {
            String cmd = comando + " " + rutaScript + " " + rutaIDD + " " + rutaIDF
                    + " " + rutaMateriales;
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("Abriendo IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String nombre = "", roughness = "";
            String[] linea;
            ArrayList<CaracteristicaMaterial> temporal = new ArrayList<>();
            int nCaracteristica = 0;
            while ((s = stdInput.readLine()) != null) {
                if (s.startsWith("N:")) {
                    nombre = s.substring(2);
                } else if (s.startsWith("R:")) {
                    roughness = s.substring(2);
                } else if (s.contains("---")) {
                    listaMateriales.add(new Material(nombre, roughness, new ArrayList(temporal)));
                    nombre = "";
                    roughness = "";
                    temporal.clear();
                    nCaracteristica = 0;
                } else {
                    //linea[] = nombre, valor, rangomin, rangomax, rangominEstricto, rangoMaxEstricto
                    linea = s.split("%");
                    double numero, delta, rangoMin, rangoMax;
                    numero = Double.parseDouble(linea[1]);
                    delta = numero * 1.5;
                    delta = truncar(delta);

                    if (!linea[2].equalsIgnoreCase("None")) {
                        rangoMin = Double.parseDouble(linea[2]);
                    } else if (!linea[4].equalsIgnoreCase("None")) {
                        rangoMin = Double.parseDouble(linea[4]);
                    } else {
                        rangoMin = delta - numero;
                    }

                    if (!linea[3].equalsIgnoreCase("None")) {
                        rangoMax = Double.parseDouble(linea[3]);
                    } else if (!linea[5].equalsIgnoreCase("None")) {
                        rangoMax = Double.parseDouble(linea[5]);
                    } else {
                        rangoMax = delta + numero;
                    }
                    temporal.add(new CaracteristicaMaterial(linea[0], numero, rangoMin, rangoMax, eleccionCaracteristicas.get(nCaracteristica)));
                    nCaracteristica++;
                }
            }
            p.waitFor();

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
                if (s.contains("**FATAL")) {
                    System.exit(-1);
                }
            }
        } catch (IOException | InterruptedException ex) {
            System.err.println("exception happened - here's what I know: " + ex);
            System.exit(-1);
        }
    }

    /*Ejecuta script python que ejecuta la simulacion de energyplus*/
    public void simularIDF() throws IOException {
        String s = null;
        final String rutaScript = "lib/python/simular.py";
        try {
            String cmd = comando + " " + rutaScript + " " + rutaIDD + " " + rutaIDF
                    + " " + rutaEPW;
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("Simulando IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                //System.out.print("Python> ");
                //System.out.println(s);                
            }
            p.waitFor();

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            int errores = 0;
            while ((s = stdError.readLine()) != null) {
                errores++;
                if (errores == 1) {
                    System.err.println("---Advertencia de EnergyPlus---");
                    System.err.println("Revisar archivo de salida eplusout.err para mas informacion");
                }
                System.err.println(s);
            }
        } catch (IOException | InterruptedException ex) {
            System.err.println("exception happened - here's what I know: " + ex);
            System.exit(-1);
        }
    }

    /*Llama al script python encargado de extraer datos de demanda energetica
    y los imprime por la salida estandar*/
    public void extraerDatosReporte() {
        final String rutaScript = "lib/python/lecturas_simulacion.py";
        int i = 0;
        String s = null;
        try {
            String cmd = comando + " " + rutaScript + " " + rutaIDF;
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("Extrayendo datos energeticos");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                listaValoresEnergeticos.get(i).setValor(s);
                i++;
            }
            p.waitFor();
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }
        } catch (IOException | InterruptedException ex) {
            System.err.println("exception happened - here's what I know: " + ex);
            System.exit(-1);
        }
    }

    /*Llama al script python encargado de modificar datos en el archivo idf, en
    este caso son las variables que genero el framework de optimizacion. Estas
    variables son entregadas al script python por parametros*/
    public void modificarIDF(ArrayList<String> modificaciones) {
        String s = null;
        final String rutaScript = "lib/python/modificarIDF.py";
        String valores = "";
        int i = 0;
        for (int j = 0; j < listaMateriales.size(); j++) {
            for (int k = 0; k < listaMateriales.get(j).getCaracteristicas().size(); k++) {
                if (listaMateriales.get(j).getCaracteristicas(k).isSeleccionado()) {
                    valores = valores.concat(modificaciones.get(i) + " ");
                    i++;
                } else {
                    valores = valores.concat(String.valueOf(listaMateriales.get(j).getCaracteristicas(k).getValor() + " "));
                }
            }
        }
        try {
            String cmd = comando + " " + rutaScript + " " + rutaIDD + " " + rutaIDF
                    + " " + rutaMateriales + " " + valores;
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("Modificando IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.print("Python> ");
                System.out.println(s);
            }
            p.waitFor();

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }
        } catch (IOException | InterruptedException ex) {
            System.err.println("exception happened - here's what I know: " + ex);
            System.exit(-1);
        }
    }

    public double truncar(double n) {
        n = (double) Math.round(n * 100) / 100;
        return n;
    }
}
