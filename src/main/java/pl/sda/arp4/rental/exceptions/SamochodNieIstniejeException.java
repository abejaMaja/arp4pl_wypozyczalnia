package pl.sda.arp4.rental.exceptions;

// Wybrałem RuntimeException bo nie powinno dojść do sytuacji z rzuceniem wyjątku
//  jednak może się okazać że ktoś popełnił błąd
public class SamochodNieIstniejeException extends RuntimeException {
    public SamochodNieIstniejeException(String wiadomoscKtoraSieWyswietli) {
        super(wiadomoscKtoraSieWyswietli);
    }
}
