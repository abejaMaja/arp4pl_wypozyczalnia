package pl.sda.arp4.rental;

import pl.sda.arp4.rental.model.Samochod;
import pl.sda.arp4.rental.model.SkrzyniaBiegow;
import pl.sda.arp4.rental.model.StatusSamochodu;
import pl.sda.arp4.rental.model.TypNadwozia;
import pl.sda.arp4.rental.service.Wypozyczalnia;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod("122", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod("124", SkrzyniaBiegow.MANUAL, TypNadwozia.SEDAN, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod("126", SkrzyniaBiegow.MANUAL, TypNadwozia.SEDAN, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod("126", SkrzyniaBiegow.MANUAL, TypNadwozia.SEDAN, StatusSamochodu.WYNAJETY);

        wypozyczalnia.zwrocListe();
        wypozyczalnia.zwrocListeDostepnych();
        wypozyczalnia.zwrocListeWynajetych();
        wypozyczalnia.usunSamochod("122");
        wypozyczalnia.wynajmij("124", "Jan Kowlaski", 7);
        System.out.println(wypozyczalnia.zwrocListeWynajetych());




    }
}
