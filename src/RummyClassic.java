import java.util.ArrayList;

public class RummyClassic extends Normes {

    private static final int FINALITZAR_TORN = 1;
    private static final int TIRAR_COMBINACIONS = 2;
    private static final int MODIFICAR_COMBINACIONS = 3;

    private static int numPuntsGuanyar;

    public static void jugarRummyClassic() {
        RummyClassic jocActual = new RummyClassic();
        numPuntsGuanyar = Consola.numPuntsGuanyar(Joc.numJugadors);
        boolean hiHaGuanyadorPartida = false;

        while (!hiHaGuanyadorPartida) {
            jocActual.inicialitzarNovaRonda();
            boolean hiHaGuanyadorRonda = false;

            while (!hiHaGuanyadorRonda) {
                int opcio = Consola.demanarSiVolJugarOGuardar();

                if (opcio == GUARDAR_PARTIDA) {
                    Joc.guardarEstatPartida();
                } else {
                    if (Joc.barallaPartida.baralla.isEmpty()) {
                        jocActual.restaurarBarallaSiEstaBuida();
                    }

                    Consola.imprimirPuntsJugadors(Joc.arrayJugadors);
                    Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
                    jocActual.mostrarTaulaComuna();
                    jocActual.mostrarInfoGeneralAJugador(jugadorActual);
                    jocActual.mostrarInfoAgafarCartaBarallaODescartades();

                    Carta cartaAgafada = jocActual.agafarCarta();
                    jugadorActual.maCartes.add(cartaAgafada);
                    ordenarCartes(jugadorActual.maCartes);
                    Consola.mostrarMaCartes(jugadorActual.maCartes);

                    boolean accioCompletada = false;
                    while (!accioCompletada) {
                        int accio = Consola.demanarAccioRummyClassic();
                        if (accio == FINALITZAR_TORN) {
                            accioCompletada = true;
                        } else if (accio == TIRAR_COMBINACIONS) {
                            jocActual.tirarCombinacions(jugadorActual);
                        } else if (accio == MODIFICAR_COMBINACIONS) {
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
        }

        int numBaralles = Baralla.determinarQuantitatBaralles(Baralla.RUMMY_CLASSIC, Joc.arrayJugadors.length);
        Joc.barallaPartida.inicialitzarBaralla(Baralla.RUMMY_CLASSIC, numBaralles);
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

    private boolean tirarCombinacions(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());

        boolean seguirCreant = true;
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

        Consola.missatgeJugadaAcceptada();
        for (int i = 0; i < combinacionsNoves.size(); i++) {
            ArrayList<Carta> combinacio = combinacionsNoves.get(i);
            Joc.taulaComuna.add(combinacio);
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

    private boolean guanyadorRonda(Jugador jugadorActual) {
        if (jugadorActual.maCartes.isEmpty()) {
            for (int i = 0; i < Joc.arrayJugadors.length; i++) {
                if (Joc.arrayJugadors[i] != jugadorActual) {
                    Jugador jugadorRivalActu = Joc.arrayJugadors[i];
                    calcularPuntsMa(jugadorRivalActu);
                    jugadorActual.puntuacio += jugadorRivalActu.puntsMa;
                }
            }
            Consola.missatgeGuanyadorRonda(jugadorActual, jugadorActual.puntuacio);
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

    public void calcularPuntsMa(Jugador jugador) {
        jugador.puntsMa = 0;

        for (int i = 0; i < jugador.maCartes.size(); i++) {
            Carta cartaActual = jugador.maCartes.get(i);
            if (cartaActual.esAS()) {
                jugador.puntsMa += 15;
            } else {
                jugador.puntsMa += cartaActual.getValor();
            }
        }
    }

}