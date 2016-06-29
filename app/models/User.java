package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class User extends Model {

    @Required
    public String name;
    @Required
    public String email;

    @Override
    public String toString() {
        return name;
    }
}
