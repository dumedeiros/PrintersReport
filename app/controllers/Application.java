package controllers;

import annotations.EmailList;
import annotations.EmailListCheck;
import com.google.gson.JsonObject;
import models.Printer;
import models.Recipient;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.WS;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application extends Controller {

    public static final String ADDRESS = "http://<<IP>>/sws/app/information/counters/counters.json";
    public static final String IP = "<<IP>>";
    public static final String ERROR = "Sem acesso";


    public static void doManually(@Required(message = "Nenhuma impressora foi selecionada") List<String> ips,
                                  @Required @EmailList(message = "Email(s) Inválido(s)") String recipients) {


        if (Validation.hasErrors()) {
            validation.keep();
            params.flash();
            manually();
        }

        List<Printer> printers = new ArrayList<Printer>();
        List<Recipient> recipientsList = new ArrayList<Recipient>();

        for (String ip : ips) {
            printers.add(Printer.finByIP(ip));
        }

        List<String> recFormated = Arrays.asList(recipients.trim().split(EmailListCheck.SPLIT_PATTERN));
        for (String email : recFormated) {
            Recipient r = new Recipient();
            r.email = email;
            recipientsList.add(r);
        }

        new FormPDFReportJob().setPrinters(printers).setRecipients(recipientsList).now();

        flash.success("Relatório de impressoras gerado com sucesso.");

        index();
    }

    public static void manually() {
        List<Printer> printers = Printer.findAll();
        //QuickLoad
//        for (Printer p : printers) {
//            Validation.addError(p.ip, ERROR);
//        }
        for (Printer printer : printers) {
            try {
                JsonObject obj = WS.url(ADDRESS.replace(IP, printer.ip)).get().getJson().getAsJsonObject();
            } catch (Exception e) {
                Validation.addError(printer.ip, ERROR);
            }

        }

        render(printers);

    }

    public static void index() {

        render();
    }


}