import java.util.ArrayList;

public class GinRummy extends Normes {

    private static final int ACCIO_COMPLETADA = 1;
    private static final int MOURE_POS_CARTES = 2;
    private static final int TANCAR_RONDA = 3;

    private static final int TANCAMENT_INVALID = 0;
    private static final int TANCAMENT_BIG_GIN = 1;
    private static final int TANCAMENT_GIN = 2;
    private static final int TANCAMENT_KNOCK = 3;


    public static void jugarGinRummy() {
        GinRummy jocActual = new GinRummy();
        boolean hiHaGuanyadorPartida = false;

        while (!hiHaGuanyadorPartida) {
            jocActual.inicialitzarNovaRonda();
            boolean hiHaGuanyadorRonda = false;
            int opcio = Consola.demanarSiVolJugarOGuardar();

            while (!hiHaGuanyadorRonda) {
                if (Joc.barallaPartida.baralla.size() == 2) {
                    Consola.missatgeEmpatGin();
                    hiHaGuanyadorRonda = true;
                }

                if (opcio == GUARDAR_PARTIDA) {
                    Joc.guardarEstatPartida();
                } else {
                    boolean accioCompletada = false;
                    Consola.imprimirPuntsJugadors(Joc.arrayJugadors);
                    Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];

                    jocActual.mostrarInfoGeneralAJugador(jugadorActual);
                    jocActual.mostrarInfoAgafarCartaBarallaODescartades();
                    Carta cartaAgafada = jocActual.agafarCarta();
                    jugadorActual.maCartes.add(cartaAgafada);

                    EvaluarMa millorMa = jocActual.calcularCombinacions(jugadorActual.maCartes);
                    jocActual.combinacionsOrdenades(millorMa.combinacionsPossibles);
                    jocActual.imprimirMillorMa(millorMa);

                    while (!accioCompletada) {
                        int accio = Consola.demanarAccioGinRummy();

                        if (accio == ACCIO_COMPLETADA) {
                            accioCompletada = true;
                        } else if (accio == MOURE_POS_CARTES) {
                            jocActual.mourePosCarta(jugadorActual);
                        } else if (accio == TANCAR_RONDA) {
                            int opcioTancament = jocActual.tancarRonda(jugadorActual);

                            if (opcioTancament != TANCAMENT_INVALID) {
                                int indexRival;
                                if (jugadorActual == Joc.arrayJugadors[0]) {
                                    indexRival = 1;
                                } else {
                                    indexRival = 0;
                                }
                                Jugador jugadorRival = Joc.arrayJugadors[indexRival];
                                jocActual.demanarCombinacionsRival(jugadorRival);

                                jocActual.mirarGuanyadorRonda(jugadorActual, opcioTancament);
                                accioCompletada = true;
                                hiHaGuanyadorRonda = true;
                            }
                        }

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
    }

    private void inicialitzarNovaRonda() {
        Joc.pilaDescartades.clear();
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
        Joc.pilaDescartades.add(cartaInicial);
    }

    private EvaluarMa calcularCombinacions(ArrayList<Carta> maJugador) {
        EvaluarMa millorEvaluacio = new EvaluarMa();
        millorEvaluacio.puntsMorts = calcularPuntsMortsCombinacio(maJugador);
        millorEvaluacio.cartesMortes = new ArrayList<>(maJugador);

        int numCartes = maJugador.size();
        int mascaraMaxima = 1 << numCartes;

        for (int mascara = 1; mascara < mascaraMaxima; mascara++) {
            if (Integer.bitCount(mascara) >= 3) {
                ArrayList<Carta> cartesCombinades = new ArrayList<>();
                ArrayList<Carta> cartesDescartades = new ArrayList<>();

                for (int i = 0; i < numCartes; i++) {
                    if ((mascara & (1 << i)) != 0) {
                        cartesCombinades.add(maJugador.get(i));
                    } else {
                        cartesDescartades.add(maJugador.get(i));
                    }
                }

                boolean combinacioValida = esCombinacioValida(cartesCombinades);

                if (combinacioValida) {
                    EvaluarMa novaEvaluacio = calcularCombinacions(cartesDescartades);

                    if (novaEvaluacio.puntsMorts < millorEvaluacio.puntsMorts) {
                        millorEvaluacio.puntsMorts = novaEvaluacio.puntsMorts;
                        millorEvaluacio.combinacionsPossibles = new ArrayList<>(novaEvaluacio.combinacionsPossibles);
                        millorEvaluacio.combinacionsPossibles.add(cartesCombinades);
                        millorEvaluacio.cartesMortes = novaEvaluacio.cartesMortes;
                    }
                }
            }
        }
        return millorEvaluacio;
    }

    private int calcularPuntsMortsCombinacio(ArrayList<Carta> maCartes) {
        int punts = 0;
        for (int i = 0; i < maCartes.size(); i++) {
            Carta cartaActu = maCartes.get(i);
            punts += cartaActu.getValor();
        }
        return punts;
    }

    private void imprimirMillorMa(EvaluarMa millorMa) {
        Consola.missatgeMillorCombinacio();
        Consola.mostrarCombinacionsPossibles(millorMa.combinacionsPossibles);
        Consola.missatgeCartesMortes(millorMa.cartesMortes);
        Consola.missatgePuntsMorts(millorMa.puntsMorts);
    }

    private void mourePosCarta(Jugador jugador) {
        int indexCarta = Consola.demanarIndexCarta(jugador.maCartes);
        if (indexCarta < 0 || indexCarta >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        Carta carta = jugador.maCartes.get(indexCarta);
        jugador.maCartes.remove(indexCarta);

        int indexNovaPos = Consola.demanarPosicioDinsCombiancio(jugador.maCartes);
        if (indexNovaPos < 0 || indexNovaPos > jugador.maCartes.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        jugador.maCartes.add(indexNovaPos, carta);
    }

    private int tancarRonda(Jugador jugador) {
        ArrayList<Carta> copiaMa = new ArrayList<>(jugador.maCartes);
        ArrayList<ArrayList<Carta>> combinacionsTriades = tirarCombinacions(jugador);

        for (int i = 0; i < combinacionsTriades.size(); i++) {
            ArrayList<Carta> combinacioActu = combinacionsTriades.get(i);
            boolean esValida = esCombinacioValida(combinacioActu);
            if (!esValida) {
                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMa);
                Consola.missatgeCombinacioNoValida();
                return 0;
            }
        }

        if (jugador.maCartes.isEmpty()) {
            return 1;
        } else {
            int indexCartaDescartar = 0;
            Carta cartaDescartada = jugador.maCartes.get(0);
            for (int i = 1; i < jugador.maCartes.size(); i++) {
                if (jugador.maCartes.get(i).getValor() > cartaDescartada.getValor()) {
                    cartaDescartada = jugador.maCartes.get(i);
                    indexCartaDescartar = i;
                }
            }
            jugador.maCartes.remove(indexCartaDescartar);
        }

        calcularPuntsMa(jugador);
        if (jugador.puntsMa > 10) {
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMa);
            Consola.missatgePuntsMorts(jugador.puntsMa);
            return 0;
        } else if (jugador.puntsMa == 0) {
            return 2;
        }
        return 3;
    }

    private void demanarCombinacionsRival(Jugador jugadorRival) {
        Consola.missatgeRivalTiraCombi(jugadorRival);
        boolean combinacionsRivalAcabades = false;
        ArrayList<ArrayList<Carta>> combinacionsRival = new ArrayList<>();
        while (!combinacionsRivalAcabades) {
            boolean combinacionsRivalValides = true;
            combinacionsRival = tirarCombinacions(jugadorRival);
            for (int i = 0; i < combinacionsRival.size(); i++) {
                boolean esValida = esCombinacioValida(combinacionsRival.get(i));
                if (!esValida) {
                    combinacionsRivalValides = false;
                    Consola.missatgeCombinacioNoValida();
                    break;
                }
            }
            if (combinacionsRivalValides || combinacionsRival.isEmpty()) {
                combinacionsRivalAcabades = true;
            }
        }
    }

    private void combinacionsOrdenades(ArrayList<ArrayList<Carta>> llistaCombinacions) {
        for (int combinacio = 0; combinacio < llistaCombinacions.size(); combinacio++) {
            ordenarCombinacions(llistaCombinacions.get(combinacio));
        }
    }

    private ArrayList<ArrayList<Carta>> tirarCombinacions(Jugador jugador) {
        ArrayList<ArrayList<Carta>> combinacionsTriades = new ArrayList<>();
        boolean volFerCombinacions = Consola.seguirCreantCombinacions(jugador);

        while (volFerCombinacions) {
            ArrayList<Carta> combinacioActual = Consola.demanarNovaCombinacioGeneral(jugador);
            if (combinacioActual != null && !combinacioActual.isEmpty()) {
                combinacionsTriades.add(combinacioActual);
            }
            volFerCombinacions = Consola.seguirCreantCombinacions(jugador);
        }
        return combinacionsTriades;
    }

    private void mirarGuanyadorRonda(Jugador jugadorQueHaTancat, int opcioTancament) {
        int indexRival;
        String tipusVictoria = "";

        if (jugadorQueHaTancat == Joc.arrayJugadors[0]) {
            indexRival = 1;
        } else {
            indexRival = 0;
        }
        Jugador jugadorRival = Joc.arrayJugadors[indexRival];

        calcularPuntsMa(jugadorQueHaTancat);
        calcularPuntsMa(jugadorRival);

        if (opcioTancament == TANCAMENT_BIG_GIN) {
            jugadorQueHaTancat.puntuacio += jugadorRival.puntsMa + 50;
            tipusVictoria = "Big Gin";
            Consola.missatgeGuanyadorRondakGin(jugadorQueHaTancat, tipusVictoria);
        } else if (opcioTancament == TANCAMENT_GIN) {
            jugadorQueHaTancat.puntuacio += jugadorRival.puntsMa + 25;;
            tipusVictoria = "Gin";
            Consola.missatgeGuanyadorRondakGin(jugadorQueHaTancat, tipusVictoria);
        } else if (opcioTancament == TANCAMENT_KNOCK) {
            if (jugadorQueHaTancat.puntsMa < jugadorRival.puntsMa) {
                jugadorQueHaTancat.puntuacio += jugadorRival.puntsMa - jugadorQueHaTancat.puntsMa;
                tipusVictoria = "Knock";
                Consola.missatgeGuanyadorRondakGin(jugadorQueHaTancat, tipusVictoria);
            } else {
                jugadorRival.puntuacio += (jugadorQueHaTancat.puntsMa - jugadorRival.puntuacio + 25);
                Consola.missatgePerdutRondaGin(jugadorQueHaTancat);
            }
        }
    }

    @Override
    public boolean haGuanyat(Jugador jugador) {
        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Jugador jugadorActual = Joc.arrayJugadors[i];
            if (jugadorActual.puntuacio >= 100) {
                Consola.missatgeGuanyador(jugadorActual);
                return true;
            }
        }
        return false;
    }

    public void calcularPuntsMa(Jugador jugador) {
        jugador.puntsMa = 0;

        for (int i = 0; i < jugador.maCartes.size(); i++) {
            Carta cartaActu = jugador.maCartes.get(i);
            jugador.puntsMa += cartaActu.getValor();
        }
    }

}