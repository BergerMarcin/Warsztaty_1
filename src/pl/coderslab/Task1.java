package pl.coderslab;

// Napisz prostą grę w zgadywanie liczb. Komputer ma wylosować liczbę w zakresie od 1 do 100.
//Następnie:
//1. wypisać: "Zgadnij liczbę" i pobrać liczbę z klawiatury;
//2. sprawdzić, czy wprowadzony napis, to rzeczywiście liczba i w razie błędu wyświetlić komunikat: "To
//nie jest liczba", po czym wrócić do pkt. 1;
//3. jeśli liczba podana przez użytkownika jest mniejsza niż wylosowana, wyświetlić komunikat: "Za
//mało!", po czym wrócić do pkt. 1;
//4. jeśli liczba podana przez użytkownika jest większa niż wylosowana, wyświetlić komunikat: "Za
//dużo!", po czym wrócić do pkt. 1;
//5. jeśli liczba podana przez użytkownika jest równa wylosowanej, wyświetlić komunikat: "Zgadłeś!",
//po czym zakończyć działanie programu.

import java.util.Random;
import java.util.Scanner;

public class Task1 {

    public static void main(String[] args) {
        final int min = 1;
        final int max = 100;

        Random rand = new Random();
        int intDrawn = rand.nextInt(max-min+1)+min;
        // System.out.println(intDrawn);   // To test code

        Scanner scan = new Scanner(System.in);
        int intUser = 0;
        int noOfDraws = 0;
        String prompt = "Please write/draw number (integer, from " + min + " to " + max + "): ";
        do {
            noOfDraws ++;
            System.out.println();
            intUser = readIntRangeMinMax(prompt, min, max, scan);
            if (intUser == intDrawn) {
                System.out.println("You wrote right value in " + noOfDraws + " draws/tries. CONGRATULATION & CELEBRATION! :)");
            } else if (intUser < intDrawn) {
                System.out.println("You wrote TOO LITTLE value. Number of draws/tries: " + noOfDraws);
            } else {
                System.out.println("You wrote TOO HIGH value. Number of draws/tries: " + noOfDraws);
            }
        } while (intUser != intDrawn);

    }


    /**
     * Reading from console integer and >0 value and <=max. Reading till getting correct data.
     * @param prompt   - write on console comment before data input
     * @param scan   - Scanner variable/stream initialized in over-method initialising that method
     * @return  - integer >0 value
     */
    static int readIntRangeMinMax(String prompt, int min, int max, Scanner scan) {

        int inInt = 0;
        do {
            System.out.print(prompt);
            while (!scan.hasNextInt()) {
                System.out.println("Wrong data");
                scan.next();
                System.out.print(prompt);
            }
            inInt = scan.nextInt();
            if ((inInt < min) || (inInt > max)) {
                System.out.println("Wrong data");
            }
        } while ((inInt < min) || (inInt > max));
        return inInt;
    }


}
