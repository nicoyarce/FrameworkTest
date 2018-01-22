package proyectoTitulo;

import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.model.idf.IDFObject;
import eu.dareed.eplus.parsers.idf.IDFParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import uk.ac.imperial.jeplusplus.JEPlusController;
import uk.ac.imperial.jeplusplus.JEPlusProject;

/**
 *
 * @author Nicoyarce
 */
public class CargaIDF {
    public static CargaIDF apitest = new CargaIDF();;
    public static String rutaIDF = "";
    public static String rutaScript = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\idf.py";
    
    public void EPlusAPI() throws FileNotFoundException, IOException {
        File file = new File(rutaIDF);
        InputStream in = new FileInputStream(file);
        IDF idf = new IDFParser().parseFile(in);
        System.out.println(idf.getObjects().get(1).getFields().get(0));
    }

    public void jEPlusPlus() throws FileNotFoundException, IOException {
        JEPlusController.setJarPath("C:\\Users\\Usuario\\Desktop\\jEPlus_v1.7.0_beta\\jEPlus.jar");
        Path indir = Paths.get(rutaIDF);
        Path outdir = indir.resolve("output");
        JEPlusProject project = new JEPlusProject(indir, outdir);
        project.getProjectFiles();
        project.setFixedParameterValue("@@orientation@@", 7);
        project.run();
        project.scaleResults(2.5);
    }

    public void runJython() throws FileNotFoundException, ScriptException {
        String salidaEntera;
        String[] partes;
        StringWriter writer = new StringWriter(); //ouput will be stored here
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(writer); //configures output redirection
        ScriptEngine engine = manager.getEngineByName("python");
        engine.eval(new FileReader(rutaScript), context);
        salidaEntera = writer.toString();
        partes = salidaEntera.split("\n");
        ArrayList salida = new ArrayList(Arrays.asList(partes));
        for (int i = 0; i < salida.size(); i++) {
            System.out.println("Linea N°" + i + " " + salida.get(i));
        }
    }

    public void runCommand() throws IOException {
        String s = null;
        ArrayList salida = new ArrayList();
        try {
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python " + rutaScript);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                salida.add(s);
                System.out.println(s);
                
            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            System.exit(-1);
        }

    }

}
