package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Printer extends Model {

    @Required
    public String ip;
    @Required
    public String name;

    @Override
    public String toString() {
        return name;
    }

    public static Printer finByIP(String ip) {
        return Printer.find("ip", ip).first();
    }
}
