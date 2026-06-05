import java.util.ArrayList;

public class Joc {
    final int NOVA_PARTIDA = 1;
    final int RESTAURAR_PARTIDA = 2;

    static int modalitatJoc;
    static int numJugadors;
    static Jugador [] arrayJugadors;
    public static Baralla barallaPartida;
    public static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();
    public static ArrayList<Carta> pilaDescartades = new ArrayList<>();

    public void jugar(){
        int opcioTriada = Consola.demanarQueVolFer();

        if (opcioTriada == NOVA_PARTIDA) {
            prepararNovaPartida();
        } else if (opcioTriada == RESTAURAR_PARTIDA) {
            restaurarEstatPartidaExistent();
        }

        if (modalitatJoc == 1) {
            RummyClassic.jugarRummyClassic();
        } else if (modalitatJoc == 2) {
            Rummikub.jugarRummiKub();
               } else if (modalitatJoc == 3) {
            GinRummy.jugarGinRummy();
        } else if (modalitatJoc == 4) {
            RummyArgentino.jugarRummyArgentino();
        }
    }

    void prepararNovaPartida() {
        modalitatJoc = Consola.triarModalitat();
        numJugadors = Consola.triarNumJugadors(modalitatJoc);
        arrayJugadors = new Jugador [numJugadors];
        taulaComuna.clear();
        pilaDescartades.clear();

        establirNomsJugadors();
        barallaPartida = new Baralla();
        int nBaralles = Baralla.determinarQuantitatBaralles(modalitatJoc, numJugadors);
        barallaPartida.inicialitzarBaralla(modalitatJoc, nBaralles);
        barallaPartida.mesclarCartes();
        repartirCartes(barallaPartida);
    }

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

    public static void guardarEstatPartida() {
        String nomPartida = Consola.demanarNomPartida();

        GuardarORestaurarPartida gestor = new GuardarORestaurarPartida(
                nomPartida, modalitatJoc, Torn.jugaActual, arrayJugadors, taulaComuna, barallaPartida, pilaDescartades
        );
        gestor.guardarPartida();
        Consola.missatgePartidaGuardada(nomPartida);
        System.exit(0);
    }

    void restaurarEstatPartidaExistent() {
        Consola.mostrarPartidesGuardades();
        String nomPartida = Consola.demanarNomPartida();
        GuardarORestaurarPartida restaurador = new GuardarORestaurarPartida(nomPartida);
        restaurador.restaurarPartida();
        modalitatJoc = restaurador.getModalitat();
        arrayJugadors = restaurador.getArrayJugadors();
        numJugadors = arrayJugadors.length;
        barallaPartida = restaurador.getBaralla();
        Torn.jugaActual = restaurador.getTorn();
        taulaComuna = restaurador.getTaulerComu();
        pilaDescartades = restaurador.getPilarDescartades();
        Consola.missatgePartidaRestauradaCorrecte(nomPartida);
    }
}