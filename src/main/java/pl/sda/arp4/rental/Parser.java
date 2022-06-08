package pl.sda.arp4.rental;

import java.util.*;

import pl.sda.arp4.rental.model.*;
import pl.sda.arp4.rental.service.Wypozyczalnia;

public class Parser {
    private final Wypozyczalnia wypozyczalnia;
    private final Scanner scanner;
    private Samochod samochod;

    public Parser(Wypozyczalnia wypozyczalnia, Scanner scanner ){
        this.wypozyczalnia = wypozyczalnia;
        this.scanner = scanner;
    }

    public void doIt() {
        String komenda;
        do {
            System.out.println("Podaj komendę");
            komenda = scanner.next();

            switch(komenda){
                case "dodaj":
                    obslugaDodaj();
                    break;
                case "zwrocListe":
                    obslugaZwrocListe();
                    break;
                case "zwrocListeDostepnych":
                    obslugaZwrocListeDostepnych();
                    break;
                case "zwrocListeWynajetych":
                    obslugaZwrocListeWynajetych();
                    break;
                case "usunSamochod":
                    obslugaUsunSamochod();
                    break;
                case "sprawdzCeneSamochodu":
                    obslugaSprawdzCeneSamochodu();
                    break;
                case "wynajmij":
                    obslugaWynajmij();
                    break;
                case "zwrocSamochod":
                    obslugaZawrocSamochod();
                    break;
                case "listaAktywnychWynajmów":
                    obslugaZwrocListeAktywnych();
                    break;
                case "listaZakonczonychWynajmów":
                    obslugaZwrocListeZakonczonychWynajmów();;
                    break;
                case "łącznyZysk":
                    System.out.println(wypozyczalnia.łącznyZysk());
                    break;

            }


        } while (!komenda.equalsIgnoreCase("koniec"));
    }

    public void obslugaDodaj(){
        SkrzyniaBiegow skrzyniaBiegow = null;
        TypNadwozia typNadwozia = null;
        StatusSamochodu statusSamochodu = StatusSamochodu.DOSTEPNY;
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        System.out.println("Wybierz " + (Arrays.toString(SkrzyniaBiegow.values())) + ":");
        String skrzyniaBiegowString = scanner.next();
        System.out.println("Wybierz " + (Arrays.toString(TypNadwozia.values())) + ":");
        String typNadwoziaString = scanner.next();

        try {
            skrzyniaBiegow = SkrzyniaBiegow.valueOf(skrzyniaBiegowString.toUpperCase());
            typNadwozia = TypNadwozia.valueOf(typNadwoziaString.toUpperCase());
        } catch (IllegalArgumentException iae) {
            System.out.println("Wybrana przez Ciebie opcja nie istnieje");
        }

        wypozyczalnia.dodajSamochod(numerRejestracyjny, skrzyniaBiegow, typNadwozia, statusSamochodu);

    }

    public void obslugaZwrocListe(){

        List <Samochod> lista = wypozyczalnia.zwrocListe();
        for (Samochod samochod : lista) {
            System.out.println(samochod);
        }
    }

    public void obslugaZwrocListeDostepnych(){
        List <Samochod> lista = wypozyczalnia.zwrocListeDostepnych();
        for (Samochod samochod : lista) {
            System.out.println(samochod);
        }
    }

    public void obslugaZwrocListeWynajetych(){
        List <Samochod> lista = wypozyczalnia.zwrocListeWynajetych();
        for (Samochod samochod : lista) {
            System.out.println(samochod);
        }
    }

    public void obslugaUsunSamochod(){
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        wypozyczalnia.usunSamochod(numerRejestracyjny);

    }

    public void obslugaSprawdzCeneSamochodu(){
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        System.out.println("Podaj na ile dni zamierzasz wypożyczyć auto");
        int iloscDni = scanner.nextInt();
        Optional <Double> cena = wypozyczalnia.sprawdzCeneSamochodu(numerRejestracyjny, iloscDni);
        System.out.println(cena);
    }

    public void obslugaWynajmij(){
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        System.out.println("Podaj imie i nazwisko");
        String imieInazwisko = scanner.nextLine();
        scanner.nextLine();
        System.out.println("Podaj na ile dni zamierzasz wypożyczyć auto");
        int iloscDni = scanner.nextInt();
        wypozyczalnia.wynajmij(numerRejestracyjny, imieInazwisko,  iloscDni);

    }

    public void obslugaZawrocSamochod(){
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        System.out.println("Podaj numwer identyfikatora");
        String identyfikator = scanner.next();
        wypozyczalnia.zwrocSamochod(numerRejestracyjny, identyfikator);

    }

    public void obslugaZwrocListeAktywnych(){
        List <WynajemSamochodu> lista = wypozyczalnia.listaAktywnychWynajmów();
        for (WynajemSamochodu samochod : lista) {
            System.out.println(samochod);
        }
    }

    public void obslugaZwrocListeZakonczonychWynajmów(){
        List <WynajemSamochodu> lista = wypozyczalnia.listaZakonczonychWynajmów();
        for (WynajemSamochodu samochod : lista) {
            System.out.println(samochod);
        }
    }
}
