import java.util.ArrayList;

public class RummyClassic extends Normes {
    private static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();
    private static ArrayList<Carta> pilaDescartades = new ArrayList<>();

    private static int numPuntsGuanyar;
    private static boolean esDePilaDescarts = false;

    public static void jugarRummyClassic() {
        RummyClassic jocActual = new RummyClassic();
        numPuntsGuanyar = Consola.numPuntsGuanyar(Joc.numJugadors);

        boolean hiHaGuanyadorPartida = false;

        while (!hiHaGuanyadorPartida) {
            jocActual.inicialitzarNovaRonda();
            boolean hiHaGuanyadorRonda = false;

            while (!hiHaGuanyadorRonda) {
                if (Joc.barallaPartida.baralla.isEmpty()) {
                    Joc.barallaPartida.baralla.addAll(pilaDescartades);
                    pilaDescartades.clear();
                    Carta cartaAAfegir = pilaDescartades.getLast();
                    pilaDescartades.add(cartaAAfegir);
                }

                Consola.imprimirPuntsJugadors(Joc.arrayJugadors);
                Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
                jocActual.mostrarInfoAJugador(jugadorActual);

                esDePilaDescarts = false;
                Carta cartaAgafada = jocActual.agafarCarta();
                jugadorActual.maCartes.add(cartaAgafada);

                ordenarCartes(jugadorActual.maCartes);

                boolean accioCompletada = false;

                while (!accioCompletada) {
                    int accio = Consola.demanarAccioGinRummy();

                    if (accio == 1) {
                        accioCompletada = true;
                    } else if (accio == 2) {
                        jocActual.tirarCombinacions(jugadorActual);
                    } else if (accio == 3) {
                        jocActual.afegirFitxaCombinacio(jugadorActual);
                    }
                    hiHaGuanyadorRonda = jocActual.guanyadorRonda(jugadorActual);

                    hiHaGuanyadorPartida = jocActual.haGuanyat(jugadorActual);
                    if (hiHaGuanyadorPartida) {
                        break;
                    }
                    if (hiHaGuanyadorRonda) {
                        break;
                    }
                }

                if (!hiHaGuanyadorRonda && !hiHaGuanyadorPartida) {
                    jocActual.descartarCarta(jugadorActual, cartaAgafada);

                    Torn.calcularTorn(Joc.arrayJugadors.length);
                }
            }
        }
    }

    private void inicialitzarNovaRonda() {
        pilaDescartades.clear();
        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Joc.arrayJugadors[i].maCartes.clear();
        }
        Joc.barallaPartida.inicialitzarBaralla(3, 1);
        Joc.barallaPartida.mesclarCartes();
        Joc.repartirCartes(Joc.barallaPartida);

        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Normes.ordenarCartes(Joc.arrayJugadors[i].maCartes);
        }

        int tamanyBaralla = Joc.barallaPartida.baralla.size();
        Carta cartaInicial = Joc.barallaPartida.baralla.get(tamanyBaralla - 1);
        Joc.barallaPartida.baralla.remove(tamanyBaralla - 1);
        pilaDescartades.add(cartaInicial);
    }

    private void mostrarInfoAJugador(Jugador jugadorActual) {
        Consola.tornDe(jugadorActual.nom);
        Consola.espais();
        Consola.missatgeCartes();
        Consola.mostrarMaCartes(jugadorActual.maCartes);
        Consola.espais();
        Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
        Consola.darreraCartaPilaDescards(pilaDescartades.getLast());
        Consola.espais();
    }

    private Carta agafarCarta() {
        Carta cartaAgafada = null;
        int opcioAgafar = Consola.demanarDonAgafar();

        if (opcioAgafar == 1) {
            int midaBaralla = Joc.barallaPartida.baralla.size();
            cartaAgafada = Joc.barallaPartida.baralla.get(midaBaralla - 1);
            Joc.barallaPartida.baralla.remove(midaBaralla - 1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = false;
        } else if (opcioAgafar == 2) {
            int midaPilaDescarts = pilaDescartades.size();
            cartaAgafada = pilaDescartades.get(midaPilaDescarts - 1);
            pilaDescartades.remove(midaPilaDescarts - 1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = true;
        }
        return cartaAgafada;
    }

    private boolean tirarCombinacions(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());

        boolean seguirCreant = true;
        ArrayList<ArrayList<Carta>> combinacionsNoves = new ArrayList<ArrayList<Carta>>();

        while (seguirCreant && !jugador.getMaCartes().isEmpty()) {
            ArrayList<Carta> combinacioNova = Consola.demanarNovaCombinacioRummyKub(jugador);
            if (combinacioNova.isEmpty()) {
                break;
            }

            combinacioNova = Normes.ordenarCombinacions(combinacioNova);

            if (!esCombinacioValida(combinacioNova)) {
                Consola.missatgeCombinacioNoValida();
                jugador.maCartes.addAll(combinacioNova);
                ordenarCartes(jugador.maCartes);
            } else {
                combinacionsNoves.add(combinacioNova);
            }
            seguirCreant = Consola.seguirCreantCombinacions(jugador);
        }

        if (combinacionsNoves.isEmpty()) {
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMaInicial);
            return false;
        }

        Consola.missatgeJugadaAcceptada();
        for (int i = 0; i < combinacionsNoves.size(); i++) {
            ArrayList<Carta> combinacio = combinacionsNoves.get(i);
            taulaComuna.add(combinacio);
        }
        return true;

    }

    private void afegirFitxaCombinacio(Jugador jugador) {
        int indexCarta = Consola.demanarIndexCarta(jugador.maCartes);
        if (indexCarta < 0 || indexCarta >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCombinacio = Consola.demanarIndexCombinacio(taulaComuna);
        if (indexCombinacio < 0 || indexCombinacio >= taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacio = taulaComuna.get(indexCombinacio);
        int indexPosicio = Consola.demanarPosicioDinsCombiancio(combinacio);
        if (indexPosicio < 0 || indexPosicio > combinacio.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        Carta carta = jugador.maCartes.get(indexCarta);
        jugador.maCartes.remove(indexCarta);
        combinacio.add(indexPosicio, carta);
        Consola.missatgeCartaAfegida(indexPosicio, indexCombinacio);
    }

    private int comptarPunts(ArrayList<Carta> cartesJugador) {
        int punts = 0;

        for (int i = 0; i < cartesJugador.size(); i++) {
            if (cartesJugador.get(i).esAS()) {
                punts += 15;
            } else {
                punts += cartesJugador.get(i).getValor();
            }
        }
        return punts;
    }

    private void descartarCarta(Jugador jugadorActual, Carta cartaAgafada) {
        boolean haDescartatCarta = false;
        while (!haDescartatCarta) {
            Consola.missatgeTriarCartaDescartar();
            int numCartaDescartar = Consola.demanarIndexCarta(jugadorActual.maCartes);
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

    private boolean guanyadorRonda(Jugador jugadorActual) {
        if (jugadorActual.maCartes.isEmpty()) {
            for (int i = 0; i < Joc.arrayJugadors.length; i++) {
                if (Joc.arrayJugadors[i] != jugadorActual) {
                    jugadorActual.puntuacio += comptarPunts(Joc.arrayJugadors[i].maCartes);
                }
            }
            Consola.missatgeGuanyadorRonda(jugadorActual);
        }
        return false;
    }

    @Override
    public boolean haGuanyat(Jugador jugador) {
        if (jugador.puntuacio >= numPuntsGuanyar) {
            Consola.missatgeGuanyador(jugador);
            return true;
        }
        return false;
    }
}