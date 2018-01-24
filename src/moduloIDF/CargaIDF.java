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

    public static String rutaIDF = "";
    
    /*public static void main(String[] args) {
        CargaIDF test = new CargaIDF();
    }*/

    public ArrayList<String> abrirIDF() throws IOException {
        String s = null;
        ArrayList<String> salida = new ArrayList<>();
        String rutaScript = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\idf.py";
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");            
            while ((s = stdInput.readLine()) != null) {
                salida.add(s);
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
        return salida;
    }

    public void simularIDF() throws IOException {
        String s = null;
        String rutaScript = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\simular.py";
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript);
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

    public ArrayList<ArrayList> extraerDatosReporte() {
        ArrayList<ArrayList> salida = new ArrayList<>();
        ArrayList<String> titulos = new ArrayList<>();
        ArrayList<String> valoresEnergeticos = new ArrayList<>();
        String rutaScript = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\lecturas_simulacion.py";
        int linea = 0;
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                if (linea < 5) {
                    titulos.add(s);
                } else {
                    valoresEnergeticos.add(s);
                }
                //System.out.print("Python> ");
                //System.out.println(s);
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
        salida.add(titulos);
        salida.add(valoresEnergeticos);        
        return salida;
    }

    public void modificarIDF(ArrayList<String> modificaciones) {
        String s = null;
        String rutaScript = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\modificarIDF.py";
        try {
            Process p = Runtime.getRuntime().exec("python " + rutaScript);
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
