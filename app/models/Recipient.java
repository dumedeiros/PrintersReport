package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Entity
public class Recipient extends User {
}
