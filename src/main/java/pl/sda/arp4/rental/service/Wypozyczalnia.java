package pl.sda.arp4.rental.service;

import pl.sda.arp4.rental.exceptions.SamochodNieIstniejeException;
import pl.sda.arp4.rental.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class Wypozyczalnia {
    private static Integer LICZBIK_WYNAJMOW = 1;

    private Map<String, Samochod> pojazdy = new HashMap<>();
    private Map<String, WynajemSamochodu> wynajmy = new HashMap<>();

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

    // tej metody nie ma w poleceniu ale będzie wykorzystywana tylko z wnętrza klasy (stąd ma modyfikator dostępu private)
    // jej istnienie jest 100% opcjonalne - jest przykładem zastosowania metod prywatnych
    private Optional<Samochod> znajdzSamochod(String rejestracja) {
        return Optional.ofNullable(pojazdy.get(rejestracja));
    }

    // Zapytania z perspektywy klienta
    // TODO: wyświetlić listę samochodów - to polecenie powinno być zrealizowane następująco
    //  - implementujemy parser
    //  - w parser użytkownik wpisuje komende ktora odpowiada za wyswietlanie listy dostepnych samochodów
    //  - lista jest zwracana z użyciem metody: zwrocListeDostepnych() i w pętli (lub nie) wyświetlana w parserze
    public Optional<Double> sprawdzCeneSamochodu(String rejestracja, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            double cenaZaIloscDni = samochod.getTyp().getCenaBazowa() * liczbaDni;
            return Optional.of(cenaZaIloscDni);
        }

        return Optional.empty();
    }

    public void wynajmij(String rejestracja, String imieINazwiskoKlienta, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            samochod.setStatus(StatusSamochodu.WYNAJETY);

            String generowanyIdentyfikator = "WYNAJEM-" + LICZBIK_WYNAJMOW;
            wynajmy.put(generowanyIdentyfikator,
                    new WynajemSamochodu(
                            generowanyIdentyfikator,
                            imieINazwiskoKlienta,
                            samochod));
            return;
        }

        // Note: rzucamy exception ponieważ nikt nie prosi nas o wynik (nie ma typu zwracanego)
        //  ale chcemy zwrócić uwagę że metoda się "nie udała"
        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public void zwrocSamochod(String rejestracja, String identyfikatorWynajmu) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            if (samochod.getStatus() != StatusSamochodu.WYNAJETY) {
                throw new SamochodNieIstniejeException("Nie można zwrócić samochodu który nie jest wynajety");
            }
            samochod.setStatus(StatusSamochodu.DOSTEPNY);
            WynajemSamochodu wynajemSamochodu = wynajmy.get(identyfikatorWynajmu);
            wynajemSamochodu.setDataZwrotu(LocalDateTime.now());
            return;
        }

        // Note: rzucamy exception ponieważ nikt nie prosi nas o wynik (nie ma typu zwracanego)
        //  ale chcemy zwrócić uwagę że metoda się "nie udała"
        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public List<WynajemSamochodu> listaAktywnychWynajmów() {
        List<WynajemSamochodu> wynajmyAktywne = new ArrayList<>();
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() == null) {
                wynajmyAktywne.add(wynajem);
            }
        }
        return wynajmyAktywne;
    }

    public List<WynajemSamochodu> listaZakonczonychWynajmów() {
        List<WynajemSamochodu> wynajmyAktywne = new ArrayList<>();
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() != null) {
                wynajmyAktywne.add(wynajem);
            }
        }
        return wynajmyAktywne;
    }

    public double łącznyZysk() {
        double zysk = 0.0;
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() != null) {

                // tutaj pierwsza wersja jest w "dniach"
                // obliczanie ile minęło dni (tutaj zawsze będzie 0, chyba że zostawicie aplikacje na 1 dzień i będą wynajmy) :)
//                Period period = Period.between(wynajem.getDataWynajmu().toLocalDate(), wynajem.getDataZwrotu().toLocalDate());
//                zysk += period.getDays() * wynajem.getWynajetySamochod().getTyp().getCenaBazowa();

                // Druga wersja jest w minutach
                Duration duration = Duration.between(wynajem.getDataWynajmu(), wynajem.getDataZwrotu());
                zysk += (duration.getSeconds()/60) * wynajem.getWynajetySamochod().getTyp().getCenaBazowa();
            }
        }
        return zysk;
    }
}
