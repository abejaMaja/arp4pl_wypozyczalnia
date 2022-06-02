package pl.sda.arp4.rental.service;

import org.junit.Assert;
import org.junit.Test;
import pl.sda.arp4.rental.model.Samochod;
import pl.sda.arp4.rental.model.SkrzyniaBiegow;
import pl.sda.arp4.rental.model.StatusSamochodu;
import pl.sda.arp4.rental.model.TypNadwozia;

import java.util.List;

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
    }

    // penicola
    @Test
    public void test_uzytkownikNieZepsujeMetodyZmianyStatusuNaNiedostepnyPrzekazujacNieistniejacySamochod() {

    }
}
