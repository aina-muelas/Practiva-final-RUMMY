import java.util.ArrayList;

public class RummyArgentino extends Normes {

    private static int numPuntsGuanyar;
    private static boolean esDePilaDescarts = false;

    public static void jugarRummyArgentino() {

        RummyArgentino jocActual = new RummyArgentino();
        numPuntsGuanyar = Consola.numPuntsGuanyar(Joc.numJugadors);

        boolean hiHaGuanyadorPartida = false;

        while (!hiHaGuanyadorPartida) {
            jocActual.inicialitzarNovaRonda();
            boolean hiHaGuanyadorRonda = false;

            while (!hiHaGuanyadorRonda) {
                int opcio = Consola.demanarSiVolJugarOGuardar();

                if (opcio == 2) {
                    Joc.guardarEstatPartida();
                } else {
                    if (Joc.barallaPartida.baralla.isEmpty()) {
                        jocActual.restaurarBarallaSiEstaBuida();
                    }

                    Consola.imprimirPuntsJugadors(Joc.arrayJugadors);
                    Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
                    jocActual.mostrarInfoAJugador(jugadorActual);

                    esDePilaDescarts = false;
                    Carta cartaAgafada = jocActual.agafarCarta();
                    jugadorActual.maCartes.add(cartaAgafada);
                    ordenarCartes(jugadorActual.maCartes);
                    Consola.mostrarMaCartes(jugadorActual.maCartes);

                    boolean accioCompletada = false;
                    while (!accioCompletada) {
                        int accio = Consola.demanarAccioRummyClassic();

                        if (accio == 1) {
                            accioCompletada = true;
                        } else if (accio == 2) {
                            jocActual.tirarCombinacions(jugadorActual);
                        } else if (accio == 3) {
                            jocActual.afegirFitxaCombinacio(jugadorActual);
                        }

                        hiHaGuanyadorRonda = jocActual.guanyadorRonda(jugadorActual);
                        if (hiHaGuanyadorRonda) {
                            hiHaGuanyadorPartida = jocActual.haGuanyat(jugadorActual);
                            accioCompletada = true;

                        }
                    }

                    if (!hiHaGuanyadorRonda) {
                        jocActual.descartarCarta(jugadorActual, cartaAgafada);
                        hiHaGuanyadorRonda = jocActual.guanyadorRonda(jugadorActual);

                        if (hiHaGuanyadorRonda) {
                            hiHaGuanyadorPartida = jocActual.haGuanyat(jugadorActual);

                        }
                    }

                    if (!hiHaGuanyadorRonda) {
                        Torn.calcularTorn(Joc.arrayJugadors.length);
                    }
                }
            }
        }
    }

    private void inicialitzarNovaRonda() {
        Joc.pilaDescartades.clear();
        Joc.taulaComuna.clear();
        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Joc.arrayJugadors[i].maCartes.clear();
            Joc.arrayJugadors[i].puntsMa = 0;
        }

        int numBaralles = Baralla.determinarQuantitatBaralles(1, Joc.arrayJugadors.length);
        Joc.barallaPartida.inicialitzarBaralla(1, numBaralles);
        Joc.barallaPartida.mesclarCartes();
        Joc.repartirCartes(Joc.barallaPartida);

        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Normes.ordenarCartes(Joc.arrayJugadors[i].maCartes);
        }

        int tamanyBaralla = Joc.barallaPartida.baralla.size();
        Carta cartaInicial = Joc.barallaPartida.baralla.get(tamanyBaralla - 1);
        Joc.barallaPartida.baralla.remove(tamanyBaralla - 1);
        Joc.pilaDescartades.add(cartaInicial);
    }

    private void restaurarBarallaSiEstaBuida() {
        Carta cartaAAfegir = Joc.pilaDescartades.getLast();
        Joc.pilaDescartades.remove(Joc.pilaDescartades.size() - 1);

        Joc.barallaPartida.baralla.addAll(Joc.pilaDescartades);
        Joc.barallaPartida.mesclarCartes();
        Joc.pilaDescartades.clear();
        Joc.pilaDescartades.add(cartaAAfegir);
    }

    private void mostrarInfoAJugador(Jugador jugadorActual) {
        Consola.tornDe(jugadorActual.nom);
        Consola.espais();
        Consola.mostrarTaulaComuna(Joc.taulaComuna);
        Consola.espais();
        Consola.missatgeCartes();
        Consola.mostrarMaCartes(jugadorActual.maCartes);
        Consola.espais();
        Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
        Consola.darreraCartaPilaDescards(Joc.pilaDescartades.getLast());
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
            int midaPilaDescarts = Joc.pilaDescartades.size();
            cartaAgafada = Joc.pilaDescartades.get(midaPilaDescarts - 1);
            Joc.pilaDescartades.remove(midaPilaDescarts - 1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = true;
        }
        return cartaAgafada;
    }

    private boolean tirarCombinacions(Jugador jugador) {
        int puntsCombinacio = 0;
        boolean supera700 = maCartesSupera700(jugador);
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());

        boolean seguirCreant = true;
        if (supera700) {
            Consola.missatgeMaSupera700();
        }

        ArrayList<ArrayList<Carta>> combinacionsNoves = new ArrayList<ArrayList<Carta>>();

        while (seguirCreant && !jugador.getMaCartes().isEmpty()) {
            ArrayList<Carta> combinacioNova = Consola.demanarNovaCombinacioGeneral(jugador);
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


        for (int i = 0; i < combinacionsNoves.size(); i++) {
            ArrayList<Carta> combinacio = combinacionsNoves.get(i);
            puntsCombinacio += comptarPuntsCombinacio(combinacio);
        }

        if (supera700 && puntsCombinacio < 100) {
            jugador.maCartes.clear();
            jugador.maCartes = new ArrayList<>(copiaMaInicial);
            Consola.missatgeMaSupera700();
            return false;
        } else {
            for (int i = 0; i < combinacionsNoves.size(); i++) {
                ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                Joc.taulaComuna.add(combinacio);
            }
        }
        return true;

    }

    private void afegirFitxaCombinacio(Jugador jugador) {
        int indexCarta = Consola.demanarIndexCarta(jugador.maCartes);
        if (indexCarta < 0 || indexCarta >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCombinacio = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexCombinacio < 0 || indexCombinacio >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacio = Joc.taulaComuna.get(indexCombinacio);
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

    private void descartarCarta(Jugador jugadorActual, Carta cartaAgafada) {
        boolean haDescartatCarta = false;
        while (!haDescartatCarta) {
            Consola.missatgeTriarCartaDescartar();
            int numCartaDescartar = Consola.demanarIndexCarta(jugadorActual.maCartes);
            Carta cartaQueVolDescartar = jugadorActual.maCartes.get(numCartaDescartar);

            if (cartaAgafada.equals(cartaQueVolDescartar) && esDePilaDescarts) {
                Consola.missatgeNoEsPotDescartar(cartaQueVolDescartar);
            } else {
                Joc.pilaDescartades.add(cartaQueVolDescartar);
                jugadorActual.maCartes.remove(numCartaDescartar);
                Consola.missatgeSiEsPotDescartar(cartaQueVolDescartar);
                haDescartatCarta = true;
            }
        }
    }

    private int obtenirValorCarta(Carta carta) {
        String numero = carta.numero;

        if (carta.esJoker()) {
            return 50;
        } else if (carta.esMono()) {
            return 20;
        } else if (numero.equals("AS")) {
            return 15;
        } else if (numero.equals("8") || numero.equals("9") || numero.equals("10")
                || numero.equals("J") || numero.equals("Q") || numero.equals("K")) {
            return 10;
        } else if (numero.equals("2") || numero.equals("3") || numero.equals("4")
                || numero.equals("5") || numero.equals("6") || numero.equals("7")) {
            return 5;
        }
        return 0;
    }

    private int comptarPunts(ArrayList<Carta> cartesJugador) {
        int punts = 0;

        for (int i = 0; i < cartesJugador.size(); i++) {
            Carta cartaActual = cartesJugador.get(i);
            punts += obtenirValorCarta(cartaActual);
        }
        return punts;
    }

    private int comptarPuntsCombinacio(ArrayList<Carta> combinacio) {
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

    private boolean guanyadorRonda(Jugador jugadorActual) {
        if (jugadorActual.maCartes.isEmpty()) {
            for (int i = 0; i < Joc.arrayJugadors.length; i++) {
                if (Joc.arrayJugadors[i] != jugadorActual) {
                    jugadorActual.puntuacio += comptarPunts(Joc.arrayJugadors[i].maCartes);
                }
            }
            Consola.missatgeGuanyadorRonda(jugadorActual, jugadorActual.puntuacio);
            return true;
        }
        return false;
    }

    private boolean maCartesSupera700(Jugador jugador) {
        if (jugador.puntsMa > 700) {
            return true;
        }
        return false;
    }

    @Override
    public boolean haGuanyat(Jugador jugador) {
        return false;
    }
}
