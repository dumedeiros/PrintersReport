package controllers;

import Utils.DateUtils;
import com.google.gson.JsonObject;
import models.Printer;
import notifiers.Mails;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import play.Play;
import play.jobs.Job;
import play.jobs.On;
import play.libs.WS;
import play.modules.pdf.PDF;
import play.mvc.Http;
import play.mvc.Scope;
import play.test.FunctionalTest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Fire at 9:00 AM on the last day of every month
@On("0 0 9 L * ?")
//@On("0/5 * * * * ?") // -> Every 5 seconds Cron notation
public class FormPDFReportJob extends Job<File> {

    public static final String ADDRESS = "http://<<IP>>/sws/app/information/counters/counters.json";
    public static final String IP = "<<IP>>";
    public static final String TEMPLATE = "Application/printer.html";
    public static final String TEMP_FOLDER = "temp.folder";
    public static final String PRINTER = "printer";
    public static final String DATE = "date";
    public static final String DOT_PDF = ".pdf";

    Map<String, Object> pdfArgs;

    /**
     * Retorna o nome da pasta temporaria de arquivos
     */
    public static String tempFolder() {
        return Play.configuration.getProperty(TEMP_FOLDER);
    }

    /**
     * Cria uma requisicao falsa, necessária para utilizar o modulo PDF ( e suas renderizacao de template)
     * Uma classe Job normal não possuem objetos de escopo, como Request, Response, Flash, RenderArgs, etc...
     */
    public static void createFakeRequest() {
        Http.Request req = FunctionalTest.newRequest();
        Http.Request.current.set(req);
    }

    /**
     * Cria o diretorio temporario para armazenar os relatótios
     */

    public static void createTempFolder() {
        new File(tempFolder()).mkdir();
    }

    public void doJob() {

        Printer actualPrinter = null;
        List failures = new ArrayList(); //para armazenar possiveis impressoras que nao obtenha comunicacao
        createFakeRequest();
        createTempFolder();
        List<File> files = new ArrayList<File>();

        for (Printer printer : Printer.<Printer>findAll()) {
            try {
                actualPrinter = printer;
                //Acessa o recurso
                JsonObject obj = WS.url(ADDRESS.replace(IP, printer.ip)).get().getJson().getAsJsonObject();

                //Passos necessários para poder renderizar com o módulo PDF
                Scope.RenderArgs ra = new Scope.RenderArgs();
                pdfArgs = new HashMap<String, Object>();
                pdfArgs.put(PRINTER, obj);
                pdfArgs.put(DATE, DateUtils.todayFormatted());
                ra.data.putAll(pdfArgs);
                Scope.RenderArgs.current.set(ra);

                PDF.Options options = new PDF.Options();
                options.pageSize = IHtmlToPdfTransformer.A4P; //Formato papel A4

                StringBuilder filePath = new StringBuilder()
                        .append(tempFolder())
                        .append(printer.name)
                        .append(DOT_PDF);

                File file = new File(filePath.toString());
                files.add(file);
                //Renderiza o TEMPLATE (.html) para o arquivo em pdf enviando os argumentos
                PDF.writePDF(file, TEMPLATE, options, pdfArgs);
            } catch (Exception e) {
                failures.add(actualPrinter); //caso tenha falha de comunicacao adicionar na lista para ser enviado por email
            }
        }
        Mails.sendEmail(files, failures);
    }
}

