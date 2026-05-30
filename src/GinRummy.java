import java.util.ArrayList;

public class GinRummy extends Normes {
    static ArrayList<Carta> pilaDescartades = new ArrayList<>();
    static int[] puntsAcumulatsJugador = new int[Joc.arrayJugadors.length];
    static boolean esDePilaDescarts = false;

    public static void jugarGinRummy() {
        GinRummy jocActual = new GinRummy();

        boolean hiHaGuanyador = false;

        while (!hiHaGuanyador) {
            // imprimir la carta d'adalt de tot de la pila descartades

            boolean haDescartatCarta = false;
            boolean accioCompletada = false;
            int accio;

            Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
            Consola.tornDe(jugadorActual.nom);
            Consola.espais();
            Consola.missatgeCartes();
            ordenarCartes(jugadorActual.maCartes);
            Consola.mostrarBaralla(jugadorActual.maCartes);
            Consola.espais();

            esDePilaDescarts = false;
            Carta cartaAgafada = jocActual.agafarCarta(jugadorActual);
            jugadorActual.maCartes.add(cartaAgafada);

            while (!accioCompletada) {
                Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
                Consola.espais();

                boolean barallabuida = Joc.barallaPartida.baralla.isEmpty();


                hiHaGuanyador = jocActual.haGuanyat(jugadorActual);
            }

            while (!haDescartatCarta) {
                Consola.missatgeTriarCartaDescartar();
                int numCartaDescartar = jocActual.intdexCartaQueVolDescartar(jugadorActual);
                Carta cartaQueVolDescartar = jugadorActual.maCartes.get(numCartaDescartar);

                if (cartaAgafada.equals(cartaQueVolDescartar) && esDePilaDescarts) {
                    Consola.missatgeNoEsPotDescartar(cartaQueVolDescartar);
                } else {
                    pilaDescartades.add(cartaQueVolDescartar);
                    jugadorActual.maCartes.remove(numCartaDescartar);
                    Consola.missatgeSiEsPotDescartar(cartaQueVolDescartar);
                    haDescartatCarta = true;
                }
            }
        }
    }

    Carta agafarCarta(Jugador jugador) {
        Carta cartaAgafada = null;
        int opcioAgafar = Consola.demanarDonAgafar();

        if (opcioAgafar == 1) {
            int midaBaralla = Joc.barallaPartida.baralla.size();
            cartaAgafada = Joc.barallaPartida.baralla.get(midaBaralla -1);
            Joc.barallaPartida.baralla.remove(midaBaralla -1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = false;
        } else if (opcioAgafar == 2) {
            int midaPilaDescarts = pilaDescartades.size();
            cartaAgafada = pilaDescartades.get(midaPilaDescarts -1);
            pilaDescartades.remove(midaPilaDescarts -1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = true;
        }
        return cartaAgafada;
    }

    int intdexCartaQueVolDescartar(Jugador jugador) {
        int numCartaDescartar =  Consola.demanarIndexCarta(jugador.maCartes);
        return numCartaDescartar;
    }


    int comptarPuntsCombinacio(ArrayList<Carta> combinacio) {
        int punts = 0;
        boolean esGrup = esGrupValid(combinacio);
        boolean esEscala = esEscalaValida(combinacio);

        int indexFitxaReal = -1;
        int valorFitxaReal = 0;

        for (int i = 0; i < combinacio.size(); i++) {
            if (!combinacio.get(i).esJoker()) {
                indexFitxaReal = i;
                valorFitxaReal = combinacio.get(i).getValor();
                break;
            }
        }

        for (int i = 0; i < combinacio.size(); i++) {
            if (!combinacio.get(i).esJoker()) {
                punts += combinacio.get(i).getValor();
            } else {
                if (esGrup) {
                    punts += valorFitxaReal;
                } else if (esEscala) {
                    punts += valorFitxaReal + (i - indexFitxaReal);
                }
            }
        }
        return punts;
    }
}