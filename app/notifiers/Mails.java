
package notifiers;

import models.Admin;
import models.Config;
import models.Printer;
import models.Recipient;
import org.apache.commons.mail.EmailAttachment;
import play.Play;
import play.mvc.Mailer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Mails extends Mailer {

    private static final String MAILER_FROM = "mailer.from";
    public static final String SUBJECT = "Relatório mensal das impressoras";
    public static final String FORMAT = "MMMMM/yyyy";


    public static void sendEmail(List<File> filesToAttach, List<Printer> failures, List<Recipient> recipients) {
        Admin admin = Admin.all().first();

        LocalDate date = LocalDate.now();


        setSubject(SUBJECT.concat(" (").concat(new SimpleDateFormat(FORMAT, Locale.getDefault()).format(new Date()).concat(")")));

        setFrom(Play.configuration.getProperty(MAILER_FROM));


        for (Recipient recipient : recipients) {
            addRecipient(recipient.email);
        }

        setReplyTo(admin.email);
        addCc(admin.email);

        for (File file : filesToAttach) {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(file.getPath());
            addAttachment(attachment);
        }

        Config config = Config.all().first();

        //Enviado para o template para no caso de falhas o destinatario ter acesso ao email
        send(failures, admin, config);
    }

}
