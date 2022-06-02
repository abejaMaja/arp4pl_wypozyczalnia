package pl.sda.arp4.rental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Samochod {
    // dla identyfikacji pojazdu
    private String numerRejestracyjny;

    // dla filtrowania obiektów
    private SkrzyniaBiegow skrzynia;
    private TypNadwozia typ;

    // dla sprawdzenia dostępności
    private StatusSamochodu status;
}
