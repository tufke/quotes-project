package nl.kabisa.service.quotes.database.model;

public interface Versioned {

    boolean validateVersion(Long version);
}
