package controllers;

import models.Printer;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

    /**
     * Usado para carregar os dados inicias
     * Utilizado uma unica vez que é no primeiro deploy para carregar os dados iniciais no banco de dados
     * -> Utilizar tb para resetar a aplicação para o estado inicial
     */
    @Override
    public void doJob() throws Exception {
        if (Printer.count() == 0) {
            Fixtures.deleteAllModels();
            Fixtures.loadModels("data.yml");
        }

    }
}
