package moduloIDF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Nicoyarce
 */
public class CargaIDF {

    public static String rutaIDF = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\caso_base_agosto.idf";
    public static int nVariables;
    public static int nObjetivos;
    public ArrayList<String> salidaAbrirIDF;
    public ArrayList<ValoresEnergeticos> salidaExtraccionDatos;

    public CargaIDF() throws IOException {
        abrirIDF();
        nVariables = salidaAbrirIDF.size();
        nObjetivos = 1; //definido por usuarios?
    }

    /*public static void main(String[] args) throws IOException {
        CargaIDF test = new CargaIDF();       
        for (ValoresEnergeticos valoresEnergeticos : test.extraerDatosReporte()) {
            System.out.println(valoresEnergeticos.getTitulo());
            System.out.println(valoresEnergeticos.getValor());
        }     
    }*/
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
            //System.out.println("Here is the standard error of the command (if any):\n");
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
        int cantidadDatos = 5; //numero de valores de demanda energetica
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript + " " + rutaIDF);
            System.out.println("Extrayendo datos energeticos");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            //la salida del script primero imprime titulos y luego valores por eso el codigo esta asi
            while ((s = stdInput.readLine()) != null) {
                if (linea < cantidadDatos) {
                    salidaExtraccionDatos.add(new ValoresEnergeticos(s, 0.0));
                } else {
                    //se calcula la linea a la que le corresponde el valor con su respectivo titulo
                    salidaExtraccionDatos.get(linea - cantidadDatos).setValor(s);
                }
                linea++;
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
