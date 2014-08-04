import org.apache.commons.io.FileUtils
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.FileTemplateResolver

import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat

public class Main {

    static String site = "site"
    static String theme = "theme"



    public processTheme(File root){
        // iterate over the theme folders
        // process the html and copy all other resources

        root.listFiles().each{ file ->
            if ( ((File)file).isDirectory()){
                println "found directory " + file.getName() + " desending"
                processTheme(file)
            } else if ( ((File)file).getName().endsWith(".html")){
                processTemplate(file)
            } else {
                copyResourceFile(file)
            }
        }

    }


    public void processTemplate(File file){
        println "processing template file " + file.getName()
        FileTemplateResolver resolver = new org.thymeleaf.templateresolver.FileTemplateResolver()
        resolver.setPrefix("theme/")
        resolver.setTemplateMode("XHTML")
        resolver.setSuffix(".html")


        TemplateEngine engine = new TemplateEngine()
        engine.setTemplateResolver(resolver)
        File outFile = new File("site/index.html");
		outFile.getParentFile().mkdirs();
        java.io.FileWriter writer = new java.io.FileWriter(outFile)

        Context context = new Context();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
        context.setVariable("today", dateFormat.format(cal.getTime()));
        engine.process("index", context, writer);
        writer.flush()
    }

    public void copyResourceFile(File file){
        println "copy resource .. got path " + file.getPath()
        String [] paths = file.getPath().split("theme")
        println "got path length " + paths.size()
        File dest = new File("site" + paths[1])
        FileUtils.copyFile(file, dest)
    }

    public static void main(String [] args){
        Main m = new Main()
        m.processTheme(new File ("theme/"))

    }

}
