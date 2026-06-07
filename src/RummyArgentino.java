import java.util.ArrayList;

public class RummyArgentino extends Normes {

    private static int numPuntsGuanyar;

    private static final int FINALITZAR_TORN = 1;
    private static final int TIRAR_COMBINACIONS = 2;
    private static final int MODIFICAR_COMBINACIONS = 3;

    private static final int ACABAR_MODIFICACIONS = 1;
    private static final int AFEGIR_FITXA_COMBINACIO = 2;
    private static final int SUBSTITUIR_COMODI = 3;

    public static void jugarRummyArgentino() {

        RummyArgentino jocActual = new RummyArgentino();
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
                        int accio = Consola.demanarQueFerRummyArgentino();

                        if (accio == FINALITZAR_TORN) {
                            accioCompletada = true;
                        } else if (accio == TIRAR_COMBINACIONS) {
                            jocActual.tirarCombinacions(jugadorActual);
                        } else if (accio == MODIFICAR_COMBINACIONS) {
                            jocActual.modificarTaula(jugadorActual);
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

        int numBaralles = Baralla.determinarQuantitatBaralles(Baralla.RUMMY_ARGENTINO, Joc.arrayJugadors.length);
        Joc.barallaPartida.inicialitzarBaralla(Baralla.RUMMY_ARGENTINO, numBaralles);
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

    private boolean modificarTaula(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());
        ArrayList<ArrayList<Carta>> copiaTaulaInicial = new ArrayList<>();

        for (int i = 0; i < Joc.taulaComuna.size(); i++) {
            ArrayList<Carta> combinacioActu = new ArrayList<>(Joc.taulaComuna.get(i));
            copiaTaulaInicial.add(combinacioActu);
        }

        int numCartesMaJugador = jugador.maCartes.size();
        boolean seguirModificant = true;

        while (seguirModificant && !jugador.maCartes.isEmpty()) {
            Consola.espais();
            Consola.missatgeMostrarTaula();
            Consola.mostrarTaulaComuna(Joc.taulaComuna);
            Consola.espais();
            Consola.missatgeCartes();
            Consola.mostrarMaCartes(jugador.maCartes);
            Consola.espais();

            int opcio = Consola.demanarQueModificarArgentino();

            if (opcio == ACABAR_MODIFICACIONS) {
                seguirModificant = false;
            } else if (opcio == AFEGIR_FITXA_COMBINACIO) {
                afegirFitxaCombinacio(jugador);
            } else if (opcio == SUBSTITUIR_COMODI) {
                substituirComodi(jugador);
            }
        }

        for (int i = 0; i < Joc.taulaComuna.size(); i++) {
            if (!esCombinacioValida(Joc.taulaComuna.get(i))) {
                Consola.missatgeModificacioNoValida();
                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMaInicial);
                Joc.taulaComuna.clear();
                Joc.taulaComuna.addAll(copiaTaulaInicial);
                return false;
            }
        }

        if (jugador.maCartes.size() >= numCartesMaJugador) {
            Consola.missatgeMinimTirarUnaCarta();
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMaInicial);
            Joc.taulaComuna.clear();
            Joc.taulaComuna.addAll(copiaTaulaInicial);
            return false;
        }
        Consola.missatgeModificacioValida();
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

    private void substituirComodi(Jugador jugador) {
        int moureCombincioExistent = 1;
        int moureNovaCombinacio = 2;

        int indexOrigen = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexOrigen < 0 || indexOrigen >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCartaASubstituir = Consola.demanarIndexCarta(Joc.taulaComuna.get(indexOrigen));
        if (indexCartaASubstituir < 0 || indexCartaASubstituir >= Joc.taulaComuna.get(indexOrigen).size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        Carta cartaASubstituir = Joc.taulaComuna.get(indexOrigen).get(indexCartaASubstituir);
        if (!cartaASubstituir.esMono() && !cartaASubstituir.esJoker()) {
            System.out.println("No es pot moure una carta que NO sigui un comodi");
            return;
        }

        int indexCartaSubstitueix = Consola.demanarIndexCarta(jugador.maCartes);
        Carta cartaQueSubstitueix = jugador.maCartes.get(indexCartaSubstitueix);


        if (indexCartaSubstitueix < 0 || indexCartaSubstitueix >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        Joc.taulaComuna.get(indexOrigen).set(indexOrigen, cartaQueSubstitueix);
        jugador.maCartes.remove(indexCartaSubstitueix);

        int opcio = Consola.demanarOnMoure();

        if (opcio == moureCombincioExistent) {
            moureFitxaEntreCombinacions(cartaASubstituir);
        } else if (opcio == moureNovaCombinacio) {
            moureFitxaNovaCombinacio(cartaASubstituir);
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

    private void moureFitxaEntreCombinacions(Carta cartaAMoure) {
        int indexDesti = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexDesti < 0 || indexDesti >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacioDesti = Joc.taulaComuna.get(indexDesti);
        int indexPosicio = Consola.demanarPosicioDinsCombiancio(combinacioDesti);
        if (indexPosicio < 0 || indexPosicio > combinacioDesti.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        combinacioDesti.add(indexPosicio, cartaAMoure);
        Consola.missatgeCartaAfegida(indexPosicio, indexDesti);

    }

    private void moureFitxaNovaCombinacio(Carta cartaAMoure) {
        ArrayList<Carta> indexNou = new ArrayList<>();
        Joc.taulaComuna.add(indexNou);

        ArrayList<Carta> indexDesti = Joc.taulaComuna.getLast();
        int posDarrerIndex = Joc.taulaComuna.size();

        ArrayList<Carta> combinacioDesti = indexDesti;

        combinacioDesti.add(cartaAMoure);
        Consola.missatgeCartaAfegida(0, posDarrerIndex);
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

            if (cartaActual.esJoker()) {
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

    public void calcularPuntsMa(Jugador jugador) {
        jugador.puntsMa = 0;
        for (int i = 0; i < jugador.maCartes.size(); i++) {
            Carta cartaActual = jugador.maCartes.get(i);
            jugador.puntsMa += obtenirValorCarta(cartaActual);
        }
    }

}
