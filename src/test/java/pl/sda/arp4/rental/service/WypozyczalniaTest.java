package pl.sda.arp4.rental.service;

import org.junit.Assert;
import org.junit.Test;
import pl.sda.arp4.rental.exceptions.SamochodNieIstniejeException;
import pl.sda.arp4.rental.model.Samochod;
import pl.sda.arp4.rental.model.SkrzyniaBiegow;
import pl.sda.arp4.rental.model.StatusSamochodu;
import pl.sda.arp4.rental.model.TypNadwozia;

import java.sql.Array;
import java.util.*;

// Czego funkcja nie powinna pozwalać:
//  - dodawania pojazdu o istniejącym numerze rejestracyjnym
//
// Zasada z Test Driven Development - zasada dobrego testera:
//
// Treść testu powinna mówić jakie są warunki działania/zakres funkcji.
// Test mówi jak funkcja powinna się zachowywać.
public class WypozyczalniaTest {

    @Test
    public void test_mozliweJestDodawanieSamochodu() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());

        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać",
                wynikZwroconaLista.contains(testowanySamochod));
    }

    // nie jest możliwe 'nadpisanie' samochodu
    @Test
    public void test_mozliweJestDodawanieSamochoduAleNieJegoNadpisanie() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test1", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.NIEDOSTEPNY);

        // Testujemy na jednej wypożyczalni
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // dodajemy pierwszy (zadziała poprawnie)
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        // to musi nie zadziałać (nie może dodać, nie może nadpisać)
        wypozyczalnia.dodajSamochod(
                testowanySamochodDrugi.getNumerRejestracyjny(),
                testowanySamochodDrugi.getSkrzynia(),
                testowanySamochodDrugi.getTyp(),
                testowanySamochodDrugi.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo drugi nie powinien być dodany", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać", wynikZwroconaLista.contains(testowanySamochod));
    }

    @Test
    public void test_mozemyPobracListeSamochodowDostepnych() {
        // pobieramy liste ktora zawiera TYLKO DOSTEPNE samochody
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.NIEDOSTEPNY);

        // Testujemy na jednej wypożyczalni
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // dodajemy pierwszy (zadziała poprawnie)
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        // to musi nie zadziałać (nie może dodać, nie może nadpisać)
        wypozyczalnia.dodajSamochod(
                testowanySamochodDrugi.getNumerRejestracyjny(),
                testowanySamochodDrugi.getSkrzynia(),
                testowanySamochodDrugi.getTyp(),
                testowanySamochodDrugi.getStatus());

        List<Samochod> wynikZwroconaListaWszystkichSamochodow = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna zawierać oba pojazdy", 2, wynikZwroconaListaWszystkichSamochodow.size());

        List<Samochod> dostepne = wypozyczalnia.zwrocListeDostepnych();
        Assert.assertEquals("Lista powinna zawierać jeden dostepny pojazd", 1, dostepne.size());
        Assert.assertTrue("Lista powinna zawierać tylko samochod dostepny", dostepne.contains(testowanySamochod));
    }

    @Test
    public void test_mozemyZmienicStatusSamochoduNaNiedostepny() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać", wynikZwroconaLista.contains(testowanySamochod));

        wypozyczalnia.usunSamochod("test1");
        wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.get(0).getStatus() == StatusSamochodu.NIEDOSTEPNY);

        Samochod samochodDoPorownania = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.NIEDOSTEPNY);
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.contains(samochodDoPorownania));


        // TODO: lista dostępnych powinna być rozmiaru 0
        List<Samochod> wynikZwroconaListaWynajetych = wypozyczalnia.zwrocListeDostepnych();
        Assert.assertEquals("Lista powinna zawierać 0 elementów", 0, wynikZwroconaListaWynajetych.size());
    }

    // penicola
    @Test(expected = SamochodNieIstniejeException.class)
    public void test_uzytkownikNieZepsujeMetodyZmianyStatusuNaNiedostepnyPrzekazujacNieistniejacySamochod() {

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.wynajmij("123", "Jan Kowalski", 2);
        List<Samochod> listaWynajetych = wypozyczalnia.zwrocListeWynajetych();

    }

    @Test
    public void test_uzytkownikUsuwajacSamochodZmieniaStatus() {

        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        wypozyczalnia.usunSamochod("test1");
        List lista = wypozyczalnia.zwrocListe();
        boolean check = lista.get(0).toString().endsWith("NIEDOSTEPNY)");
        Assert.assertTrue("Musi być spełniony warunek", check);
    }

    @Test
    public void test_czyDobrzeZwracaneListy() {

        Samochod testowanySamochod1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.WYNAJETY);
        Samochod testowanySamochod2 = new Samochod("test2", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.WYNAJETY);
        Samochod testowanySamochod3 = new Samochod("test3", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.NIEDOSTEPNY);
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod1.getNumerRejestracyjny(),
                testowanySamochod1.getSkrzynia(),
                testowanySamochod1.getTyp(),
                testowanySamochod1.getStatus());

        wypozyczalnia.dodajSamochod(
                testowanySamochod2.getNumerRejestracyjny(),
                testowanySamochod2.getSkrzynia(),
                testowanySamochod2.getTyp(),
                testowanySamochod2.getStatus());

        wypozyczalnia.dodajSamochod(
                testowanySamochod3.getNumerRejestracyjny(),
                testowanySamochod3.getSkrzynia(),
                testowanySamochod3.getTyp(),
                testowanySamochod3.getStatus());

        List pojazdy = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna zawierać 3 elementy bo 3 dodane auta", 3, pojazdy.size());

        List pojazdyWynajete = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna zawierać 2 elementy bo 2 dodane auta wynajęte", 2, pojazdyWynajete.size());

        List pojazdyDostepne = wypozyczalnia.zwrocListeDostepnych();
        Assert.assertEquals("Lista powinna zawierać 0 elementy bo żaden nie jest dostepny", 0, pojazdyDostepne.size());
    }

    @Test
    public void test_czyMoznaWynajacSamochod() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());
        List listaWynajetych = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna zawierać 0 elementy bo żaden nie jest wynajety", 0, listaWynajetych.size());

        wypozyczalnia.wynajmij(testowanySamochod.getNumerRejestracyjny(), "Jan Kowalski", 7);
        List listaWynajetych2 = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna zawierać 1 elementy bo 1  jest wynajety", 1, listaWynajetych2.size());

    }

    @Test
    public void test_łącznyZysk() {
        Samochod testowanySamochod1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochod2 = new Samochod("test2", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        wypozyczalnia.dodajSamochod(
                testowanySamochod1.getNumerRejestracyjny(),
                testowanySamochod1.getSkrzynia(),
                testowanySamochod1.getTyp(),
                testowanySamochod1.getStatus());

        wypozyczalnia.dodajSamochod(
                testowanySamochod2.getNumerRejestracyjny(),
                testowanySamochod2.getSkrzynia(),
                testowanySamochod2.getTyp(),
                testowanySamochod2.getStatus());

        wypozyczalnia.wynajmij("test1", "Jan Kowalski", 1);
        wypozyczalnia.wynajmij("test2", "Jan Kowalski", 2);
        List listaWynajetych = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna zawierać 2 elementy bo 2 auta wynajete", 2, listaWynajetych.size());


        Optional<Double> cena = wypozyczalnia.sprawdzCeneSamochodu("test1", 1);
        Optional<Double> spodziewanaCena = Optional.of(1000.0);
        Assert.assertEquals("Koszt SUVa na jeden dzień 1000",spodziewanaCena , cena);

        double zysk = wypozyczalnia.łącznyZysk();
        double spodziewanyZysk = 3000.0;
        Assert.assertEquals("Koszt SUVa na jeden dzień 1000 + kolejnego SUVa na dwa dni",spodziewanaCena , cena);

    }




}
