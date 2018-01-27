package moduloIDF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Nicoyarce
 */
public class CargaIDF {

    public static String rutaIDF = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\caso_base_agosto.idf";
    //variables estaticas por consturctor del framework moea
    public static int nVariables;
    public static int nObjetivos;
    public ArrayList<Boolean> objetivos;
    public ArrayList<String> salidaAbrirIDF;
    public ArrayList<ValoresEnergeticos> salidaExtraccionDatos;

    public CargaIDF() throws IOException {
        abrirIDF();
        nVariables = salidaAbrirIDF.size();
        //arraylist booleano para indicar cuales seran los objetivos a optimizar
        /*  1 [Total Energy [kWh]/Total Site Energy]
            2 [Total Energy [kWh]/Total Source Energy]
            3 [District Heating [W]/Heating]
            4 [District Cooling [W]/Cooling]
            5 [Facility [Hours]/Time Setpoint Not Met During Occupied Heating]
            6 [District Heating Intensity [kWh/m2]/HVAC]
         */
        objetivos = new ArrayList<>(Arrays.asList(true, false, false, false, false, false));
        nObjetivos = 0;
        for (Boolean opcion : objetivos) {
            if (opcion) {
                nObjetivos++;
            }
        }
    }

    private void abrirIDF() throws IOException {
        String s = null;
        salidaAbrirIDF = new ArrayList<>();
        final String rutaScript = "src/python/abrirIDF.py";
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript + " " + rutaIDF);
            System.out.println("Abriendo IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");            
            while ((s = stdInput.readLine()) != null) {
                salidaAbrirIDF.add(s);
                //System.out.print("Python> ");
                //System.out.println(s);
            }
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            System.exit(-1);
        }
    }

    public void simularIDF() throws IOException {
        String s = null;
        final String rutaScript = "src/python/simular.py";
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript + " " + rutaIDF);
            System.out.println("Simulando IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                //System.out.print("Python> ");
                //System.out.println(s);
            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            System.exit(-1);
        }
    }

    public void extraerDatosReporte() {
        salidaExtraccionDatos = new ArrayList<>();
        final String rutaScript = "src/python/lecturas_simulacion.py";
        int linea = 0;
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript + " " + rutaIDF);
            System.out.println("Extrayendo datos energeticos");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // se fijan nombres de objetivos
            ArrayList<String> titulos = new ArrayList<>(Arrays.asList(
                    "[Total Energy [kWh]/Total Site Energy]",
                    "[Total Energy [kWh]/Total Source Energy]",
                    "[District Heating [W]/Heating]",
                    "[District Cooling [W]/Cooling]",
                    "[Facility [Hours]/Time Setpoint Not Met During Occupied Heating]",
                    "[District Heating Intensity [kWh/m2]/HVAC]"));

            while ((s = stdInput.readLine()) != null) {                
                if(objetivos.get(linea)){
                    salidaExtraccionDatos.add(new ValoresEnergeticos(titulos.get(linea), s));                    
                }
                linea++;               
            }
            for (ValoresEnergeticos dato : salidaExtraccionDatos) {
                System.out.println(dato.getValor());
            }
                
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            System.exit(-1);
        }
    }

    public void modificarIDF(ArrayList<String> modificaciones) {
        String s = null;
        final String rutaScript = "src/python/modificarIDF.py";
        String valores = "";
        for (String modificacion : modificaciones) {
            valores = valores.concat(modificacion + " ");
        }
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript + " " + rutaIDF + " " + valores);
            System.out.println("Modificando IDF");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                //System.out.print("Python> ");
                //System.out.println(s);
            }
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            System.exit(-1);
        }
    }

}
