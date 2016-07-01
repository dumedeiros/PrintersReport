package annotations;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import play.data.validation.EmailCheck;

public class EmailListCheck extends AbstractAnnotationCheck<EmailList> {

    /**
     * Error Message Key
     **/

    public final static String message = "validation.emailList";
    public final static String SPLIT_PATTERN = "\\s*(;|,)\\s*";

    @Override
    public void configure(EmailList list) {
        setMessage(list.message());
    }

    @Override
    public boolean isSatisfied(Object validatedObject, Object value, OValContext context, Validator validator) throws OValException {
        String[] emails = value.toString().trim().split(SPLIT_PATTERN);
        for (String email : emails) {
            EmailCheck emailCheck = new EmailCheck();
            if (!emailCheck.isSatisfied(validatedObject, email, context, validator)) {
                return false;
            }
        }
        return true;
    }


}
