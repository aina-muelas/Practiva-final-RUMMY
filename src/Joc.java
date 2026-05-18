public class Joc {
    public static void prepararPartida(){
        Baralla laMevaBaralla = new Baralla();
        int nBaralles = Baralla.determinarQuantitatBaralles(modalitatJoc, numJugadors);
        laMevaBaralla.inicialitzarBaralla(modalitatJoc, nBaralles);
        laMevaBaralla.mesclarCartes();
        repartirCartes(laMevaBaralla);
    }
    static int modalitatJoc = Consola.triarModalitat();
    static int numJugadors = Consola.triarNumJugadors(modalitatJoc);

    static Jugador [] arrayJugadors = new Jugador [numJugadors];

    public static void establirNomsJugadors() {
        for (int i = 0; i < arrayJugadors.length; i++) {
            arrayJugadors[i] = new Jugador(Consola.triarNom(i));

        }
    }


    static int determinarCartesInicials(int modalitat, int numJugadors) {
        int cartes = 7; // valor per defecte
        switch (modalitat) {
            case 1: // rummy classic
                if (numJugadors == 2) cartes = 10;
                else if (numJugadors == 5 || numJugadors == 6) cartes = 6;
                break;
            case 2: // Rummikub
                cartes = 14;
                break;
            case 3: // Gin Rummy
                cartes = 10;
                break;
            case 4: // Rummy Argentino
                cartes = 13;
                break;
        }
        return cartes;
    }

    public static void repartirCartes(Baralla laMevaBaralla) {
        int numCartes = determinarCartesInicials(modalitatJoc, numJugadors);

        for (int i = 0; i < numCartes; i++) {

            for (int j = 0; j < arrayJugadors.length; j++) {

                if (laMevaBaralla.baralla.size() > 0) {

                    Carta c = laMevaBaralla.baralla.get(0);

                    arrayJugadors[j].maCartes.add(c);

                    laMevaBaralla.baralla.remove(0);
                }
            }
        }
    }
}