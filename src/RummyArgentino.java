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
        int totalJokersJugada = 0;
        int totalMonosJugada = 0;
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

            int numMonosCombi = numMonosCombinacio(combinacioNova);
            int numJokersCombi = numJokersCombinacio(combinacioNova);

            boolean esGrup = esGrupValid(combinacioNova);
            if (esGrup && (numJokersCombi + numMonosCombi > 1)) {
                Consola.missatgeNumComodinsInvalidArgentino();
                jugador.maCartes.addAll(combinacioNova);
                ordenarCartes(jugador.maCartes);
            }

            if ((totalJokersJugada + numJokersCombi >= 3) || (totalMonosJugada + numMonosCombi >= 3)) {
                Consola.missatgeNumComodinsTotalsInvalidArgentino();
                jugador.maCartes.addAll(combinacioNova);
                ordenarCartes(jugador.maCartes);
            }

            if (!esCombinacioValida(combinacioNova)) {
                Consola.missatgeCombinacioNoValida();
                jugador.maCartes.addAll(combinacioNova);
                ordenarCartes(jugador.maCartes);
            } else {
                actualitzarMobilitatComodins(combinacioNova);
                combinacionsNoves.add(combinacioNova);
                totalJokersJugada += numJokersCombi;
                totalMonosJugada += numMonosCombi;
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

    private int numMonosCombinacio(ArrayList<Carta> combinacio) {
        int numMonos = 0;
        for (int i = 0; i < combinacio.size(); i++) {
            Carta cartaActual = combinacio.get(i);
            if (cartaActual.esMono()) {
                numMonos++;
            }
        }
        return numMonos;
    }

    private int numJokersCombinacio(ArrayList<Carta> combinacio) {
        int numJokers = 0;
        for (int i = 0; i < combinacio.size(); i++) {
            Carta cartaActual = combinacio.get(i);
            if (cartaActual.esJoker()) {
                numJokers++;
            }
        }
        return numJokers;
    }

    private void actualitzarMobilitatComodins(ArrayList<Carta> combinacio) {
        for (int i = 0; i < combinacio.size(); i++) {
            Carta carta = combinacio.get(i);

            if (carta.esJoker() || carta.esMono()) {
                if (i > 0 && i < combinacio.size() - 1) {
                    Carta cAnterior = combinacio.get(i - 1);
                    Carta cSeguent = combinacio.get(i + 1);

                    if (!cAnterior.esJoker() && !cAnterior.esMono() && !cSeguent.esJoker() && !cSeguent.esMono()) {
                        carta.setEsPotMoure(false);
                    } else {
                        carta.setEsPotMoure(true);
                    }
                }
            } else {
                carta.setEsPotMoure(true);
            }
        }
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

        int rangReferencia = -1;
        int indexReferencia = -1;

        for (int i = 0; i < combinacio.size(); i++) {
            Carta carta = combinacio.get(i);
            if (!carta.esJoker() && !carta.esMono()) {
                rangReferencia = agafarRang(carta);
                indexReferencia = i;
                break;
            }
        }

        for (int i = 0; i < combinacio.size(); i++) {
            Carta cartaActual = combinacio.get(i);

            if(cartaActual.esJoker()) {
                punts += 50;
            } else if (cartaActual.esMono()) {
                int rangSuplantat;
                if (esGrup) {
                    rangSuplantat = rangReferencia;
                } else {
                    rangSuplantat = rangReferencia + (i - indexReferencia);
                }

                if (rangSuplantat >= 3 && rangSuplantat <= 7) {
                    punts += 5;
                } else if (rangSuplantat == 1) {
                    punts += 15;
                } else if (rangSuplantat == 2) {
                    punts += 5;
                } else {
                    punts += 20;
                }
            } else {
                punts += obtenirValorCarta(cartaActual);
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
        if (jugador.puntuacio >= numPuntsGuanyar) {
            Consola.missatgeGuanyador(jugador);
            return true;
        }
        return false;
    }
}
