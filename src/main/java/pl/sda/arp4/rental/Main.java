package pl.sda.arp4.rental;

import java.util.Scanner;

import pl.sda.arp4.rental.service.Wypozyczalnia;


public class Main {
    public static void main(String[] args) {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        Scanner scanner = new Scanner(System.in);

        Parser parser = new Parser(wypozyczalnia, scanner);
        parser.doIt();
    }
}
