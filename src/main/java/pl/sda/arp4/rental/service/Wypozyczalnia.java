package pl.sda.arp4.rental.service;

import pl.sda.arp4.rental.model.Samochod;
import pl.sda.arp4.rental.model.SkrzyniaBiegow;
import pl.sda.arp4.rental.model.StatusSamochodu;
import pl.sda.arp4.rental.model.TypNadwozia;

import java.util.*;

public class Wypozyczalnia {
    private Map<String, Samochod> pojazdy = new HashMap<>();

    // Możemy dodać samochód
    // Co ta funkcja powinna robić:
    //  - dodawać samochód
    //      - jeśli dodam samochód ( X Y Z ) to ten samochód musi być w wypożyczalni
    //  - dodawać samochody
    //      - jeśli dodam 5 samochodów (różnych) to powinno być 5 różnych samochodów w wypożyczalni
    //
    // Czego funkcja nie powinna pozwalać:
    //  - dodawania pojazdu o istniejącym numerze rejestracyjnym
    //
    // Zasada z Test Driven Development - zasada dobrego testera:
    //
    // Treść testu powinna mówić jakie są warunki działania/zakres funkcji.
    // Test mówi jak funkcja powinna się zachowywać.
    public void dodajSamochod(String numerRejestracyjny,
                              SkrzyniaBiegow skrzyniaBiegow,
                              TypNadwozia typNadwozia,
                              StatusSamochodu statusSamochodu) {
        if (!pojazdy.containsKey(numerRejestracyjny)) {
            pojazdy.put(numerRejestracyjny,
                    new Samochod(numerRejestracyjny, skrzyniaBiegow, typNadwozia, statusSamochodu));
            // udało się dodać samochód
        }
    }

    public List<Samochod> zwrocListe() {
        return new ArrayList<>(pojazdy.values());
    }

    public List<Samochod> zwrocListeDostepnych() {
        List<Samochod> listaDostepnych = new ArrayList<>();
        for (Samochod pojazd : pojazdy.values()) {
            if (pojazd.getStatus() == StatusSamochodu.DOSTEPNY) {
                listaDostepnych.add(pojazd);
            }
        }

        return listaDostepnych;
    }

    public List<Samochod> zwrocListeWynajetych() {
        List<Samochod> listaWynajetych = new ArrayList<>();
        for (Samochod pojazd : pojazdy.values()) {
            if (pojazd.getStatus() == StatusSamochodu.WYNAJETY) {
                listaWynajetych.add(pojazd);
            }
        }

        return listaWynajetych;
    }

    public void usunSamochod(String numerRejestracyjny) {
        pojazdy.get(numerRejestracyjny).setStatus(StatusSamochodu.NIEDOSTEPNY);
    }
}
