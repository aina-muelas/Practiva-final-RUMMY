public class Joc {
    static int modalitatJoc = Consola.triarModalitat();
    static int numJugadors = Consola.triarNumJugadors(modalitatJoc);

    static Jugador [] arrayJugadors = new Jugador [numJugadors];

    public static void establirNomsJugadors() {
        for (int i = 0; i < arrayJugadors.length; i++) {
            arrayJugadors[i] = new Jugador(Consola.triarNom(i));

        }
    }

    public static  void main (String [] args) {
        establirNomsJugadors();
        System.out.println(arrayJugadors[0]);
        System.out.println();
        System.out.println(arrayJugadors[1]);
    }

}
