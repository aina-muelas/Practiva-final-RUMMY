import java.util.ArrayList;
import java.util.Scanner;

public class Consola {
    public static Scanner scanner = new Scanner(System.in);

    public static void mostrarBaralla(ArrayList<Carta> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            Carta c = cartes.get(i);
            System.out.println((i + 1)+ ". " + c.toString());
        }
    }

    public static int triarModalitat(){
        int modalitat = 0;
        while (modalitat <= 0) {
            System.out.println("Indica a quina modalitat vols jugar: '1': Rummy Argtenmtino, '2': GinRummy, '3': rummyKub");
            modalitat = scanner.nextInt();
            scanner.nextLine();
            if (modalitat <= 0 || modalitat > 3) {
                System.out.println("Modalitat de joc no v�lida, torna a triar");
            }
        }
        return modalitat;
    }
    public static int triarNumJugadors(int modalitatTriada){
        int numJugadors = 1;
        if (modalitatTriada == 1) {
            while (numJugadors < 2 || numJugadors > 6) {
                System.out.println("Introdueix el num de jugadors per al rummyArgentino, recorda: minim 2 i maxim 6");
                numJugadors = scanner.nextInt();
                scanner.nextLine();
                if (numJugadors < 2 || numJugadors > 6) {
                    System.out.println("Num de jugadors no valid");
                }
            }
        } else if (modalitatTriada == 2) {
            System.out.println("el num de jugadors per al GinRummy es 2");
            numJugadors = 2;
        } else if (modalitatTriada == 3) {
            while (numJugadors < 2 || numJugadors > 6) {
                System.out.println("Introdueix el num de jugadors per al Rummikub, recorda: minim 2 i maxim 4");
                numJugadors = scanner.nextInt();
                scanner.nextLine();
                if (numJugadors < 2 || numJugadors > 4) {
                    System.out.println("Num de jugadors no valid");
                }
            }
        }
        return numJugadors;
    }

    public static String triarNom(int num) {
        String nom;
        System.out.println("es el torn del jugador " + num + " per triar el seu nom");
        nom = scanner.nextLine();
        return nom;
    }

}
