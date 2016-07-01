package controllers;

import models.Recipient;
import play.jobs.Job;
import play.jobs.On;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Fire at 10:00 AM Everyday
@On("0 0 10 * * ?")
public class EveryDayJobTest extends Job<File> {

    @Override
    public void doJob() throws Exception {
        System.out.println("todayJob");
        List<Recipient> recipients = new ArrayList<Recipient>();
        Recipient r = new Recipient();
        r.email = "ufrn.eduardo@gmail.com";
        recipients.add(r);
        r = new Recipient();
        r.email = "eduardo.medeiros@ufersa.edu.br";
        recipients.add(r);
        new FormPDFReportJob().setRecipients(recipients).now();
    }
}

