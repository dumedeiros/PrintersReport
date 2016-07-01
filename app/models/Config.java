package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Config extends Model {

    @Required
    public String externalLink;
    @Required
    public String internalLink;
    @Required
    public String port;


}
