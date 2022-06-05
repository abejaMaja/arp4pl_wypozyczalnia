package pl.sda.arp4.rental.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WynajemSamochodu {
    /**
     * Te klasę można zrealizować na wiele sposobów.
     *
     * Do klasy można również dopisać jakieś pole typu "identyfikator wynajmu", ja to zrobiłem, ale jest to
     * opcjonalne.
     */
    private String identyfikator;
    private LocalDateTime dataWynajmu;  // w datach wystarczy LocalDate, ale żeby można się tym było "bawić" ustawiłem LocalDateTime
    private LocalDateTime dataZwrotu;   // domyślnie null, jeśli jest ustawione, oznacza że samochód jest zwrócony
    private String imieINazwiskoKlienta;
    private Samochod wynajetySamochod;

    public WynajemSamochodu(String identyfikator, String imieINazwiskoKlienta, Samochod wynajetySamochod) {
        this.identyfikator = identyfikator;
        this.imieINazwiskoKlienta = imieINazwiskoKlienta;
        this.wynajetySamochod = wynajetySamochod;
        this.dataWynajmu = LocalDateTime.now();
    }
}
